package com.nft.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.google.common.hash.Hashing;
import com.nft.common.ali.pay.AliConfig;
import com.nft.common.ali.pay.ZxingUtils;
import com.nft.common.config.TxConfig;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.EmailInfo;
import com.nft.common.core.domain.pojo.collect.CollectionBuyParam;
import com.nft.common.core.redis.RedisCache;
import com.nft.common.enums.BusinessFrom;
import com.nft.common.enums.ChargeStatus;
import com.nft.common.enums.PayType;
import com.nft.common.tencent.MD5Utils;
import com.nft.common.tencent.StringUtils;
import com.nft.common.tencent.TimeUtils;
import com.nft.common.tencent.WebUtils;
import com.nft.common.tencent.pay.RequestHandler;
import com.nft.common.tencent.pay.Sha1Util;
import com.nft.common.tencent.pay.WeixinPayUtil;
import com.nft.common.utils.DateUtils;
import com.nft.common.utils.HttpClient;
import com.nft.common.utils.PathUtil;
import com.nft.common.utils.sms.EmailUtils;
import com.nft.common.utils.sms.SmsUtils;
import com.nft.common.utils.uuid.IdUtils;
import com.nft.mall.domain.Certification;
import com.nft.mall.domain.Charge;
import com.nft.mall.mapper.CertificationMapper;
import com.nft.mall.mapper.ChargeMapper;
import com.nft.mall.service.IBuildModelService;
import com.nft.mall.service.ICollectionService;
import com.nft.mall.service.IThirdPartyService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.faceid.v20180301.FaceidClient;
import com.tencentcloudapi.faceid.v20180301.models.DetectAuthRequest;
import com.tencentcloudapi.faceid.v20180301.models.DetectAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.SecapiPayRefund;
import weixin.popular.bean.paymch.SecapiPayRefundResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 第三方Service业务层处理
 *
 * @author nft
 * @date 2021-05-12
 */
@Slf4j
@Service
public class ThirdPartyServiceImpl implements IThirdPartyService {

    @Autowired
    private CertificationMapper certificationMapper;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IBuildModelService buildModelService;

    @Autowired
    private ICollectionService collectionService;

