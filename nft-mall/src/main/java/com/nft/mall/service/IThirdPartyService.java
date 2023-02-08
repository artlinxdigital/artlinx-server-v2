package com.nft.mall.service;

import com.alibaba.fastjson.JSONObject;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.collect.CollectionBuyParam;
import com.nft.mall.domain.Charge;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 第三方Service接口
 *
 * @author nft
 * @date 2021-05-12
 */
public interface IThirdPartyService {

    /**
     * 阿里云发送短信
     *
     * @param account 账号
     * @param msgCode 验证码
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 结果
     */
    AjaxResult sendSmsByAliYun(final String account, final String msgCode, final Integer timeout, final TimeUnit timeUnit);

    /**
     * 腾讯云发送短信
     *
     * @param account 账号
     * @param msgCode 验证码
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 结果
     */
    AjaxResult sendSmsByTxYun(final String account, final String msgCode, final Integer timeout, final TimeUnit timeUnit);

    /**
     * 发送邮件(注册)
     *
     * @param account 账号
     * @param msgCode 验证码
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 结果
     */
    AjaxResult sendHtmlMail(final String account, final String msgCode, final Integer timeout, final TimeUnit timeUnit);

    /**
     * 发送邮件(注册成功)
     *
     * @param account 账号
     * @return 结果
     */
    AjaxResult sendHtmlMailForRegister(final String account);

    /**
     * 发送邮件(实名审核)
     *
     * @param account 账号
     * @return 结果
     */
    AjaxResult sendHtmlMailForAudit(final String account);

    /**
     * 发送邮件(实名成功)
     *
     * @param account 账号
     * @return 结果
     */
    AjaxResult sendHtmlMailForRealName(final String account);

    /**
     * 发送邮件(铸造上架)
     *
     * @param account 账号
     * @return 结果
     */
    AjaxResult sendHtmlMailForMoldUp(final String account);

    /**
     * 发送邮件给买家(Successful Transaction)
     *
     * @param account 账号
     * @param fileUrl 藏品原文件下载地址
     * @return 结果
     */
    AjaxResult sendHtmlMailForTradeBuyer(final String account, final String fileUrl);

    /**
     * 发送邮件给卖家(Successful Transaction)
     *
     * @param account 账号
     * @param productName 藏品名称
     * @return 结果
     */
    AjaxResult sendHtmlMailForTradeSeller(final String account, final String productName);

    /**
     * 微信h5下单接口
     *
     * @param address 地址
     * @param money 金额
     * @param ipAddr ip
     * @param subject 主题
     * @param param 下单参数
     * @return 结果
     */
    AjaxResult wxH5(final String address, final String money, final String ipAddr, final String subject, final CollectionBuyParam param);

    /**
     * 微信PC下单接口
     *
     * @param address 地址
     * @param money 金额
     * @param ipAddr ip
     * @param subject 主题
     * @param param 下单参数
     * @return 结果
     */
    AjaxResult wxNative(final String address, final String money, final String ipAddr, final String subject, final CollectionBuyParam param);

    /**
     * 微信jsapi下单接口
     *
     * @param address 地址
     * @param money 金额
     * @param ipAddr ip
     * @param subject 主题
     * @param code
     * @param request
     * @param response
     * @return 结果
     */
    AjaxResult wxJsapi(final String address, final String money, final String ipAddr, final String subject, final String code,
                     final HttpServletRequest request, final HttpServletResponse response);

    /**
     * 微信App下单接口
     *
     * @param address 地址
     * @param money 金额
     * @param ipAddr ip
     * @param subject 主题
     * @param request
     * @param response
     * @return 结果
     */
    AjaxResult wxApp(final String address, final String money, final String ipAddr, final String subject, final HttpServletRequest request, final HttpServletResponse response);

    /**
     * 微信退款
     *
     * @param charge 充值信息
     * @return 结果
     */
    boolean wxPayRefund(Charge charge);

    /**
     * 支付宝手机端下单接口
     *
     * @param returnUrl 返回地址
     * @param orderId 订单ID
     * @param money 金额
     * @return 结果
     */
    AjaxResult aliMobilePay(final String returnUrl, String orderId, String money);

    /**
     * 支付宝二维码下单接口
     *
     * @param returnUrl 返回地址
     * @param orderId 订单ID
     * @param money 金额
     * @return 结果
     */
    AjaxResult aliQrCodePay(final String returnUrl, String orderId, String money);

    /**
     * 支付宝PC端下单接口
     *
     * @param returnUrl 返回地址
     * @param orderId 订单ID
     * @param money 金额
     * @return 结果
     */
    AjaxResult aliPcPay(final String returnUrl, String orderId, String money);

    /**
     * 支付宝APP端下单接口
     *
     * @param returnUrl 返回地址
     * @param orderId 订单ID
     * @param money 金额
     * @return 结果
     */
    AjaxResult aliAppPay(final String returnUrl, String orderId, String money);

    /**
     * 获取授权信息
     *
     * @param isWeiXin 是否是微信
     * @param type 类型
     * @param address 地址
     * @return 结果
     */
    JSONObject getDetectAuth(String isWeiXin, String type, String address);

    /**
     * 获得人脸识别的视频
     *
     * @param nonce 随机数
     * @param orderNo 订单号
     * @param version 版本号
     * @param getFile 地址
     * @return 结果
     */
    String getVideo(String nonce, String orderNo, String version, Integer getFile) throws Exception;

    /**
     * 获取签名信息
     *
     * @param values
     * @param ticket
     * @return 结果
     */
    String sign(List<String> values, String ticket);

    /**
     * 获取访问的token
     *
     * @return 结果
     */
    String getAccessToken();

    /**
     * 获取人脸识别的id
     *
     * @param userId 用户id
     * @param version 版本号
     * @param nonce 随机数
     * @param orderId 订单id
     * @param name 名称
     * @param idNo id号码
     * @param sign 签名信息
     * @return 结果
     */
    String getFaceId(String userId, String version, String nonce, String orderId, String name, String idNo, String sign);

    /**
     * 根据订单号查询腾讯云，判断是否通过人脸识别
     *
     * @param nonce 随机数
     * @param orderNo 订单号
     * @param version 版本号
     * @return 结果
     */
    String getMsgByOrderNo(String nonce, String orderNo, String version);
}
