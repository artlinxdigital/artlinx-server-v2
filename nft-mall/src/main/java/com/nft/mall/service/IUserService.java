package com.nft.mall.service;

import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.user.*;
import com.nft.mall.domain.Certification;

import java.util.Map;

/**
 * 用户Service接口
 *
 * @author nft
 * @date 2021-05-12
 */
public interface IUserService {

    /**
     * 查询注册账号已经存在
     *
     * @param account 账号
     * @param type    类型
     * @return 结果
     */
    AjaxResult checkAccountIsExist(String account, int type);

    /**
     * 查询地址已经存在
     *
     * @param address 账号
     * @return 结果
     */
    AjaxResult checkAddressIsExist(String address);

    /**
     * 注册账号
     *
     * @param registerUserParam 账号信息
     * @return 结果
     */
    AjaxResult register(RegisterUserParam registerUserParam);

    /**
     * 登录(密码)
     *
     * @param account 账号信息
     * @return 结果
     */
    AjaxResult loginByPwd(String account);

    /**
     * 登录(地址)
     *
     * @param walletAddress 地址信息
     * @return 结果
     */
    AjaxResult loginByWalletAddress(String walletAddress);

    /**
     * 通过私钥重置托管密码
     *
     * @param resetPwdParam 密码信息
     * @return 结果
     */
    AjaxResult resetPwdByPrivateKey(ResetPwdParam resetPwdParam);

    /**
     * 通过地址查询用户信息
     *
     * @param walletAddress 地址
     * @return 结果
     */
    Certification getByWalletAddress(String walletAddress);

    /**
     * 根据account和type获得详细信息
     *
     * @param account 用户账号
     * @param type            类型
     * @return 结果
     */
    AjaxResult getUserByAccountAndType(String account, Integer type);

    /**
     * 根据id和type获得详细信息
     *
     * @param certificationId 用户id
     * @param type            类型
     * @return 结果
     */
    AjaxResult getMsgByIds(Long certificationId, Integer type);

    /**
     * 根据地址删除实名信息
     *
     * @param walletAddress
     * @return 结果
     */
    AjaxResult deleteByWalletAddress(String walletAddress);

    /**
     * 根据地址 名称 证件号 验证是否实名
     *
     * @param map
     * @return 结果
     */
    Certification getCertificationByMap(Map<String, String> map);

    /**
     * 查询用户团队列表
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    AjaxResult getTeamList(Long certificationId);

    /**
     * 获取用户详细信息
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    AjaxResult getUserInfo(Long certificationId);

    /**
     * 用户修改昵称
     *
     * @param certificationId 用户ID
     * @param nickName 昵称
     * @return 结果
     */
    AjaxResult updateNickName(Long certificationId, String nickName);
    AjaxResult updateMobile(Long certificationId, String mobile);

    /**
     * 用户修改简介
     *
     * @param certificationId 用户ID
     * @param introduction 简介
     * @return 结果
     */
    AjaxResult updateIntroduction(Long certificationId, String introduction);

    /**
     * 用户修改实名状态
     *
     * @param userAuthParam 用户实名信息
     * @return 结果
     */
    AjaxResult updateUserAuthStatus(UserAuthParam userAuthParam);

    /**
     * 用户充值
     *
     * @param chargeParam 充值信息
     * @return 结果
     */
    AjaxResult addCharge(ChargeParam chargeParam);

    /**
     * 获取用户充值列表
     *
     * @param certificationId 用户ID
     * @param status 充值状态
     * @return 结果
     */
    AjaxResult getChargeList(Long certificationId, Integer status);

    /**
     * 用户申请成为藏品发布者
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    AjaxResult addReleaseApply(Long certificationId);

    /**
     * 用户设置提现方式
     *
     * @param withdrawConfigParam 提现信息
     * @return 结果
     */
    AjaxResult addWithdrawConfig(WithdrawConfigParam withdrawConfigParam);

    /**
     * 获取提现信息
     *
     * @param certificationId 用户ID
     * @param type 方式
     * @return 结果
     */
    AjaxResult getWithdrawConfig(Long certificationId, Integer type);

    /**
     * 用户提现申请
     *
     * @param withdrawParam 提现申请信息
     * @return 结果
     */
    AjaxResult addWithdraw(WithdrawParam withdrawParam);

    /**
     * 用户提现申请
     *
     * @param withdrawParam 提现申请信息
     * @return 结果
     */
    AjaxResult addBill(WithdrawParam withdrawParam);

    /**
     * 获取用户提现列表
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    AjaxResult getWithdrawList(Long certificationId);

    /**
     * 修改用户头像
     *
     * @param avatar 头像地址
     * @param certificationId 用户ID
     * @return 结果
     */
    AjaxResult updateUserAvatar(String avatar, Long certificationId);

    /**
     * 个人/机构实名认证申请
     *
     * @param userRealNameParam 申请信息
     * @return 结果
     */
    AjaxResult addRealNameApply(UserRealNameParam userRealNameParam);

    /**
     * 用户设置银行卡
     *
     * @param bankParam 银行信息
     * @return 结果
     */
    AjaxResult addWCertificationBank(CertificationBankParam bankParam);

    /**
     * 用户更新银行卡
     *
     * @param bankParam 银行信息
     * @return 结果
     */
    AjaxResult updateWCertificationBank(CertificationBankParam bankParam);

    /**
     * 获取银行卡信息
     *
     * @param certificationId 用户ID
     * @return 结果
     */
    AjaxResult getCertificationBank(Long certificationId);

}
