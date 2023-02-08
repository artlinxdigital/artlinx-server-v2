package com.nft.mall.api.controller.login;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.nft.common.annotation.Log;
import com.nft.common.config.NftConfig;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.collect.CollectionBuyParam;
import com.nft.common.core.domain.pojo.user.*;
import com.nft.common.core.redis.RedisCache;
import com.nft.common.enums.*;
import com.nft.common.tencent.pay.Json;
import com.nft.common.utils.DateUtils;
import com.nft.common.utils.StringUtils;
import com.nft.common.utils.ValidatorUtils;
import com.nft.common.utils.file.FileUploadUtils;
import com.nft.common.utils.web3.Web3jUtils;
import com.nft.config.RedisKeyExpirationListener;
import com.nft.mall.domain.*;
import com.nft.mall.mapper.MoneyHistoryMapper;
import com.nft.mall.mapper.ProductMapper;
import com.nft.mall.mapper.ProductTradeMapper;
import com.nft.mall.service.IChargeService;
import com.nft.mall.service.IUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author nft
 * @date 2021-05-12
 */
@Slf4j
@RestController
@RequestMapping("/mall/user")
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IChargeService chargeService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private MoneyHistoryMapper moneyHistoryMapper;
    @Autowired
    private ProductTradeMapper productTradeMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询用户团队列表
     */
    @Log(title = "查询用户团队列表", businessType = BusinessType.GET)
    @GetMapping(value = "/getTeamList/{certificationId}")
    public AjaxResult getTeamList(@PathVariable("certificationId") Long certificationId) {
        if (ObjectUtil.isNull(certificationId) || certificationId.longValue() <= 0) {
            return AjaxResult.error("参数错误");
        }
        return userService.getTeamList(certificationId);
    }

    /**
     * 获取商城用户详细信息
     */
    @Log(title = "获取用户详细信息", businessType = BusinessType.UPDATE)
    @GetMapping(value = "/getUserInfo/{certificationId}")
    public AjaxResult getUserInfo(@PathVariable("certificationId") Long certificationId) {
        if (ObjectUtil.isNull(certificationId) || certificationId.longValue() <= 0) {
            return AjaxResult.error("参数错误");
        }
        return userService.getUserInfo(certificationId);
    }

    /**
     * 编辑昵称
     */
    @Log(title = "用户修改昵称", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateNickName")
    public AjaxResult updateNickName(@Validated @RequestBody CertificationAttach param) {
        if (ObjectUtil.isNull(param.getCertificationId()) || param.getCertificationId() <= 0 || StringUtils.isBlank(param.getNickName())) {
            return AjaxResult.error("参数错误");
        }
        return userService.updateNickName(param.getCertificationId(), param.getNickName());
    }
    @Log(title = "用户用户联系方式", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateMobile")
    public AjaxResult updateMobile(@Validated @RequestBody Certification param) {
        if (ObjectUtil.isNull(param.getId()) || param.getId() <= 0 || StringUtils.isBlank(param.getMobile())) {
            return AjaxResult.error("参数错误");
        }
        return userService.updateMobile(param.getId(), param.getMobile());
    }

    /**
     * 编辑简介
     */
    @Log(title = "用户修改简介", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateIntroduction")
    public AjaxResult updateIntroduction(@Validated @RequestBody UserSummaryParam param) {
        return userService.updateIntroduction(param.getCertificationId(), param.getDesc());
    }

    /**
     * 更新用户实名状态
     */
    @Log(title = "更新用户实名状态", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateUserAuthStatus")
    public AjaxResult updateUserAuthStatus(@Validated @RequestBody UserAuthParam userAuthParam) {
        if (ObjectUtil.isNull(userAuthParam)) {
            return AjaxResult.error("参数错误");
        }
        return userService.updateUserAuthStatus(userAuthParam);
    }

    /**
     * 用户充值
     */
    @Log(title = "用户充值", businessType = BusinessType.INSERT)
    @PostMapping("/addCharge")
    public AjaxResult addCharge(@Validated @RequestBody ChargeParam chargeParam) {
        if (ObjectUtil.isNull(chargeParam)) {
            return AjaxResult.error("参数错误");
        }
        return userService.addCharge(chargeParam);
    }

    /**
     * 用户充值列表
     */
    @Log(title = "获取用户充值列表", businessType = BusinessType.GET)
    @GetMapping(value = "/getChargeList/{certificationId}")
    public AjaxResult getChargeList(@PathVariable("certificationId") Long certificationId) {
        if (ObjectUtil.isNull(certificationId) || certificationId.longValue() <= 0) {
            return AjaxResult.error("参数错误");
        }
        return userService.getChargeList(certificationId, ChargeStatus.SUCCESS.getCode());
    }

    /**
     * 查询充值记录
     */
    @Log(title = "获取用户充值详情", businessType = BusinessType.GET)
    @GetMapping(value = "/getChargeByOrderId/{orderId}")
    public AjaxResult getChargeByOrderId(@PathVariable("orderId") String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return AjaxResult.error("参数错误");
        }
        Charge charge = chargeService.selectChargeById(orderId);
        return ObjectUtil.isNull(charge) ? AjaxResult.error("查询失败") : AjaxResult.success("查询成功", charge);
    }

    /**
     * 查询充值记录和下单信息
     */
    @Log(title = "获取用户充值和下单详情", businessType = BusinessType.GET)
    @GetMapping(value = "/getChargeAndOrderByOrderId/{orderId}")
    public AjaxResult getChargeAndOrderByOrderId(@PathVariable("orderId") String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return AjaxResult.error("参数错误");
        }
        Charge charge = chargeService.selectChargeById(orderId);
        Object object = redisCache.getCacheObject(orderId.trim());
        CollectionBuyParam param = ObjectUtil.isNull(object) ? null : (CollectionBuyParam) object;
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("charge", charge);
        resultMap.put("param", param);
        return ObjectUtil.isNull(charge) ? AjaxResult.error("查询失败") : AjaxResult.success("查询成功", resultMap);
    }

    /**
     * 用户申请成为藏品铸造者
     */
    @Log(title = "用户申请成为藏品发铸造者", businessType = BusinessType.INSERT)
    @PostMapping("/addReleaseApply")
    public AjaxResult addReleaseApply(Long certificationId) {
        if (ObjectUtil.isNull(certificationId) || certificationId <= 0) {
            return AjaxResult.error("参数错误");
        }
        return userService.addReleaseApply(certificationId);
    }

    /**
     * 用户设置提现方式
     */
    @Log(title = "用户设置提现方式", businessType = BusinessType.INSERT)
    @PostMapping("/addWithdrawConfig")
    public AjaxResult addWithdrawConfig(@Validated @RequestBody WithdrawConfigParam withdrawConfigParam) {
        if (ObjectUtil.isNull(withdrawConfigParam)) {
            return AjaxResult.error("参数错误");
        }
        // 账号
        String mobile = withdrawConfigParam.getMobile();
        // 校验账号格式
        if (!ValidatorUtils.checkAccount(mobile)) {
            return AjaxResult.error("请输入正确的手机号或者邮箱.");
        }
        // 验证码
        String msgCode = withdrawConfigParam.getMsgCode();
        Object object = redisCache.getCacheObject("code_" + mobile);
        if (ObjectUtil.isNull(object)) {
            return AjaxResult.error("The verification code has expired, please resend.");
        }
        String existCode = (String) object;
        if (StringUtils.isBlank(existCode)) {
            return AjaxResult.error("The verification code has expired, please resend.");
        }
        if (!existCode.equals(msgCode)) {
            return AjaxResult.error("Verification code error.");
        }
        return userService.addWithdrawConfig(withdrawConfigParam);
    }

    /**
     * 获取用户提现配置信息
     */
    @Log(title = "获取用户提现配置信息", businessType = BusinessType.GET)
    @GetMapping(value = "/getWithdrawConfig/{certificationId}/{type}")
    public AjaxResult getWithdrawConfig(@PathVariable("certificationId") Long certificationId, @PathVariable("type") Integer type) {
        if (ObjectUtil.isNull(certificationId) || certificationId.longValue() <= 0 || ObjectUtil.isNull(type)) {
            return AjaxResult.error("参数错误");
        }
        return userService.getWithdrawConfig(certificationId, type);
    }

    /**
     * 用户申请提现
     */
    @Log(title = "用户申请提现", businessType = BusinessType.INSERT)
    @PostMapping("/addWithdraw")
    public AjaxResult addWithdraw(@Validated @RequestBody WithdrawParam withdrawParam) {
        if (ObjectUtil.isNull(withdrawParam)) {
            return AjaxResult.error("参数错误");
        }
        return userService.addBill(withdrawParam);
    }

    /**
     * 获取用户提现列表
     */
    @Log(title = "获取用户提现列表", businessType = BusinessType.GET)
    @GetMapping(value = "/getWithdrawList/{certificationId}")
    public AjaxResult getWithdrawList(@PathVariable("certificationId") Long certificationId) {
        if (ObjectUtil.isNull(certificationId) || certificationId.longValue() <= 0) {
            return AjaxResult.error("参数错误");
        }
        return userService.getWithdrawList(certificationId);
    }

    /**
     * 获取用户原石数量
     */
    @Log(title = "获取用户原石数量", businessType = BusinessType.GET)
    @GetMapping(value = "/getTokenBalance/{walletAddress}/{contractAddress}")
    public AjaxResult getTokenBalance(@PathVariable("walletAddress") String walletAddress, @PathVariable("contractAddress") String contractAddress) {
        if (StringUtils.isBlank(walletAddress) || StringUtils.isBlank(contractAddress)) {
            return AjaxResult.error("参数错误");
        }
        String tokenBalance = Web3jUtils.getTokenBalance(walletAddress, contractAddress);
        return AjaxResult.success("查询成功", tokenBalance);
    }

    /**
     * 用户上传个人头像
     */
    @Log(title = "用户上传头像", businessType = BusinessType.UPDATE)
    @PostMapping("/updateAvatar")
    public AjaxResult updateAvatar(@RequestParam("avatarFile") MultipartFile file, @RequestParam("certificationId") Long certificationId) throws IOException {
        if (!file.isEmpty()) {
            String avatar = FileUploadUtils.upload(NftConfig.getAvatarPath(), file);
            System.out.println(avatar);
            return userService.updateUserAvatar(avatar, certificationId);
        }
        return AjaxResult.error("上传图片异常，请稍后重试");
    }

    /**
     * 用户删除个人头像
     */
    @Log(title = "用户删除头像", businessType = BusinessType.UPDATE)
    @PostMapping("/delAvatar")
    public AjaxResult delAvatar(@RequestParam("certificationId") Long certificationId) {
        return userService.updateUserAvatar("", certificationId);
    }

    /**
     * 个人/机构实名认证申请
     */
    @Log(title = "个人/机构实名认证申请", businessType = BusinessType.INSERT)
    @PostMapping("/addRealNameApply")
    public AjaxResult addRealNameApply(@Validated @RequestBody UserRealNameParam realNameParam) {
        if (ObjectUtil.isNull(realNameParam)) {
            return AjaxResult.error("参数错误");
        }
        return userService.addRealNameApply(realNameParam);
    }

    /**
     * 查询用户手续费余额
     */
    @Log(title = "查询用户手续费余额", businessType = BusinessType.GET)
    @GetMapping(value = "/checkAddressFee/{address}/{fee}")
    public AjaxResult checkAddressFee(@PathVariable("address") String address, @PathVariable("fee") BigDecimal fee) {
        if (StringUtils.isBlank(address) || fee.compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.error("参数错误");
        }
        Json json = Web3jUtils.supplyTradeFee(fee, address, 5);
        return json.isFlag() ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }

    /**
     * 获取用户的交易记录
     */
    @Log(title = "获取用户的交易记录", businessType = BusinessType.GET)
    @GetMapping("/getMoneyHistory")
    public AjaxResult getMoneyHistory(@RequestParam("certificationId") Long certificationId) {
        if (ObjectUtil.isNull(certificationId) || certificationId.longValue() <= 0) {
            return AjaxResult.error("参数错误");
        }
        MoneyHistory moneyHistory = new MoneyHistory();
        moneyHistory.setCertificationId(certificationId);
        moneyHistory.setType(MoneyHistoryType.BUY_PAY.getCode());
        moneyHistory.setStatus(MoneyHistoryStatus.ING.getCode());
        List<MoneyHistory> historyList = moneyHistoryMapper.selectMyHistoryOfUseful(moneyHistory);
        historyList.stream().forEach(history -> {
            MoneyHistoryType historyType = MoneyHistoryType.getByCode(history.getType());
            //交易备注
            history.setOrderRemark(historyType.getMessage());
            MoneyHistoryStatus historyStatus = MoneyHistoryStatus.getByCode(history.getStatus());
            //状态描述
            history.setStatusDesc(historyStatus.getMessage());
            //交易类型描述
            history.setTradeTypeDesc(MoneyHistoryType.getTradeType(history.getType()));
        });
        return AjaxResult.success("查询成功", historyList);
    }

    /**
     * 获取用户的待支付订单
     */
    @Log(title = "获取用户的待支付订单", businessType = BusinessType.GET)
    @GetMapping("/getMyToPay")
    public AjaxResult getMyToPay(@RequestParam("certificationId") Long certificationId) {
        if (ObjectUtil.isNull(certificationId) || certificationId.longValue() <= 0) {
            return AjaxResult.error("参数错误");
        }
        ProductTrade productTrade = new ProductTrade();
        productTrade.setCreateId(certificationId);
        productTrade.setStatus(CollectTradeStatus.WAIT_PAY.getCode());
        List<ProductTrade> productTrades = productTradeMapper.selectProductTradeList(productTrade);
        if (productTrades != null) {
            for (ProductTrade trade : productTrades) {
                //设置待支付的链接
                trade.setToPayUrl(redisCache.getCacheObject(RedisKeyExpirationListener.EX_PAY + trade.getProductTradeId()));

                MoneyHistory historySearch = new MoneyHistory();
                historySearch.setTradeId(trade.getProductTradeId());
                historySearch.setCertificationId(trade.getCreateId());
                historySearch.setType(MoneyHistoryType.BUY_PAY.getCode());
                historySearch.setStatus(MoneyHistoryStatus.ING.getCode());
                List<MoneyHistory> moneyHistories = moneyHistoryMapper.selectChargeList(historySearch);
                if (moneyHistories != null && moneyHistories.size() != 0) {
                    //设置待支付的截止日期
                    trade.setPayExpireTime(moneyHistories.get(0).getPayExpireTime());
                    String format = DateUtil.format(moneyHistories.get(0).getPayExpireTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
                    trade.setRemainTime(format);
                    //交易编号
                    trade.setOrderNo(moneyHistories.get(0).getId());
                    MoneyHistoryType historyType = MoneyHistoryType.getByCode(moneyHistories.get(0).getType());
                    //交易备注
                    trade.setOrderRemark(historyType.getMessage());
                }

                //设置藏品名称
                Product product = productMapper.selectProductById(trade.getProductId());
                trade.setProductName(product.getProductName());
            }
        }
        return AjaxResult.success("查询成功", productTrades);
    }

    /**
     * 用户设置银行卡
     */
    @Log(title = "用户设置银行卡", businessType = BusinessType.INSERT)
    @PostMapping("/addCertificationBank")
    public AjaxResult addCertificationBank(@Validated @RequestBody CertificationBankParam bankParam) {
        if (ObjectUtil.isNull(bankParam)) {
            return AjaxResult.error("参数错误");
        }
        // 账号
//        String email = bankParam.getEmail();
//        // 校验账号格式
//        if (!ValidatorUtils.checkAccount(email)) {
//            return AjaxResult.error("请输入正确的邮箱.");
//        }
//        Object object = redisCache.getCacheObject("code_" + email);
//        if (ObjectUtil.isNull(object)) {
//            return AjaxResult.error("The verification code has expired, please resend.");
//        }
//        String existCode = (String) object;
//        if (StringUtils.isBlank(existCode)) {
//            return AjaxResult.error("The verification code has expired, please resend.");
//        }
//        // 验证码
//        String msgCode = bankParam.getMsgCode();
//        if (!existCode.equals(msgCode)) {
//            return AjaxResult.error("Verification code error.");
//        }
        return userService.addWCertificationBank(bankParam);
    }

    /**
     * 用户更新银行卡
     */
    @Log(title = "用户更新银行卡", businessType = BusinessType.INSERT)
    @PostMapping("/updateCertificationBank")
    public AjaxResult updateCertificationBank(@Validated @RequestBody CertificationBankParam bankParam) {
        if (ObjectUtil.isNull(bankParam)) {
            return AjaxResult.error("参数错误");
        }
        // 账号
//        String email = bankParam.getEmail();
//        // 校验账号格式
//        if (!ValidatorUtils.checkAccount(email)) {
//            return AjaxResult.error("请输入正确的邮箱.");
//        }
//        Object object = redisCache.getCacheObject("code_" + email);
//        if (ObjectUtil.isNull(object)) {
//            return AjaxResult.error("The verification code has expired, please resend.");
//        }
//        String existCode = (String) object;
//        if (StringUtils.isBlank(existCode)) {
//            return AjaxResult.error("The verification code has expired, please resend.");
//        }
//        // 验证码
//        String msgCode = bankParam.getMsgCode();
//        if (!existCode.equals(msgCode)) {
//            return AjaxResult.error("Verification code error.");
//        }
        return userService.updateWCertificationBank(bankParam);
    }

    /**
     * 获取用户银行卡信息
     */
    @Log(title = "获取用户提现配置信息", businessType = BusinessType.GET)
    @GetMapping(value = "/getCertificationBank/{certificationId}")
    public AjaxResult getCertificationBank(@PathVariable("certificationId") Long certificationId) {
        if (ObjectUtil.isNull(certificationId) || certificationId.longValue() <= 0) {
            return AjaxResult.error("参数错误");
        }
        return userService.getCertificationBank(certificationId);
    }
}
