package com.nft.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.TeamInfo;
import com.nft.common.core.domain.pojo.user.*;
import com.nft.common.core.redis.RedisCache;
import com.nft.common.enums.*;
import com.nft.common.tencent.TimeUtils;
import com.nft.common.tencent.pay.Json;
import com.nft.common.utils.CopyUtils;
import com.nft.common.utils.DateUtils;
import com.nft.common.utils.RandomUtils;
import com.nft.common.utils.StringUtils;
import com.nft.common.utils.web3.Web3jUtils;
import com.nft.mall.domain.*;
import com.nft.mall.domain.vo.CertificationVO;
import com.nft.mall.mapper.*;
import com.nft.mall.service.IBuildModelService;
import com.nft.mall.service.IThirdPartyService;
import com.nft.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户Service业务层处理
 *
 * @author nft
 * @date 2021-05-19
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private CertificationMapper certificationMapper;

    @Autowired
    private CertificationAttachMapper certificationAttachMapper;

    @Autowired
    private SaveKeyMapper saveKeyMapper;

    @Autowired
    private ProductTradeMapper productTradeMapper;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private ReleaseApplyMapper releaseApplyMapper;

    @Autowired
    private CertificationWithdrawMapper withdrawMapper;

    @Autowired
    private WithdrawConfigMapper withdrawConfigMapper;

    @Autowired
    private BankConfigMapper bankConfigMapper;

    @Autowired
    private RoughRecordMapper roughRecordMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private CertificationBankMapper certificationBankMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IBuildModelService buildModelService;

    @Autowired
    private IThirdPartyService thirdPartyService;

    /**
     * 查询注册账号是否已经存在
     *
     * @param account 账号
     * @return 结果
     */
    @Override
    public AjaxResult checkAccountIsExist(String account, int type) {
        if (StringUtils.isEmpty(account)) {
            return AjaxResult.error("请输入账号");
        }
        List<Certification> certificationList = certificationMapper.listByAccount(account);
        if (CollUtil.isNotEmpty(certificationList)) {
            return AjaxResult.error("The email has already existed.");
        }
        return AjaxResult.success();
    }

    /**
     * 查询地址已经存在
     *
     * @param address 账号
     * @return 结果
     */
    @Override
    public AjaxResult checkAddressIsExist(String address) {
        if (StringUtils.isEmpty(address)) {
            return AjaxResult.error("请输入地址");
        }
        Certification certification = certificationMapper.selectByWalletAddress(address);
        if (ObjectUtil.isNotNull(certification) && StrUtil.isNotBlank(certification.getWalletAddress())) {
            return AjaxResult.error("地址已存在,请直接登录!");
        }
        return AjaxResult.success();
    }

    /**
     * 注册账号
     *
     * @param registerUserParam 账号信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult register(RegisterUserParam registerUserParam) {
        if (ObjectUtil.isNull(registerUserParam)) {
            return AjaxResult.error("注册失败");
        }

        // 账号
        String account = registerUserParam.getAccount();
        // 查询账号是否已经存在
        Certification certification = certificationMapper.selectByAccountAndType(account, CertificationType.PERSON.getCode());
        int effectNum = 0;
        if (ObjectUtil.isNotNull(certification)) {
            // 查询是否有登录密码，有的话说明注册过了
            SaveKey saveKey = saveKeyMapper.selectByCertificationId(certification.getId());
            if (ObjectUtil.isNotNull(saveKey)) {
                return AjaxResult.error("该帐号已注册，请直接登录");
            }
            certification.setMobile(account);
            certification.setType(CertificationType.PERSON.getCode());
            certification.setStatus(CertificationStatus.NOT_REAL.getCode());
            certification.setWalletAddress(registerUserParam.getWalletAddress());
            certification.setPublicKey(registerUserParam.getPublicKey());
            certification.setUpdateTime(new Date());
            effectNum = certificationMapper.updateCertification(certification);
            return effectNum == 1 ? AjaxResult.success("注册成功") : AjaxResult.success("注册失败");
        }
        if (StringUtils.isNotBlank(registerUserParam.getOtherCode())) {
            Certification existCertification = certificationMapper.selectByMyCode(registerUserParam.getOtherCode());
            if (ObjectUtil.isNull(existCertification)) {
                return AjaxResult.error("推荐人不存在");
            }
        }
        // 新增用户信息
        Certification addCertification = CopyUtils.copy(registerUserParam, Certification.class);
        addCertification.setType(CertificationType.PERSON.getCode());
        addCertification.setMobile(account);
        addCertification.setStatus(CertificationStatus.NOT_REAL.getCode());
        addCertification.setCreateTime(new Date());
        // 设置邀请码和推荐码
        addCertification.setMyCode(RandomUtils.genCharStringByNum(6).toLowerCase());
        effectNum = certificationMapper.insertCertification(addCertification);
        Long certificationId = addCertification.getId();
        if (effectNum != 1 || ObjectUtil.isNull(certificationId)) {
            return AjaxResult.error("注册失败");
        }
        // 新增用户补充信息
        CertificationAttach attach = new CertificationAttach();
        attach.setCertificationId(certificationId);
        attach.setStatus(EnableStatus.ENABLE.getCode());
        attach.setCreateTime(new Date());
        effectNum = certificationAttachMapper.insertCertificationAttach(attach);
        if (effectNum != 1 || ObjectUtil.isNull(certificationId)) {
            return AjaxResult.error("注册失败");
        }
        // 用户私钥托管
        SaveKey saveKey = new SaveKey();
        saveKey.setCertificationId(certificationId);
        saveKey.setPrivateKey(registerUserParam.getEncryptPrivateKey());
        saveKey.setPrivatePass(registerUserParam.getEncryptPassword());
        saveKey.setCreateId(certificationId);
        saveKey.setCreateTime(new Date());
        effectNum = saveKeyMapper.insertSaveKey(saveKey);
        if (effectNum == 1) {
            // 移除验证码
            redisCache.deleteObject("code_" + account);
        }
        // 给用户发送邮件
        thirdPartyService.sendHtmlMailForRegister(account);
        return effectNum == 1 ? AjaxResult.success("注册成功") : AjaxResult.success("注册失败");
    }

    /**
     * 登录(密码)
     *
     * @param account 账号信息
     * @return 结果
     */
    @Override
    public AjaxResult loginByPwd(String account) {
        if (StringUtils.isBlank(account)) {
            return AjaxResult.error("登录失败");
        }
        List<Certification> certificationList = certificationMapper.listByAccount(account);
        if (CollectionUtil.isEmpty(certificationList)) {
            return AjaxResult.error("用户不存在,请去注册");
        }
        Certification certification = certificationList.get(0);
        SaveKey saveKey = saveKeyMapper.selectByCertificationId(certification.getId());
        if (ObjectUtil.isNull(saveKey)) {
            return AjaxResult.error("登录失败");
        }
        RegisterUserParam userInfo = new RegisterUserParam();
        userInfo.setLoginId(certification.getId());
        userInfo.setAccount(StringUtils.trimToEmpty(account));
        userInfo.setWalletAddress(certification.getWalletAddress());
        userInfo.setPublicKey(certification.getPublicKey());
        userInfo.setEncryptPrivateKey(saveKey.getPrivateKey());
        userInfo.setEncryptPassword(saveKey.getPrivatePass());
        return AjaxResult.success("登录成功", userInfo);
    }

    /**
     * 登录(地址)
     *
     * @param walletAddress 地址信息
     * @return 结果
     */
    @Override
    public AjaxResult loginByWalletAddress(String walletAddress) {
        if (StringUtils.isBlank(walletAddress)) {
            return AjaxResult.error("登录失败");
        }
        Certification certification = certificationMapper.selectByWalletAddress(walletAddress);
        if (ObjectUtil.isNull(certification)) {
//            return AjaxResult.error("登录失败");
            System.out.println("登录失败");
            // 这里进行用户注册

            // 新增用户
// 账号
//            account: this.ruleForm.account,
//                    walletAddress: this.ruleForm.walletAddress,
//                    publicKey: this.ruleForm.publicKey,
//                    encryptPrivateKey: this.ruleForm.privateKey,
//                    encryptPassword: this.ruleForm.sePass,
//                    msgCode: this.ruleForm.msgCode,
//                    otherCode: this.ruleForm.otherCode
            String account = "";  // contract way

            // 新增用户信息
            Certification addCertification = new Certification();
            addCertification.setWalletAddress(walletAddress);
            addCertification.setType(CertificationType.PERSON.getCode());
            addCertification.setMobile(account);
            addCertification.setStatus(CertificationStatus.SUCCESS.getCode());
            addCertification.setCreateTime(new Date());
            addCertification.setReleaseStatus(ReleaseStatus.YES.getCode());
            certificationMapper.insertCertification(addCertification);

            certification = certificationMapper.selectByWalletAddress(walletAddress);
            // 新增用户补充信息
            CertificationAttach attach = new CertificationAttach();
            attach.setCertificationId(certification.getId());
            attach.setStatus(EnableStatus.ENABLE.getCode());
            attach.setCreateTime(new Date());
            certificationAttachMapper.insertCertificationAttach(attach);

        }

        certification = certificationMapper.selectByWalletAddress(walletAddress);

        RegisterUserParam userInfo = new RegisterUserParam();
        userInfo.setLoginId(certification.getId());
        userInfo.setAccount(StringUtils.trimToEmpty(certification.getMobile()));
        userInfo.setWalletAddress(StringUtils.trimToEmpty(certification.getWalletAddress()));
        userInfo.setPublicKey(StringUtils.trimToEmpty(certification.getPublicKey()));
//        userInfo.setEncryptPrivateKey(StringUtils.trimToEmpty(saveKey.getPrivateKey()));
//        userInfo.setEncryptPassword(StringUtils.trimToEmpty(saveKey.getPrivatePass()));
        return AjaxResult.success("登录成功", userInfo);
    }

    /**
     * 通过私钥重置托管密码
     *
     * @param resetPwdParam 密码信息
     * @return 结果
     */
    @Override
    public AjaxResult resetPwdByPrivateKey(ResetPwdParam resetPwdParam) {
        if (Objects.isNull(resetPwdParam) || CollectionUtils.isEmpty(resetPwdParam.getResetEncryptParamList())) {
            return AjaxResult.error("重置失败");
        }
        boolean isContinue = true;
        int effectNum;
        List<ResetEncryptParam> encryptInfoList = resetPwdParam.getResetEncryptParamList();
        for (ResetEncryptParam info : encryptInfoList) {
            Long operationId = info.getOperationId();
            Certification certification = certificationMapper.selectByIdAndType(operationId, info.getType());
            if (ObjectUtil.isNull(certification)) {
                isContinue = false;
                break;
            }
            if (info.getType().equals(CertificationType.PERSON.getCode())) {
                SaveKey saveKey = new SaveKey();
                saveKey.setCertificationId(operationId);
                saveKey.setPrivatePass(StringUtils.trimToEmpty(info.getEncryptPassword()));
                saveKey.setPrivateKey(StringUtils.trimToEmpty(info.getEncryptPrivateKey()));
                effectNum = saveKeyMapper.updateByCertificationId(saveKey);
                if (effectNum < 1) {
                    isContinue = false;
                    break;
                }
            }
        }
        return isContinue ? AjaxResult.success("重置成功") : AjaxResult.error("重置失败");
    }

    /**
     * 通过地址查询用户信息
     *
     * @param walletAddress 地址
     * @return 结果
     */
    @Override
    public Certification getByWalletAddress(String walletAddress) {
        return certificationMapper.selectByWalletAddress(walletAddress);
    }

    /**
     * 根据account和type获得详细信息
     *
     * @param account 用户账号
     * @param type    类型
     * @return 结果
     */
    @Override
    public AjaxResult getUserByAccountAndType(String account, Integer type) {
        Certification certification = certificationMapper.selectByAccountAndType(account, type);
        List<SaveKey> saveKeys = saveKeyMapper.listSaveKeyById(certification.getId());
        List<String> keys = saveKeys.stream().map(c -> c.getPrivateKey()).collect(Collectors.toList());
        CertificationVO certificationVO = CopyUtils.copy(certification, CertificationVO.class);
        certificationVO.setPrivateKeys(keys);
        return AjaxResult.success("查询成功", certificationVO);
    }

    /**
     * 根据id和type获得详细信息
     *
     * @param certificationId 用户id
     * @param type            类型
     * @return 结果
     */
    @Override
    public AjaxResult getMsgByIds(Long certificationId, Integer type) {
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        List<SaveKey> saveKeys = saveKeyMapper.listSaveKeyById(certificationId);
        List<String> keys = saveKeys.stream().map(c -> c.getPrivateKey()).collect(Collectors.toList());
        CertificationVO certificationVO = CopyUtils.copy(certification, CertificationVO.class);
        certificationVO.setPrivateKeys(keys);
        return AjaxResult.success("查询成功", certificationVO);
    }

    /**
     * 根据地址删除实名信息
     *
     * @param walletAddress
     * @return 结果
     */
    @Override
    public AjaxResult deleteByWalletAddress(String walletAddress) {
        int effectNum = certificationMapper.deleteByWalletAddress(walletAddress);
        return effectNum == 1 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 根据地址 名称 证件号 验证是否实名
     *
     * @param map
     * @return 结果
     */
    @Override
    public Certification getCertificationByMap(Map<String, String> map) {
        return certificationMapper.selectInfoByMap(map);
    }

    /**
     * 查询用户团队列表
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult getTeamList(Long certificationId) {
        // 查询用户信息
        Certification existCertification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(existCertification)) {
            return AjaxResult.error("用户不存在");
        }
        if (StringUtils.isBlank(existCertification.getMyCode())) {
            return AjaxResult.error("邀请码不存在");
        }
        // 查询推荐人列表
        List<Certification> certificationList = certificationMapper.selectByReferralCode(existCertification.getMyCode());
        if (CollectionUtil.isEmpty(certificationList)) {
            return AjaxResult.success(Lists.newArrayList());
        }
        // 推荐人ID集合
        Set<Long> certificationIdSet = certificationList.stream().map(Certification::getId).collect(Collectors.toSet());
        // 查询推荐人交易列表
        List<ProductTrade> productTradeList = productTradeMapper.selectByCertificationIdSetAndStatus(certificationIdSet, Sets.newHashSet(CollectTradeStatus.SUCCESS.getCode()));
        // 交易映射关系
        Map<Long, List<ProductTrade>> productTradeListMap = productTradeList.stream().collect(Collectors.groupingBy(ProductTrade::getToId));
        // 查询用户补充信息列表
        List<CertificationAttach> certificationAttachList = certificationAttachMapper.selectByCertificationIdSet(certificationIdSet);
        Map<Long, CertificationAttach> certificationAttachMap = certificationAttachList.stream().collect(Collectors.toMap(CertificationAttach::getCertificationId, Function.identity()));
        List<TeamInfo> teamInfoList = Lists.newArrayList();
        TeamInfo teamInfo;
        for (Certification certification : certificationList) {
            teamInfo = new TeamInfo();
            List<ProductTrade> productTradeListByMap = productTradeListMap.getOrDefault(certification.getId(), null);
            if (CollectionUtil.isNotEmpty(productTradeListByMap)) {
                BigDecimal tradeAmount = productTradeListByMap.stream().map(ProductTrade::getTradeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                teamInfo.setTradeAmount(tradeAmount);
            } else {
                teamInfo.setTradeAmount(BigDecimal.ZERO);
            }
            CertificationAttach attach = certificationAttachMap.getOrDefault(certification.getId(), null);
            boolean isVerify = certification.getStatus().equals(CertificationStatus.SUCCESS.getCode());
            teamInfo.setIsVerify(isVerify ? 1 : 0);
            teamInfo.setRealName(isVerify ? certification.getRealName() : certification.getMobile());
            teamInfo.setStatusDesc(isVerify ? "已实名" : "未实名");
            teamInfo.setAccount(certification.getMobile());
            teamInfo.setNickName(StringUtils.isEmpty(attach.getNickName()) ? certification.getMobile() : attach.getNickName());
            teamInfo.setAvatarUrl(attach.getAvatarUrl());
            teamInfo.setCreateTime(DateUtils.dateTime(certification.getCreateTime()));
            teamInfoList.add(teamInfo);
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        BigDecimal tradeAmount = teamInfoList.stream().map(TeamInfo::getTradeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        resultMap.put("teamCount", teamInfoList.size());
        resultMap.put("teamAmount", tradeAmount);
        resultMap.put("teamInfoList", teamInfoList);
        return AjaxResult.success("查询成功", resultMap);
    }

    /**
     * 获取用户详细信息
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult getUserInfo(Long certificationId) {
        // 查询用户信息
        Certification existCertification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(existCertification)) {
            return AjaxResult.error("用户不存在");
        }
        UserInfo userInfo = CopyUtils.copy(existCertification, UserInfo.class);
        // 查询用户补充信息
        List<CertificationAttach> attachList = certificationAttachMapper.selectByCertificationIdSet(Sets.newHashSet(certificationId));
        if (CollectionUtil.isEmpty(attachList)) {
            return AjaxResult.error("用户不存在");
        }
        if (StringUtils.isBlank(existCertification.getOtherCode())) {
            userInfo.setRecommendName("no data");
        }
        // 查询用户推荐人
//        Certification recommend = certificationMapper.selectByMyCode(existCertification.getOtherCode());
//        userInfo.setRecommendName(ObjectUtil.isNull(recommend) ? "no data" : recommend.getMobile());
//        if (ObjectUtil.isNotNull(recommend)) {
//            // 查询用户推荐人补充信息
//            List<CertificationAttach> recommendAttachList = certificationAttachMapper.selectByCertificationIdSet(Sets.newHashSet(recommend.getId()));
//            if (CollectionUtil.isEmpty(recommendAttachList)) {
//                return AjaxResult.error("用户不存在");
//            }
//            CertificationAttach recommendAttach = recommendAttachList.get(0);
//            userInfo.setRecommendAvatarUrl(StringUtils.trimToEmpty(recommendAttach.getAvatarUrl()));
//        }
        // 查询用户推荐列表
        List<Certification> certificationList = certificationMapper.selectByReferralCode(existCertification.getMyCode());
        CertificationAttach attach = attachList.get(0);
        userInfo.setNickName(StringUtils.isBlank(attach.getNickName()) ? existCertification.getMobile() : attach.getNickName());
        // 查询用户余额
        Balance balance = balanceMapper.selectBalanceByUserId(certificationId);
        userInfo.setBalance(ObjectUtil.isNull(balance) ? BigDecimal.ZERO : balance.getBalance());
        userInfo.setAvatarUrl(attach.getAvatarUrl());
        userInfo.setIntroduction(attach.getIntroduction());
        userInfo.setTeamCount(certificationList.size());
        // 查询用户藏品发布申请列表
        ReleaseApply releaseApply = releaseApplyMapper.selectByCertificationId(certificationId);
        userInfo.setApplyStatus(ObjectUtil.isNull(releaseApply) ? ApplyStatus.WAIT.getCode() : releaseApply.getApplyStatus());
        return AjaxResult.success("操作成功", userInfo);
    }

    /**
     * 用户修改昵称
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult updateNickName(Long certificationId, String nickName) {
        // 查询用户信息
        Certification existCertification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(existCertification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户补充信息
        List<CertificationAttach> attachList = certificationAttachMapper.selectByCertificationIdSet(Sets.newHashSet(certificationId));
        if (CollectionUtil.isEmpty(attachList)) {
            return AjaxResult.error("用户不存在");
        }
        CertificationAttach attach = attachList.get(0);
        CertificationAttach updateAttach = new CertificationAttach();
        updateAttach.setCertificationAttachId(attach.getCertificationAttachId());
        updateAttach.setNickName(nickName);
        updateAttach.setUpdateTime(new Date());
        int effectNum = certificationAttachMapper.updateCertificationAttach(updateAttach);
        return effectNum == 1 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }

    @Override
    public AjaxResult updateMobile(Long certificationId, String mobile) {
        // 查询用户信息
        Certification existCertification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(existCertification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户补充信息
        List<CertificationAttach> attachList = certificationAttachMapper.selectByCertificationIdSet(Sets.newHashSet(certificationId));
        if (CollectionUtil.isEmpty(attachList)) {
            return AjaxResult.error("用户不存在");
        }

        Certification updateCertification = new Certification();
        updateCertification.setId(certificationId);
        updateCertification.setMobile(mobile);
        updateCertification.setUpdateTime(new Date());
        int effectNum = certificationMapper.updateCertification(updateCertification);
        if (effectNum == 1) {
            // 给用户发送邮件
//            thirdPartyService.sendHtmlMailForRealName(mobile);
            return AjaxResult.success("操作成功");
        }
        return AjaxResult.error("操作失败");
    }

    /**
     * 用户修改简介
     *
     * @param certificationId 用户ID
     * @param introduction    简介
     * @return 结果
     */
    @Override
    public AjaxResult updateIntroduction(Long certificationId, String introduction) {
        // 查询用户信息
        Certification existCertification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(existCertification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户补充信息
        List<CertificationAttach> attachList = certificationAttachMapper.selectByCertificationIdSet(Sets.newHashSet(certificationId));
        if (CollectionUtil.isEmpty(attachList)) {
            return AjaxResult.error("用户不存在");
        }
        CertificationAttach attach = attachList.get(0);
        CertificationAttach updateAttach = new CertificationAttach();
        updateAttach.setCertificationAttachId(attach.getCertificationAttachId());
        updateAttach.setIntroduction(introduction);
        updateAttach.setUpdateTime(new Date());
        int effectNum = certificationAttachMapper.updateCertificationAttach(updateAttach);
        return effectNum == 1 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }

    /**
     * 用户修改实名状态
     *
     * @param userAuthParam 用户实名信息
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult updateUserAuthStatus(UserAuthParam userAuthParam) {
        if (ObjectUtil.isNull(userAuthParam)) {
            return AjaxResult.error("参数错误");
        }
        // 查询用户信息
        Long certificationId = userAuthParam.getCertificationId();
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户状态
        if (certification.getStatus() == CertificationStatus.SUCCESS.getCode()) {
            return AjaxResult.error("不能重复认证");
        }
//        if (StringUtils.isNotBlank(certification.getOtherCode())) {
//            Certification invitor = certificationMapper.selectByMyCode(certification.getOtherCode());
//            if (ObjectUtil.isNotNull(invitor)) {
//                Integer rewardFrom = RoughRewardFrom.RECOMMEND.getCode();
//                BigDecimal invitorAmount = userAuthParam.getInvitorAmount();
//                String walletAddress = invitor.getWalletAddress();
//                // 链上原石激励
//                String hash = Web3jUtils.mint(rewardFrom, invitorAmount, walletAddress, userAuthParam.getContractAddress());
//                // 构建原石记录
//                Integer rewardStatus = StringUtils.isBlank(hash) ? RoughStatus.FAIL.getCode() : RoughStatus.OK.getCode();
//                RoughRecord roughRecord = buildModelService.buildRoughRecord(invitor, invitorAmount, hash, rewardFrom, rewardStatus);
//                roughRecordMapper.insertRoughRecord(roughRecord);
//            }
//        }
        // 更新用户状态
        Certification updateCertification = new Certification();
        updateCertification.setId(certificationId);
        updateCertification.setRealName(userAuthParam.getRealName());
        updateCertification.setNumber(userAuthParam.getNumber());
        updateCertification.setStatus(CertificationStatus.SUCCESS.getCode());
        updateCertification.setRzTime(new Date());
        updateCertification.setUpdateTime(new Date());
        int effectNum = certificationMapper.updateCertification(updateCertification);
        if (effectNum == 1) {
            // 给用户发送邮件
            thirdPartyService.sendHtmlMailForRealName(certification.getMobile());
            return AjaxResult.success("实名成功");
        }
        return AjaxResult.error("实名失败");
    }

    /**
     * 用户充值
     *
     * @param chargeParam 充值信息
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult addCharge(ChargeParam chargeParam) {
        if (ObjectUtil.isNull(chargeParam)) {
            return AjaxResult.error("参数错误");
        }
        // 查询用户信息
        String address = chargeParam.getAddress();
        Certification certification = certificationMapper.selectByWalletAddress(chargeParam.getAddress());
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户补充信息
        List<CertificationAttach> attachList = certificationAttachMapper.selectByCertificationIdSet(Sets.newHashSet(certification.getId()));
        if (CollectionUtil.isEmpty(attachList)) {
            return AjaxResult.error("用户不存在");
        }
        CertificationAttach attach = attachList.get(0);
        // 链上充值
        Json chargeJson = Web3jUtils.charge(chargeParam.getMoney().multiply(BigDecimal.TEN.pow(18)).toBigInteger(), address);
        if (!chargeJson.isFlag()) {
            return AjaxResult.error("充值失败");
        }
        // 用户新增充值记录
        Charge charge = CopyUtils.copy(chargeParam, Charge.class);
        charge.setCertificationId(certification.getId());
        int randomNum = (int) (Math.random() * 1999 + 5000);
        // 订单号
        String outTradeNo = TimeUtils.getSysTime("yyyyMMddHHmmss") + randomNum;
        charge.setId(outTradeNo);
        charge.setStatus(ChargeStatus.SUCCESS.getCode());
        charge.setCreateTime(new Date());
        chargeMapper.insertCharge(charge);
        // 更新用户余额信息
        CertificationAttach updateAttach = new CertificationAttach();
        updateAttach.setCertificationAttachId(attach.getCertificationAttachId());
        updateAttach.setCertificationId(certification.getId());
        updateAttach.setBalance(attach.getBalance().add(chargeParam.getMoney()));
        updateAttach.setUpdateTime(new Date());
        int effectNum = certificationAttachMapper.updateCertificationAttach(updateAttach);
        return effectNum == 1 ? AjaxResult.success("充值成功") : AjaxResult.error("充值失败");
    }

    /**
     * 获取用户充值列表
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult getChargeList(Long certificationId, Integer status) {
        // 查询用户信息
        Certification existCertification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(existCertification)) {
            return AjaxResult.error("用户不存在");
        }
        List<Charge> chargeList = chargeMapper.listByCertificationIdAndStatus(certificationId, status);
        List<ChargeInfo> chargeInfoList = CopyUtils.copyList(chargeList, ChargeInfo.class);
        chargeInfoList.stream().forEach(chargeInfo -> {
            ChargeType typeEnum = ChargeType.getByCode(chargeInfo.getType());
            chargeInfo.setTypeDesc(ObjectUtil.isNull(typeEnum) ? "" : typeEnum.getMessage());
            ChargeStatus statusEnum = ChargeStatus.getByCode(chargeInfo.getStatus());
            chargeInfo.setStatusDesc(ObjectUtil.isNull(statusEnum) ? "" : statusEnum.getMessage());
            chargeInfo.setChargeTime(DateUtil.formatDateTime(chargeInfo.getCreateTime()));
            String typeMessage = ChargeType.getTypeMessage(chargeInfo.getType());
            chargeInfo.setChargeSource(typeMessage);
        });
        return AjaxResult.success("操作成功", chargeInfoList);
    }

    /**
     * 用户申请成为藏品发布者
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult addReleaseApply(Long certificationId) {
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户是否已实名
        if (!certification.getStatus().equals(CertificationStatus.SUCCESS.getCode())) {
            return AjaxResult.error("用户未实名,请先去实名!");
        }
        // 查询用户的申请状态
        if (certification.getReleaseStatus().equals(ReleaseStatus.YES.getCode())) {
            return AjaxResult.error("不能重复申请!");
        }
        // 查询用户申请信息
        ReleaseApply exitsApplyInfo = releaseApplyMapper.selectByCertificationIdAndStatus(certificationId, ApplyStatus.ING.getCode());
        if (ObjectUtil.isNotNull(exitsApplyInfo)) {
            return AjaxResult.error("不能重复申请!");
        }
        ReleaseApply releaseApply = new ReleaseApply();
        releaseApply.setCertificationId(certificationId);
        releaseApply.setApplyStatus(ApplyStatus.ING.getCode());
        releaseApply.setCreateTime(new Date());
        int effectNum = releaseApplyMapper.insertReleaseApply(releaseApply);
        return effectNum == 1 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }

    /**
     * 用户设置提现方式
     *
     * @param withdrawConfigParam 提现信息
     * @return 结果
     */
    @Override
    public AjaxResult addWithdrawConfig(WithdrawConfigParam withdrawConfigParam) {
        if (ObjectUtil.isNull(withdrawConfigParam)) {
            return AjaxResult.error("参数错误");
        }
        // 用户ID
        Long certificationId = withdrawConfigParam.getCertificationId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户是否实名
        if (certification.getStatus() != CertificationStatus.SUCCESS.getCode()) {
            return AjaxResult.error("用户未实名");
        }
        // 查询用户提现设置信息
        List<WithdrawConfig> existConfigList = withdrawConfigMapper.selectByCertificationIdAndType(certificationId, withdrawConfigParam.getConfigType());
        if (CollectionUtil.isNotEmpty(existConfigList)) {
            return AjaxResult.error("提现方式已存在");
        }
        // 新增提现设置
        WithdrawConfig withdrawConfig = CopyUtils.copy(withdrawConfigParam, WithdrawConfig.class);
        withdrawConfig.setRealName(certification.getRealName());
        withdrawConfig.setIdNumber(certification.getNumber());
        withdrawConfig.setCreateTime(new Date());
        int effectNum = withdrawConfigMapper.insertWithdrawConfig(withdrawConfig);
        return effectNum == 1 ? AjaxResult.success("设置成功") : AjaxResult.error("设置失败");
    }

    /**
     * 获取提现信息
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult getWithdrawConfig(Long certificationId, Integer type) {
        if (ObjectUtil.isNull(certificationId)) {
            return AjaxResult.error("参数错误");
        }
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询银行卡信息
        BankConfig bankConfig = new BankConfig();
        bankConfig.setStatus(EnableStatus.ENABLE.getCode());
        List<BankConfig> bankConfigList = bankConfigMapper.selectBankConfigList(bankConfig);
        Map<String, BankConfig> configMap = bankConfigList.stream().collect(Collectors.toMap(BankConfig::getBankName, Function.identity()));
        // 查询配置信息
        List<WithdrawConfig> configList = withdrawConfigMapper.selectByCertificationIdAndType(certificationId, type);
        List<WithdrawConfigInfo> configInfoList = CopyUtils.copyList(configList, WithdrawConfigInfo.class);
        configInfoList.stream().forEach(configInfo -> {
            BankConfig config = configMap.getOrDefault(configInfo.getAccountBank(), null);
            if (ObjectUtil.isNotNull(config)) {
                configInfo.setBankStyle(config.getBankStyle());
                configInfo.setBankType(config.getBankType());
            }
        });
        return AjaxResult.success("查询成功", configInfoList);
    }

    /**
     * 用户提现申请
     *
     * @param withdrawParam 提现申请信息
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult addWithdraw(WithdrawParam withdrawParam) {
        if (ObjectUtil.isNull(withdrawParam)) {
            return AjaxResult.error("参数错误");
        }
        // 查询用户信息
        Long certificationId = withdrawParam.getCertificationId();
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户是否已实名
        if (!certification.getStatus().equals(CertificationStatus.SUCCESS.getCode())) {
            return AjaxResult.error("用户未实名,请先去实名!");
        }
        // 查询用户附加信息
        List<CertificationAttach> attachList = certificationAttachMapper.selectByCertificationIdSet(Sets.newHashSet(certificationId));
        if (CollectionUtil.isEmpty(attachList)) {
            return AjaxResult.error("用户不存在");
        }
        CertificationAttach attach = attachList.get(0);
        // 查询用户提现设置
        WithdrawConfig config = withdrawConfigMapper.selectWithdrawConfigById(withdrawParam.getWithdrawConfigId());
        if (ObjectUtil.isNull(config)) {
            return AjaxResult.error("未设置提现信息");
        }
        // 用户可用余额
        BigDecimal balance = attach.getBalance();
        // 提现金额
        BigDecimal amount = withdrawParam.getWithdrawAmount();
        // 新增提现申请记录
        CertificationWithdraw withdraw = CopyUtils.copy(withdrawParam, CertificationWithdraw.class);
        // 订单号
        int randomNum = (int) (Math.random() * 1999 + 5000);
        String outTradeNo = TimeUtils.getSysTime("yyyyMMddHHmmss") + randomNum;
        withdraw.setOrderNumber(outTradeNo);
        withdraw.setWithdrawAccount(config.getBankCard());
        withdraw.setWithdrawStatus(WithdrawStatus.ING.getCode());
        withdraw.setCreateTime(new Date());
        withdrawMapper.insertCertificationWithdraw(withdraw);
        // 编辑用户可用余额
        CertificationAttach updateAttach = new CertificationAttach();
        updateAttach.setCertificationAttachId(attach.getCertificationAttachId());
        updateAttach.setBalance(balance.subtract(amount));
        updateAttach.setUpdateTime(new Date());
        int effectNum = certificationAttachMapper.updateCertificationAttach(updateAttach);
        return effectNum == 1 ? AjaxResult.success("提现申请已发送") : AjaxResult.error("提现申请失败");
    }

    /**
     * 用户提现申请
     *
     * @param withdrawParam 提现申请信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addBill(WithdrawParam withdrawParam) {
        if (ObjectUtil.isNull(withdrawParam)) {
            return AjaxResult.error("参数错误");
        }
        // 查询用户信息
        Long certificationId = withdrawParam.getCertificationId();
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户是否已实名
        if (!certification.getStatus().equals(CertificationStatus.SUCCESS.getCode())) {
            return AjaxResult.error("用户未实名,请先去实名!");
        }
        // 查询用户提现设置
        WithdrawConfig config = withdrawConfigMapper.selectWithdrawConfigById(withdrawParam.getWithdrawConfigId());
        if (ObjectUtil.isNull(config)) {
            return AjaxResult.error("未设置提现信息");
        }
        // 新增提现申请记录
        CertificationWithdraw withdraw = CopyUtils.copy(withdrawParam, CertificationWithdraw.class);
        // 订单号
        int randomNum = (int) (Math.random() * 1999 + 5000);
        String outTradeNo = TimeUtils.getSysTime("yyyyMMddHHmmss") + randomNum;
        withdraw.setOrderNumber(outTradeNo);
        withdraw.setWithdrawAccount(config.getBankCard());
        withdraw.setWithdrawStatus(WithdrawStatus.ING.getCode());
        withdraw.setCreateTime(new Date());
        withdrawMapper.insertCertificationWithdraw(withdraw);
        // 新增提现记录(交易手续费)
        CertificationWithdraw feeWithdraw = buildModelService.buildCertificationWithdraw(certification, withdrawParam.getFee(), WithdrawType.FEE.getCode(), WithdrawStatus.YES.getCode());
        int effectNum = withdrawMapper.insertCertificationWithdraw(feeWithdraw);
        return effectNum == 1 ? AjaxResult.success("提现申请已发送") : AjaxResult.error("提现申请失败");
    }

    /**
     * 获取用户提现列表
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult getWithdrawList(Long certificationId) {
        if (ObjectUtil.isNull(certificationId)) {
            return AjaxResult.error("参数错误");
        }
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询提现列表
        List<CertificationWithdraw> withdrawList = withdrawMapper.selectByCertificationId(certificationId);
        if (CollectionUtil.isEmpty(withdrawList)) {
            return AjaxResult.success("查询成功", Lists.newArrayList());
        }
        List<WithdrawInfo> withdrawInfoList = CopyUtils.copyList(withdrawList, WithdrawInfo.class);
        withdrawInfoList.stream().forEach(withdrawInfo -> {
            WithdrawStatus withdrawStatus = WithdrawStatus.getByCode(withdrawInfo.getWithdrawStatus());
            withdrawInfo.setStatusDesc(ObjectUtil.isNull(withdrawStatus) ? "" : withdrawStatus.getInfo());
            String typeMessage = WithdrawType.getTypeMessage(withdrawInfo.getWithdrawType(), 0);
            withdrawInfo.setWithdrawTime(DateUtil.formatDateTime(withdrawInfo.getCreateTime()));
            withdrawInfo.setTypeDesc(typeMessage);
            String withDrawGoal = WithdrawType.getTypeMessage(withdrawInfo.getWithdrawType(), 1);
            withdrawInfo.setWithdrawGoal(withDrawGoal);
        });
        return AjaxResult.success("查询成功", withdrawInfoList);
    }

    /**
     * 修改用户头像
     *
     * @param avatar          头像地址
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult updateUserAvatar(String avatar, Long certificationId) {
        if (ObjectUtil.isNull(certificationId)) {
            return AjaxResult.error("参数错误");
        }
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户附属信息
        List<CertificationAttach> attachList = certificationAttachMapper.selectByCertificationIdSet(Sets.newHashSet(certificationId));
        if (CollUtil.isEmpty(attachList)) {
            return AjaxResult.error("用户不存在");
        }
        CertificationAttach attach = attachList.get(0);
        CertificationAttach updateAttach = new CertificationAttach();
        updateAttach.setCertificationAttachId(attach.getCertificationAttachId());
        updateAttach.setAvatarUrl(StringUtils.trimToEmpty(avatar));
        updateAttach.setUpdateTime(new Date());
        int effectNum = certificationAttachMapper.updateCertificationAttach(updateAttach);
        return effectNum == 1 ? AjaxResult.success("操作成功", avatar) : AjaxResult.error();
    }

    /**
     * 个人/机构实名认证申请
     *
     * @param userRealNameParam 申请信息
     * @return 结果
     */
    @Override
    public AjaxResult addRealNameApply(UserRealNameParam userRealNameParam) {
        // 用户ID
        Long certificationId = userRealNameParam.getCertificationId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户状态
        if (certification.getStatus().equals(CertificationStatus.SUCCESS.getCode())) {
            return AjaxResult.error("已实名,请勿操作");
        }
        if (certification.getStatus().equals(CertificationStatus.WAIT_CHECK.getCode())) {
            return AjaxResult.error("审核中,请耐心等待");
        }
        Certification updateCertification = CopyUtils.copy(userRealNameParam, Certification.class);
        updateCertification.setStatus(CertificationStatus.WAIT_CHECK.getCode());
        updateCertification.setId(certificationId);
        updateCertification.setRealApplyTime(new Date());
        updateCertification.setUpdateTime(new Date());
        int effectNum = certificationMapper.updateCertification(updateCertification);
        // 给管理员发送邮件
        thirdPartyService.sendHtmlMailForAudit("allen@artlinx.HK");
        return effectNum == 1 ? AjaxResult.success("已提交审核,请耐心等待") : AjaxResult.error("操作失败");
    }

    /**
     * 用户设置银行卡
     *
     * @param bankParam 银行信息
     * @return 结果
     */
    @Override
    public AjaxResult addWCertificationBank(CertificationBankParam bankParam) {
        if (ObjectUtil.isNull(bankParam)) {
            return AjaxResult.error("参数错误");
        }
        // 用户ID
        Long certificationId = bankParam.getCertificationId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户是否实名
        if (certification.getStatus() != CertificationStatus.SUCCESS.getCode()) {
            return AjaxResult.error("用户未实名");
        }
        // 查询用户银行卡信息
        CertificationBank queryBank = new CertificationBank();
        queryBank.setCertificationId(certificationId);
        List<CertificationBank> bankList = certificationBankMapper.selectCertificationBankList(queryBank);
        if (CollectionUtil.isNotEmpty(bankList)) {
            return AjaxResult.error("银行卡已存在");
        }
        // 新增银行卡
        CertificationBank certificationBank = CopyUtils.copy(bankParam, CertificationBank.class);
        certificationBank.setEmail(certification.getMobile());
        certificationBank.setRealName(certification.getRealName());
        certificationBank.setIdNumber(certification.getNumber());
        certificationBank.setCreateTime(new Date());
        int effectNum = certificationBankMapper.insertCertificationBank(certificationBank);
        return effectNum == 1 ? AjaxResult.success("设置成功") : AjaxResult.error("设置失败");
    }

    /**
     * 用户更新银行卡
     *
     * @param bankParam 银行信息
     * @return 结果
     */
    @Override
    public AjaxResult updateWCertificationBank(CertificationBankParam bankParam) {
        if (ObjectUtil.isNull(bankParam)) {
            return AjaxResult.error("参数错误");
        }
        // 用户ID
        Long certificationId = bankParam.getCertificationId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户是否实名
        if (certification.getStatus() != CertificationStatus.SUCCESS.getCode()) {
            return AjaxResult.error("用户未实名");
        }
        // 查询用户银行卡信息
        CertificationBank queryBank = new CertificationBank();
        queryBank.setCertificationId(certificationId);
        List<CertificationBank> bankList = certificationBankMapper.selectCertificationBankList(queryBank);
        if (CollectionUtil.isEmpty(bankList)) {
            return AjaxResult.error("银行卡不存在");
        }
        // 更新银行卡
        CertificationBank updateBank = CopyUtils.copy(bankParam, CertificationBank.class);
        updateBank.setCertificationBankId(bankList.get(0).getCertificationBankId());
        updateBank.setUpdateTime(new Date());
        int effectNum = certificationBankMapper.updateCertificationBank(updateBank);
        return effectNum == 1 ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }

    /**
     * 获取银行卡信息
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    @Override
    public AjaxResult getCertificationBank(Long certificationId) {
        if (ObjectUtil.isNull(certificationId)) {
            return AjaxResult.error("参数错误");
        }
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询配置信息
        CertificationBank queryBank = new CertificationBank();
        queryBank.setCertificationId(certificationId);
        List<CertificationBank> bankList = certificationBankMapper.selectCertificationBankList(queryBank);
        List<CertificationBankInfo> bankInfoList = CopyUtils.copyList(bankList, CertificationBankInfo.class);
        return AjaxResult.success("查询成功", bankInfoList);
    }
}