    /**
     * 阿里云发送短信
     *
     * @param account  账号
     * @param msgCode  验证码
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     * @return 结果
     */
    @Override
    public AjaxResult sendSmsByAliYun(String account, String msgCode, Integer timeout, TimeUnit timeUnit) {
        log.info("手机号：" + account);
        boolean smsByAliYun = SmsUtils.sendSmsByAliYun(account, msgCode);
        if (smsByAliYun) {
            // 将验证码信息放入redis(有效期为5分钟)
            redisCache.setCacheObject("code_" + account.trim(), msgCode, 5, timeUnit);
            // 将当前时间放入redis
            redisCache.setCacheObject("time_" + account.trim(), DateUtils.getNowDate().getTime());
            log.info("手机号：【" + account + "】发送短信结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 腾讯云发送短信
     *
     * @param account 账号
     * @param msgCode 验证码
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 结果
     */
    @Override
    public AjaxResult sendSmsByTxYun(String account, String msgCode, Integer timeout, TimeUnit timeUnit) {
        log.info("手机号：" + account);
        boolean smsByTxYun = SmsUtils.sendSmsByTxYun(account, msgCode);
        if (smsByTxYun) {
            // 将验证码信息放入redis(有效期为5分钟)
            redisCache.setCacheObject("code_" + account.trim(), msgCode, 5, timeUnit);
            // 将当前时间放入redis
            redisCache.setCacheObject("time_" + account.trim(), DateUtils.getNowDate().getTime());
            log.info("手机号：【" + account + "】发送短信结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 发送邮件(获取验证码)
     *
     * @param account  账号
     * @param msgCode  验证码
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     * @return 结果
     */
    @Override
    public AjaxResult sendHtmlMail(String account, String msgCode, Integer timeout, TimeUnit timeUnit) {
        log.info("邮箱：" + account);
//        String content = "Hello, your verification code is：" + msgCode + ", valid within 5 minutes, please do not tell others the verification code!";
        EmailInfo info = new EmailInfo(account, EmailUtils.SUBJECT_GET_CODE, EmailUtils.contentForGetCode(msgCode));
        boolean sendHtmlMail = EmailUtils.sendHtmlMail(info);
        if (sendHtmlMail) {
            // 将验证码信息放入redis(有效期为5分钟)
            redisCache.setCacheObject("code_" + account.trim(), msgCode, 5, timeUnit);
            // 将当前时间放入redis
            redisCache.setCacheObject("time_" + account.trim(), DateUtils.getNowDate().getTime());
            log.info("获取验证码, 邮箱：【" + account + "】发送邮件结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 发送邮件(注册成功)
     *
     * @param account 账号
     * @return 结果
     */
    @Override
    public AjaxResult sendHtmlMailForRegister(String account) {
        log.info("邮箱：" + account);
        EmailInfo info = new EmailInfo(account, EmailUtils.SUBJECT_REGISTER, EmailUtils.contentForRegister());
        boolean sendHtmlMail = EmailUtils.sendHtmlMail(info);
        if (sendHtmlMail) {
            log.info("注册成功, 邮箱：【" + account + "】发送邮件结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 发送邮件(实名审核)
     *
     * @param account 账号
     * @return 结果
     */
    @Override
    public AjaxResult sendHtmlMailForAudit(String account) {
        log.info("邮箱：" + account);
        EmailInfo info = new EmailInfo(account, "有新的kyc等待审核", "有新的kyc等待审核");
        boolean sendHtmlMail = EmailUtils.sendHtmlMail(info);
        if (sendHtmlMail) {
            log.info("实名审核, 邮箱：【" + account + "】发送邮件结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 发送邮件(实名)
     *
     * @param account 账号
     * @return 结果
     */
    @Override
    public AjaxResult sendHtmlMailForRealName(String account) {
        log.info("邮箱：" + account);
//        String content = "Hello, your real name has been successful!";
        EmailInfo info = new EmailInfo(account, EmailUtils.SUBJECT_KYC, EmailUtils.contentForKyc());
        boolean sendHtmlMail = EmailUtils.sendHtmlMail(info);
        if (sendHtmlMail) {
            log.info("实名成功, 邮箱：【" + account + "】发送邮件结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 发送邮件(铸造上架)
     *
     * @param account 账号
     * @return 结果
     */
    @Override
    public AjaxResult sendHtmlMailForMoldUp(String account) {
        log.info("邮箱：" + account);
        EmailInfo info = new EmailInfo(account, EmailUtils.SUBJECT_MOLD_UP, EmailUtils.contentForMoldUp());
        boolean sendHtmlMail = EmailUtils.sendHtmlMail(info);
        if (sendHtmlMail) {
            log.info("作品上架, 邮箱：【" + account + "】发送邮件结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 发送邮件给买家(Successful Transaction)
     *
     * @param account 账号
     * @param fileUrl 藏品原文件下载地址
     * @return 结果
     */
    @Override
    public AjaxResult sendHtmlMailForTradeBuyer(String account, String fileUrl) {
        log.info("邮箱：" + account);
//        String content = "Hello, the download address of your collection file is：" + fileUrl + ", please do not tell others!";
        EmailInfo info = new EmailInfo(account, EmailUtils.SUBJECT_BUY, EmailUtils.contentForTradeBuyer(fileUrl));
        boolean sendHtmlMail = EmailUtils.sendHtmlMail(info);
        if (sendHtmlMail) {
            log.info("Successful Transaction, 给买家发送邮件, 邮箱：【" + account + "】发送邮件结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 发送邮件给卖家(Successful Transaction)
     *
     * @param account 账号
     * @param productName 藏品名称
     * @return 结果
     */
    @Override
    public AjaxResult sendHtmlMailForTradeSeller(String account, String productName) {
        log.info("邮箱：" + account);
//        String content = "Hello，Your artwork：" + productName + " has been sold。";
        EmailInfo info = new EmailInfo(account, EmailUtils.SUBJECT_SOLD, EmailUtils.contentForTradeSeller());
        boolean sendHtmlMail = EmailUtils.sendHtmlMail(info);
        if (sendHtmlMail) {
            log.info("Successful Transaction, 给卖家发送邮件, 邮箱：【" + account + "】发送邮件结束");
            return AjaxResult.success("Sent successfully.");
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * h5下单
     *
     * @param address          地址
     * @param money            金额
     * @param ipAddr ip
     * @param subject          主题
     * @return 结果
     */
    @Override
    public AjaxResult wxH5(String address, String money, String ipAddr, String subject, CollectionBuyParam param) {
        // 查询充值用户
        Certification certification = certificationMapper.selectByWalletAddress(address);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("充值地址不存在");
        }
        String APPID = TxConfig.appId;
        String MERID = TxConfig.merId;
        String SIGNKEY = TxConfig.signKey;
        log.info("spbill_create_ip=" + ipAddr);
        // 测试地址，也就是本地真是ip，用于本地测试用
        // String spbill_create_ip = "";
        // 我这里是网页入口，app入口参考文档的安卓和ios写法
        String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + TxConfig.payEndpoint + "\",\"wap_name\": \"信息认证\"}}";
        // H5支付标记
        String tradeType = "MWEB";

        // 虽然官方文档不是必须参数，但是不送有时候会验签失败
        String MD5 = "MD5";
        JSONObject result = new JSONObject();

        // 金额转化为分为单位 微信支付以分为单位
        String finalMoney = StringUtils.getMoney(money);
        String out_trade_no = IdUtils.getTradeNo();
        // 随机数
        String nonce_str = MD5Utils.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());

        // 签名数据
        StringBuilder sb = new StringBuilder();
        sb.append("appid=" + APPID);
        sb.append("&body=" + subject);
        sb.append("&mch_id=" + MERID);
        sb.append("&nonce_str=" + nonce_str);
        sb.append("&notify_url=" + TxConfig.callbackUrl);
        sb.append("&out_trade_no=" + out_trade_no);
        sb.append("&scene_info=" + scene_info);
        sb.append("&sign_type=" + "MD5");
        sb.append("&spbill_create_ip=" + ipAddr);
        sb.append("&total_fee=" + finalMoney);
        sb.append("&trade_type=" + tradeType);
        sb.append("&key=" + SIGNKEY);
        log.info("sb=" + sb);
        // 签名MD5加密
        String sign = MD5Utils.getMd5(sb.toString()).toUpperCase();
        log.info("sign=" + sign);
        // 封装xml报文
        String xml = "<xml>" + "<appid>" + APPID + "</appid>" + "<mch_id>" + MERID + "</mch_id>" + "<nonce_str>"
                + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body>" + subject + "</body>" + //
                "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<total_fee>" + finalMoney + "</total_fee>" + //
                "<trade_type>" + tradeType + "</trade_type>" + "<notify_url>" + TxConfig.callbackUrl + "</notify_url>"
                + "<sign_type>MD5</sign_type>" + "<scene_info>" + scene_info + "</scene_info>" + "<spbill_create_ip>"
                + ipAddr + "</spbill_create_ip>" + "</xml>";

        // 微信统一下单接口
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String mweb_url = "";
        Map map = new HashMap();
        try {
            // 预下单 获取接口地址
            map = WebUtils.getMWebUrl(createOrderURL, xml);
            log.info(JSONObject.toJSONString(map));
            String return_code = (String) map.get("return_code");
            String return_msg = (String) map.get("return_msg");
            if ("SUCCESS".equals(return_code) && "OK".equals(return_msg)) {
                // 调微信支付接口地址
                mweb_url = (String) map.get("mweb_url");
                log.info("mweb_url=" + mweb_url);
            } else {
                log.info("统一支付接口获取预支付订单出错");
                return AjaxResult.error("支付错误");
            }
        } catch (Exception e) {
            log.info("统一支付接口获取预支付订单出错");
            return AjaxResult.error("支付错误");
        }

        // 构建充值信息
        Charge charge = buildModelService.buildCharge(certification, new BigDecimal(money), PayType.WE_CHAT.getCode(), ChargeStatus.ING.getCode());
        charge.setId(out_trade_no);
        // 添加充值记录
        int effectNum = chargeMapper.insertCharge(charge);
        if (effectNum == 1) {
            if (param.getPayFrom().equals(BusinessFrom.ORDER.getCode())) {
                // 更新藏品状态
//                collectionService.updateCollectionStatus(param.getProductId(), CollectStatus.TRADING.getCode());
                // 下单信息放入redis(有效期为24小时)
                redisCache.setCacheObject(charge.getId().trim(), param, 24, TimeUnit.HOURS);
            }
            result.put("out_trade_no", out_trade_no);
            result.put("mweb_url", mweb_url);
            return AjaxResult.success("支付成功", result);
        }
        return AjaxResult.error("支付错误", out_trade_no);
    }

    /**
     * PC支付
     *
     * @param address          地址
     * @param money            金额
     * @param ipAddr ip
     * @param subject          主题
     * @return 结果
     */
    @Override
    public AjaxResult wxNative(String address, String money, String ipAddr, String subject, CollectionBuyParam param) {
        // 查询充值用户
        Certification certification = certificationMapper.selectByWalletAddress(address);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("充值地址不存在");
        }
        String APPID = TxConfig.appId;
        String MERID = TxConfig.merId;
        String SIGNKEY = TxConfig.signKey;
        log.info("spbill_create_ip=" + ipAddr);
        String tradeType = "NATIVE";

        // 虽然官方文档不是必须参数，但是不送有时候会验签失败
        String MD5 = "MD5";
        JSONObject result = new JSONObject();

        // 金额转化为分为单位 微信支付以分为单位
        String finalMoney = StringUtils.getMoney(money);
        String out_trade_no = IdUtils.getTradeNo();
        // 随机数
        String nonce_str = MD5Utils.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());

        // 签名数据
        StringBuilder sb = new StringBuilder();
        sb.append("appid=" + APPID);
        sb.append("&body=" + subject);
        sb.append("&mch_id=" + MERID);
        sb.append("&nonce_str=" + nonce_str);
        sb.append("&notify_url=" + TxConfig.callbackUrl);
        sb.append("&out_trade_no=" + out_trade_no);

        sb.append("&sign_type=" + "MD5");
        sb.append("&spbill_create_ip=" + ipAddr);
        sb.append("&total_fee=" + finalMoney);
        sb.append("&trade_type=" + tradeType);
        sb.append("&key=" + SIGNKEY);
        log.info("sb=" + sb);
        // 签名MD5加密
        String sign = MD5Utils.getMd5(sb.toString()).toUpperCase();
        log.info("sign=" + sign);
        log.info("签名数据:" + sign);
        // 封装xml报文
        String xml = "<xml>" + "<appid>" + APPID + "</appid>" + "<mch_id>" + MERID + "</mch_id>" + "<nonce_str>"
                + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body>" + subject + "</body>" + //
                "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<total_fee>" + finalMoney + "</total_fee>" + //
                "<trade_type>" + tradeType + "</trade_type>" + "<notify_url>" + TxConfig.callbackUrl + "</notify_url>"
                + "<sign_type>MD5</sign_type>" + "<spbill_create_ip>" + ipAddr + "</spbill_create_ip>"
                + "</xml>";

        // 微信统一下单接口
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        Map map = new HashMap();
        String codeUrl = "";
        try {
            // 预下单 获取接口地址
            map = WebUtils.getMWebUrl(createOrderURL, xml);
            String return_code = (String) map.get("return_code");
            String return_msg = (String) map.get("return_msg");
            if ("SUCCESS".equals(return_code) && "OK".equals(return_msg)) {
                // 调微信支付接口地址
                codeUrl = (String) map.get("code_url");
                log.info("codeUrl=" + codeUrl);
            } else {
                log.info("统一支付接口获取预支付订单出错");
                AjaxResult.error("支付错误");
            }
        } catch (Exception e) {
            log.info("统一支付接口获取预支付订单出错");
            result.put("msg", "支付错误");
            AjaxResult.error("支付错误");
        }

        // 构建充值信息
        Charge charge = buildModelService.buildCharge(certification, new BigDecimal(money), PayType.WE_CHAT.getCode(), ChargeStatus.ING.getCode());
        charge.setId(out_trade_no);

        // 添加充值记录
        int effectNum = chargeMapper.insertCharge(charge);
        if (effectNum == 1) {
            if (param.getPayFrom().equals(BusinessFrom.ORDER.getCode())) {
                // 更新藏品状态
//                collectionService.updateCollectionStatus(param.getProductId(), CollectStatus.TRADING.getCode());
                // 下单信息放入redis(有效期为24小时)
                redisCache.setCacheObject(charge.getId().trim(), param, 24, TimeUnit.HOURS);
            }
            result.put("out_trade_no", out_trade_no);
            result.put("codeUrl", codeUrl);
            return AjaxResult.success("支付成功", result);
        }
        return AjaxResult.error("支付错误", out_trade_no);
    }

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
    @Override
    public AjaxResult wxJsapi(String address, String money, String ipAddr, String subject, String code, HttpServletRequest request, HttpServletResponse response) {
        log.info("code:" + code);
        // 查询充值用户
        Certification certification = certificationMapper.selectByWalletAddress(address);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("充值地址不存在");
        }
        String APPID = TxConfig.appId;
        String MERID = TxConfig.merId;
        String SIGNKEY = TxConfig.signKey;
        log.info("spbill_create_ip=" + ipAddr);
        String tradeType = "JSAPI";

        // 虽然官方文档不是必须参数，但是不送有时候会验签失败
        String MD5 = "MD5";
        JSONObject result = new JSONObject();

        // 金额转化为分为单位 微信支付以分为单位
        String finalMoney = StringUtils.getMoney(money);
        String out_trade_no = IdUtils.getTradeNo();
        // 随机数
        String nonce_str = MD5Utils.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());

        // 获取openId
        String openId = "";
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + TxConfig.appId + "&secret="
                + TxConfig.appSecret + "&code=" + code + "&grant_type=authorization_code";
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(HttpClient.doGet(URL));
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("支付错误");
        }
        if (null != jsonObject) {
            log.info("jsonObject:" + jsonObject);
            openId = jsonObject.getString("openid");
            log.info("openId:" + openId);
        }

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", APPID);
        packageParams.put("mch_id", MERID);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", subject);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", finalMoney);
        packageParams.put("spbill_create_ip", ipAddr);
        packageParams.put("notify_url", TxConfig.callbackUrl);
        packageParams.put("trade_type", tradeType);
        packageParams.put("openid", openId);

        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(APPID, TxConfig.appSecret, SIGNKEY);

        String sign = reqHandler.createSign(packageParams);
        log.info("sign:" + sign);
        String xml = "<xml>" + "<appid>" + APPID + "</appid>" + "<mch_id>" + MERID + "</mch_id>" + "<nonce_str>"
                + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + subject + "]]></body>"
                + "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<total_fee>" + finalMoney + "" + "</total_fee>"
                + "<spbill_create_ip>" + ipAddr + "</spbill_create_ip>" + "<notify_url>"
                + TxConfig.callbackUrl + "</notify_url>" + "<trade_type>" + tradeType + "</trade_type>" + "<openid>"
                + openId + "</openid>" + "</xml>";
        log.info("xml：" + xml);

        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String prepay_id = "";
        try {
            prepay_id = WeixinPayUtil.getPayNo(createOrderURL, xml);
            log.info("prepay_id:" + prepay_id);
            if (prepay_id.equals("")) {
                request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
                response.sendRedirect("error.jsp");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        String timestamp = Sha1Util.getTimeStamp();
        String packages = "prepay_id=" + prepay_id;
        finalpackage.put("appId", APPID);
        finalpackage.put("timeStamp", timestamp);
        finalpackage.put("nonceStr", nonce_str);
        finalpackage.put("package", packages);
        finalpackage.put("signType", MD5);
        String finalsign = reqHandler.createSign(finalpackage);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appid", APPID);
        map.put("timeStamp", timestamp);
        map.put("nonceStr", nonce_str);
        map.put("packageValue", packages);
        map.put("sign", finalsign);

        map.put("bizOrderId", out_trade_no);
        map.put("orderId", out_trade_no);
        map.put("payPrice", finalMoney);

        // 构建充值信息
        Charge charge = buildModelService.buildCharge(certification, new BigDecimal(money), PayType.WE_CHAT.getCode(), ChargeStatus.ING.getCode());
        charge.setId(out_trade_no);
        // 添加充值记录
        int effectNum = chargeMapper.insertCharge(charge);
        if (effectNum == 1) {
            map.put("out_trade_no", out_trade_no);
            return AjaxResult.success("支付成功", map);
        }
        return AjaxResult.error("支付错误", out_trade_no);
    }

    /**
     * App下单接口
     *
     * @param address 地址
     * @param money 金额
     * @param ipAddr ip
     * @param subject 主题
     * @param request
     * @param response
     * @return 结果
     */
    @Override
    public AjaxResult wxApp(String address, String money, String ipAddr, String subject, HttpServletRequest request, HttpServletResponse response) {
        // 查询充值用户
        Certification certification = certificationMapper.selectByWalletAddress(address);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("充值地址不存在");
        }
        String APPID = TxConfig.openAppId;
        String MERID = TxConfig.merId;
        String SIGNKEY = TxConfig.signKey;
        log.info("spbill_create_ip=" + ipAddr);
        String tradeType = "APP";

        // 金额转化为分为单位 微信支付以分为单位
        String finalMoney = StringUtils.getMoney(money);
        String out_trade_no = IdUtils.getTradeNo();
        // 随机数
        String nonce_str = MD5Utils.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", APPID);
        packageParams.put("mch_id", MERID);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", subject);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", finalMoney);
        packageParams.put("spbill_create_ip", ipAddr);
        packageParams.put("notify_url", TxConfig.callbackUrl);
        packageParams.put("trade_type", tradeType);

        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(APPID, TxConfig.appSecret, SIGNKEY);

        String sign = reqHandler.createSign(packageParams);
        log.info("sign:" + sign);
        String xml = "<xml>" + "<appid>" + APPID + "</appid>" + "<mch_id>" + MERID + "</mch_id>" + "<nonce_str>"
                + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<body><![CDATA[" + subject + "]]></body>"
                + "<out_trade_no>" + out_trade_no + "</out_trade_no>" + "<total_fee>" + finalMoney + "" + "</total_fee>"
                + "<spbill_create_ip>" + ipAddr + "</spbill_create_ip>" + "<notify_url>"
                + TxConfig.callbackUrl + "</notify_url>" + "<trade_type>" + tradeType + "</trade_type>"  + "</xml>";
        log.info("xml：" + xml);

        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String prepay_id = "";
        try {
            prepay_id = WeixinPayUtil.getPayNo(createOrderURL, xml);
            log.info("prepay_id:" + prepay_id);
            if (prepay_id.equals("")) {
                request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
                response.sendRedirect("error.jsp");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        SortedMap<String, String> finalPackage = new TreeMap<String, String>();
        String timestamp = Sha1Util.getTimeStamp();
        finalPackage.put("appid", APPID);
        finalPackage.put("partnerid", TxConfig.merId);
        finalPackage.put("prepayid", prepay_id);
        finalPackage.put("package", "Sign=WXPay");
        finalPackage.put("noncestr", nonce_str);
        finalPackage.put("timestamp", timestamp);
        String finalsign = reqHandler.createSign(finalPackage);
        finalPackage.put("sign", finalsign);

        finalPackage.put("bizOrderId", out_trade_no);
        finalPackage.put("orderId", out_trade_no);
        finalPackage.put("payPrice", finalMoney);

        // 构建充值信息
        Charge charge = buildModelService.buildCharge(certification, new BigDecimal(money), PayType.WE_CHAT.getCode(), ChargeStatus.ING.getCode());
        charge.setId(out_trade_no);

        // 添加充值记录
        int effectNum = chargeMapper.insertCharge(charge);
        if (effectNum == 1) {
            return AjaxResult.success("支付成功", finalPackage);
        }
        return AjaxResult.error("支付错误", out_trade_no);
    }

    /**
     * 微信退款
     *
     * @param charge 充值信息
     * @return 结果
     */
    @Override
    public boolean wxPayRefund(Charge charge) {
        log.info("进行退款===" + charge);
        String APPID = TxConfig.appId;
        String MERID = TxConfig.merId;
        String SIGNKEY = TxConfig.signKey;

        int randomNum = (int) (Math.random() * 1999 + 5000);
        String out_refund_no = TimeUtils.getSysTime("yyyyMMddHHmmss") + randomNum;

        // 随机数
        String nonce_str = MD5Utils.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());

        SecapiPayRefund payRefund = new SecapiPayRefund();
        payRefund.setAppid(APPID);
        payRefund.setMch_id(MERID);
        payRefund.setNonce_str(nonce_str);
        // 支付订单号
        payRefund.setOut_trade_no(charge.getId());
        payRefund.setOut_refund_no(out_refund_no);
        String finalMoney = StringUtils.getMoney(charge.getMoney() + "");
        // 原订单金额,单位:分
        payRefund.setTotal_fee(new Integer(finalMoney));
        // 退款订单金额,单位:分
        payRefund.setRefund_fee(new Integer(finalMoney));

        SecapiPayRefundResult refundResult = PayMchAPI.secapiPayRefund(payRefund, SIGNKEY);
        log.info("refundResult=====" + JSONObject.toJSONString(refundResult));
        if (refundResult.getSign_status() != null && refundResult.getSign_status()) {
            if ("SUCCESS".equals(refundResult.getReturn_code())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 支付宝手机端下单接口
     *
     * @param returnUrl 返回地址
     * @param orderId 订单ID
     * @param money 金额
     * @return 结果
     */
    @Override
    public AjaxResult aliMobilePay(String returnUrl, String orderId, String money) {
        String form = "";
        AlipayClient alipayClient = AliConfig.getAppPay();
        // 创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        // 在公共参数中设置回跳和通知地址
        alipayRequest.setNotifyUrl(AliConfig.notify_url);
        alipayRequest.setReturnUrl(returnUrl);
        // 填充业务参数
        alipayRequest.setBizContent("{" + "    \"out_trade_no\":\"" + orderId + "\","
                + "    \"product_code\":\"QUICK_WAP_WAY\"," + "    \"total_amount\":" + new BigDecimal(money) + ","
                + "    \"subject\":\"充值\"," + "    \"body\":\"充值\"}");
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
            return AjaxResult.success(orderId, form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return AjaxResult.error("支付错误", orderId);
        }
    }

    /**
     * 支付宝手机端下单接口
     *
     * @param returnUrl 返回地址
     * @param orderId 订单ID
     * @param money 金额
     * @return 结果
     */
    @Override
    public AjaxResult aliQrCodePay(String returnUrl, String orderId, String money) {
        String form = "";
        AlipayClient alipayClient = AliConfig.getAppPay();
        // 创建API对应的request类
        AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
        alipayRequest.setReturnUrl(returnUrl);
        // 在公共参数中设置回跳和通知地址
        alipayRequest.setNotifyUrl(AliConfig.notify_url);
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + orderId + "\"," +//商户订单号
                "    \"total_amount\":" + new BigDecimal(money) + "," +
                "    \"subject\":\"充值\"," +
                "    \"timeout_express\":\"90m\"}");//订单允许的最晚付款时间

        log.info("orderId:" + orderId);
        log.info("创建支付宝订单，请求参数：{} ", JSONObject.toJSONString(alipayRequest));
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(alipayRequest);
            log.info("创建支付宝订单，返回值：{} ", JSONObject.toJSONString(response));
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return AjaxResult.error("支付错误");
        }
        if (!response.isSuccess()) {
            return AjaxResult.error("支付错误");
        }
        JSONObject jsonObj = JSONObject.parseObject(response.getBody());
        String qrCode = jsonObj.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
        if (com.nft.common.utils.StringUtils.isBlank(qrCode)) {
            return AjaxResult.error("支付错误");
        }
        String imgName = IdUtils.getTradeNo() + ".png";
        ZxingUtils.getQRCodeImge(qrCode, 256, PathUtil.getImgBasePath() + File.separator + imgName);
        form = "/uploads/" + imgName;
        //根据response中的结果继续业务逻辑处理
        return AjaxResult.success(orderId, form);
    }

    /**
     * 支付宝PC端下单接口
     *
     * @param returnUrl 返回地址
     * @param orderId 订单ID
     * @param money 金额
     * @return 结果
     */
    @Override
    public AjaxResult aliPcPay(String returnUrl, String orderId, String money) {
        String form = "";
        AlipayClient alipayClient = AliConfig.getAppPay();
        // 创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        // 在公共参数中设置回跳和通知地址
        alipayRequest.setNotifyUrl(AliConfig.notify_url);
        // 填充业务参数
        alipayRequest.setBizContent("{" + "    \"out_trade_no\":\"" + orderId + "\","
                + "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," + "    \"total_amount\":"
                + new BigDecimal(money) + "," + "    \"subject\":\"充值\"," + "    \"body\":\"充值\"}");
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
            return AjaxResult.success(orderId, form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return AjaxResult.error("支付错误", orderId);
        }
    }

    /**
     * 支付宝APP端下单接口
     *
     * @param returnUrl 返回地址
     * @param orderId 订单ID
     * @param money 金额
     * @return 结果
     */
    @Override
    public AjaxResult aliAppPay(String returnUrl, String orderId, String money) {
        String form = "";
        AlipayClient alipayClient = AliConfig.getAppPay();
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("充值");
        model.setSubject("充值");
        model.setOutTradeNo(orderId);
        model.setTotalAmount(money);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AliConfig.notify_url);
        try {
            // 这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            // 就是orderString 可以直接给客户端请求，无需再做处理
            form = response.getBody();
            log.info(form);
            return AjaxResult.success(orderId, form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return AjaxResult.error("支付错误", orderId);
        }
    }

    /**
     * 获取授权信息
     *
     * @param isWeiXin 是否是微信
     * @param type 类型
     * @param address 地址
     * @return 结果
     */
    @Override
    public JSONObject getDetectAuth(String isWeiXin, String type, String address) {
        JSONObject jsonObj = null;
        try {
            Credential cred = new Credential(TxConfig.secretId, TxConfig.SecretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(TxConfig.faceEndpoint);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            FaceidClient client = new FaceidClient(cred, "ap-shanghai", clientProfile);
            // RedirectUrl 认证完成后重定向地址
            String params = "";
            // 在微信浏览器下的情况
            if (isWeiXin != null) {
                if (new Integer(type) == 1) {
                    // 创建账户的情况
                    params = "{\"RedirectUrl\":\"https://ctsign.cn/#/company2?type=1&address=" + address + "\",\"RuleId\":\"1\"}";
                } else {
                    // 直接去实名认证的情况
                    params = "{\"RedirectUrl\":\"https://ctsign.cn/#/company2?type=1&address=" + address + "\",\"RuleId\":\"1\"}";
                }
            } else {
                params = "{\"RedirectUrl\":\"https://ctsign.cn\",\"RuleId\":\"1\"}";
            }
            DetectAuthRequest req = DetectAuthRequest.fromJsonString(params, DetectAuthRequest.class);
            DetectAuthResponse resp = client.DetectAuth(req);
            jsonObj = JSONObject.parseObject(DetectAuthRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    /**
     * 获得人脸识别的视频
     *
     * @param nonce 随机数
     * @param orderNo 订单号
     * @param version 版本号
     * @param getFile 地址
     * @return 结果
     */
    @Override
    public String getVideo(String nonce, String orderNo, String version, Integer getFile) throws Exception {
        String ticketUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket?app_id=" + TxConfig.faceAppId + "&access_token=" + getAccessToken() + "&type=SIGN&version=1.0.0";
        String result = null;
        try {
            result = HttpClient.doGet(ticketUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //获取sign ticket
        String ticket = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()).getString("value");
        log.info("---- ticket:" + ticket);
        List<String> list = new ArrayList<>();
        //调用人脸识别签名
        list.add(TxConfig.faceAppId);
        list.add(version);
        list.add(nonce);
        list.add(orderNo);
        String sign = sign(list, ticket);
        //请求接口。如果需要传参拼接到接口后面。
        String tokenUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/server/sync?" +
                "app_id=" + TxConfig.faceAppId
                + "&order_no=" + orderNo
                + "&nonce=" + nonce + "&sign=" + sign
                + "&get_file=" + getFile +
                "&version=1.0.0";
        log.info(tokenUrl);
        return HttpClient.doGet(tokenUrl);
    }

    /**
     * 获取签名信息
     *
     * @param values
     * @param ticket
     * @return 结果
     */
    @Override
    public String sign(List<String> values, String ticket) {
        if (values == null) {
            throw new NullPointerException("values is null");
        }
        // remove null
        values.removeAll(Collections.singleton(null));
        values.add(ticket);
        Collections.sort(values);
        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        return Hashing.sha1().hashString(sb, Charset.forName("utf-8")).toString().toUpperCase();
    }

    /**
     * 获取访问的token
     *
     * @return 结果
     */
    @Override
    public String getAccessToken() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 请求接口,如果需要传参拼接到接口后面。
        String tokenUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/access_token?app_id=" + TxConfig.faceAppId + "&secret=" + TxConfig.faceSecret + "&grant_type=client_credential&version=1.0.0";
        String token = "";
        Object tokenObj = redisCache.getCacheObject("access_token");
        boolean isN = false;
        if (tokenObj == null) {
            isN = true;
        } else {
            String lastDate = JSONObject.parseObject(tokenObj.toString()).getString("date");
            token = JSONObject.parseObject(tokenObj.toString()).getString("token");
            double second = 0;
            Date date1, date2;
            try {
                date1 = dateFormat.parse(lastDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            date2 = new Date();
            double millisecond = date2.getTime() - date1.getTime();
            second = millisecond / (1000);
            //如果缓存已经存在了一个小时 那么重新生成一个token
            if (second >= 3600) {
                isN = true;
            }
        }
        if (isN) {
            String result = null;
            try {
                result = HttpClient.doGet(tokenUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            log.info("access_token:" + result);
            token = JSONObject.parseObject(result).getString("access_token");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            jsonObject.put("date", dateFormat.format(new Date()));
            redisCache.setCacheObject("access_token", jsonObject.toJSONString());
        }
        return token;
    }

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
    @Override
    public String getFaceId(String userId, String version, String nonce, String orderId, String name, String idNo, String sign) {
        String faceId = "";
        // 请求接口,如果需要传参拼接到接口后面。
        String url = "https://miniprogram-kyc.tencentcloudapi.com/api/server/h5/geth5faceid";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("webankAppId", TxConfig.faceAppId);
        jsonObject.put("orderNo", orderId);
        jsonObject.put("name", name);
        jsonObject.put("idNo", idNo);
        jsonObject.put("userId", userId);
        //jsonObject.put("sourcePhotoType", "1");
        jsonObject.put("version", version);
        jsonObject.put("sign", sign);
        String a = jsonObject.toJSONString();
        log.info("get" + a);
        String result = HttpClient.doPostJson(jsonObject.toJSONString(), null, url);
        log.info("获得faceId的接口，查看信息是否匹配：" + a);
        if (result != null) {
            JSONObject obj = JSONObject.parseObject(result);
            if (obj.getInteger("code") == 0) {
                log.info("信息匹配" + obj);
                faceId = obj.getJSONObject("result").getString("h5faceId");
            }
        }
        return faceId;
    }

    /**
     * 根据订单号查询腾讯云，判断是否通过人脸识别
     *
     * @param nonce 随机数
     * @param orderNo 订单号
     * @param version 版本号
     * @return 结果
     */
    @Override
    public String getMsgByOrderNo(String nonce, String orderNo, String version) {
        String ticketUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket?app_id=" + TxConfig.faceAppId + "&access_token=" + getAccessToken() + "&type=SIGN&version=1.0.0";
        try {
            log.info("--- ticketUrl：" + ticketUrl);
            String result = HttpClient.doGet(ticketUrl);
            log.info("人脸识别订单查询结果：" + result);
            //获取sign ticket
            String ticket = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()).getString("value");
            log.info("---- ticket:" + ticket);
            List<String> list = new ArrayList<>();
            //调用人脸识别签名
            list.add(TxConfig.faceAppId);
            list.add(version);
            list.add(nonce);
            list.add(orderNo);
            String sign = sign(list, ticket);
            //请求接口。如果需要传参拼接到接口后面。
            String tokenUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/server/sync?" +
                    "app_id=" + TxConfig.faceAppId
                    + "&order_no=" + orderNo
                    + "&nonce=" + nonce + "&sign=" + sign
                    + "&version=1.0.0";
            log.info(tokenUrl);
            return HttpClient.doGet(tokenUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
