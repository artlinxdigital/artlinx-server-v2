package com.nft.mall.api.controller.pay;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.nft.common.annotation.Log;
import com.nft.common.config.TxConfig;
import com.nft.common.constant.Constants;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.user.UserAuthCallBackParam;
import com.nft.common.core.domain.pojo.user.UserAuthParam;
import com.nft.common.core.domain.pojo.user.UserPcAuthCallBackParam;
import com.nft.common.core.redis.RedisCache;
import com.nft.common.enums.BusinessType;
import com.nft.common.enums.CertificationStatus;
import com.nft.common.enums.CertificationType;
import com.nft.common.utils.CopyUtils;
import com.nft.common.utils.DateUtils;
import com.nft.common.utils.HttpClient;
import com.nft.common.utils.PathUtil;
import com.nft.mall.domain.Certification;
import com.nft.mall.service.ICertificationService;
import com.nft.mall.service.IThirdPartyService;
import com.nft.mall.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * 用户实名Controller
 *
 * @author nft
 * @date 2021-05-14
 */
@Slf4j
@RestController
@RequestMapping("/mall/approve")
public class TxCertificationController {

    @Autowired
    private ICertificationService certificationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IThirdPartyService thirdPartyService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 接口鉴权 (个人实名认证) 并返回微信h5认证页面URL
     *
     * @param certification
     * @throws IOException
     */
    @Log(title = "接口鉴权 (个人实名认证) 并返回微信h5认证页面URL", businessType = BusinessType.OTHER)
    @PostMapping("/detectAuth")
    public Object detectAuth(@RequestBody Certification certification) {
        int result;
        Certification certificationByAddress = userService.getByWalletAddress(certification.getWalletAddress());
        JSONObject jsonObj;

        log.info(certification.getMobile());

        Object object = redisCache.getCacheObject("code" + certification.getMobile().trim());

        log.info("object：" + JSONUtil.toJsonStr(object));
        log.info(certification.getMsgCode());
        if (Objects.isNull(object)) {
            return AjaxResult.error("The verification code has expired, please resend.");
        }
        String saveMsgCode = (String) object;
        if (!saveMsgCode.equals(certification.getMsgCode())) {
            return AjaxResult.error("Verification code error.");
        }

        if (certificationByAddress == null) {
            jsonObj = thirdPartyService.getDetectAuth(certification.getIsWeiXin(), certification.getType1(), certification.getWalletAddress());
            certification.setBizToken(jsonObj.getString("BizToken"));
            certification.setType(1);
            certification.setUrl(jsonObj.getString("Url"));
            result = certificationService.insertCertification(certification);
        } else {
            if (certificationByAddress.getStatus() == 1) {
                return AjaxResult.error("不能重复认证");
            } else if (DateUtils.getMinInterval(certificationByAddress.getUpdateTime(), new Date()) < 60 && certificationByAddress.getType() != 2) {
                //未超时的情况，用上一次的二维码  但是上次是企业认证的情况就重新生成
                jsonObj = new JSONObject();
                jsonObj.put("BizToken", certificationByAddress.getBizToken());
                jsonObj.put("Url", certificationByAddress.getUrl());
                result = 1;
            } else {
                //超时 修改新的二维码
                jsonObj = thirdPartyService.getDetectAuth(certification.getIsWeiXin(), certification.getType1(), certification.getWalletAddress());
                certification.setBizToken(jsonObj.getString("BizToken"));
                certification.setType(1);
                certification.setUrl(jsonObj.getString("Url"));
                certification.setId(certificationByAddress.getId());
                result = certificationService.updateCertification(certification);
            }
        }
        redisCache.deleteObject("code" + certification.getMobile().trim());
        if (result != 1) {
            return AjaxResult.error("操作失败");
        }
        return AjaxResult.success("操作成功", jsonObj);
    }

    /**
     * 根据地址查询实名信息
     *
     * @param request
     */
    @Log(title = "根据地址查询实名信息", businessType = BusinessType.GET)
    @GetMapping("/getCertificationByAddress")
    public Object getCertificationByAddress(HttpServletRequest request) {
        String address = request.getParameter("address");
        Certification certification = userService.getByWalletAddress(address);
        return ObjectUtil.isNull(certification) ? AjaxResult.error("账户未实名") : AjaxResult.success(certification);
    }

    /**
     * 删除实名信息
     *
     * @param request
     * @throws IOException
     */
    @Log(title = "删除实名信息", businessType = BusinessType.DELETE)
    @GetMapping("/delete")
    public Object delete(HttpServletRequest request) {
        String address = request.getParameter("address");
        Certification certification = userService.getByWalletAddress(address);
        return ObjectUtil.isNull(certification) ? AjaxResult.error("未实名认证") : userService.deleteByWalletAddress(address);
    }

    /**
     * 验证信息是否经过实名
     *
     * @param request
     */
    @Log(title = "验证信息是否经过实名", businessType = BusinessType.GET)
    @GetMapping("/checkCertification")
    public Object checkCertification(HttpServletRequest request) {
        String address = request.getParameter("address");
        String realName = request.getParameter("realName");
        String number = request.getParameter("number");
        Map<String, String> map = new HashMap<String, String>();
        map.put("address", address);
        map.put("realName", realName);
        map.put("number", number);
        Certification certification = userService.getCertificationByMap(map);
        return ObjectUtil.isNull(certification) ? AjaxResult.error("certification", null) : AjaxResult.success("certification", certification);
    }

    /**
     * APP人脸认证 (个人实名认证--旷世的人脸认证)
     *
     * @param request
     * @throws IOException
     */
    @Log(title = "APP人脸认证 (个人实名认证--旷世的人脸认证)", businessType = BusinessType.INSERT)
    @PostMapping("/faceAuthenticationNew")
    public Object faceAuthenticationNew(HttpServletRequest request) {
        log.info("==========================>" + request.getParameter("privateKey"));
        Long id = new Long(request.getParameter("id"));
        String address = request.getParameter("address");
        String publicKey = request.getParameter("publicKey");
        String realName = request.getParameter("realName");
        String number = request.getParameter("number");
        String type = request.getParameter("type");
        String privateKey = request.getParameter("privateKey");
        log.info("id:" + id + "address:" + address + "realName:" + realName + "number:" + number + "privateKey:" + privateKey);

        int result;
        Certification certificationByAddress = certificationService.selectCertificationById(id);
        if (certificationByAddress == null) {
            return AjaxResult.error("用户不存在", null);
        }
        if (StringUtils.isEmpty(type) || Integer.parseInt(type) != CertificationType.PERSON.getCode()) {
            return AjaxResult.error("用户类型错误", null);
        }
        if (certificationByAddress.getStatus() == CertificationStatus.SUCCESS.getCode()) {
            return AjaxResult.error("不能重复认证", null);
        }
        try {
            Certification certification = new Certification();
            //状态改为“成功，但没上链”
            certification.setStatus(CertificationStatus.SUCCESS.getCode());
            certification.setWalletAddress(address);
            certification.setPublicKey(publicKey);
            certification.setRealName(realName);
            certification.setNumber(number);
            certification.setId(id);
            log.info("controller===>" + privateKey);
            certification.setPrivateKey(privateKey);
            certification.setType(certificationByAddress.getType());
            result = certificationService.updateCertification(certification);
            return result == 1 ? AjaxResult.success("认证成功") : AjaxResult.error("认证失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("认证失败", null);
    }

    /**
     * 个人移动端认证成功后调用-v2.3
     *
     * @param callBackParam
     * @return
     * @throws Exception
     */
    @Log(title = "个人移动端认证成功后调用", businessType = BusinessType.UPDATE)
    @PostMapping("/faceAuthenticationForH5Person")
    public Object faceAuthenticationForH5Person(@Validated @RequestBody UserAuthCallBackParam callBackParam) {
        String orderNo = callBackParam.getOrderNo();
        log.info("调用faceAuthenticationForH5Person接口传进来的orderNo是：" + orderNo);
        if (StringUtils.isEmpty(orderNo)) {
            return AjaxResult.error("id错误", null);
        }
        Object object = redisCache.getCacheObject(orderNo);
        JSONObject jsonObject = JSONObject.parseObject(object.toString());
        if (Objects.isNull(jsonObject)) {
            return AjaxResult.error("没有从redis中取出" + orderNo + "对应的值", null);
        }
        log.info("取到的值是：" + JSONObject.toJSONString(jsonObject));
        String nonce = jsonObject.getString("nonce");
        //根据订单号查询腾讯云是否通过人脸识别
        String version = "1.0.0";
        String msgResult = thirdPartyService.getMsgByOrderNo(nonce, orderNo, version);
        log.info("腾讯云返回结果：" + JSONObject.parseObject(msgResult));
        String code = JSONObject.parseObject(msgResult).getString("code");
        if (!code.equals("0")) {
            return AjaxResult.error("识别失败", null);
        }
        String realName = JSONObject.parseObject(msgResult).getJSONObject("result").getString("name");
        String number = JSONObject.parseObject(msgResult).getJSONObject("result").getString("idNo");
        String id = jsonObject.getString("loginId");
        String type = callBackParam.getType();
        log.info("id:" + id + "realName:" + realName + "number:" + number);
        int result;
        Certification certificationByAddress = certificationService.selectCertificationById(Long.parseLong(id));
        if (ObjectUtil.isNull(certificationByAddress)) {
            return AjaxResult.error("用户不存在", null);
        }
        if (StringUtils.isEmpty(type) || Integer.parseInt(type) != CertificationType.PERSON.getCode()) {
            return AjaxResult.error("用户类型错误", null);
        }
        if (certificationByAddress.getStatus() == CertificationStatus.SUCCESS.getCode()) {
            return AjaxResult.error("不能重复认证", null);
        }
        try {
            // 封装用户实名参数
            UserAuthParam userAuthParam = CopyUtils.copy(callBackParam, UserAuthParam.class);
            userAuthParam.setCertificationId(Long.parseLong(id));
            userAuthParam.setRealName(realName);
            userAuthParam.setNumber(number);
            return userService.updateUserAuthStatus(userAuthParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("认证失败", null);
    }

    /**
     * APP人脸认证 (个人实名认证)
     *
     * @param callBackParam
     * @throws IOException
     */
    @Log(title = "APP人脸认证 (个人实名认证)", businessType = BusinessType.INSERT)
    @PostMapping("/faceAuthentication")
    public Object faceAuthentication(@Validated @RequestBody UserPcAuthCallBackParam callBackParam) {
        Long id = callBackParam.getCertificationId();

        String realName = callBackParam.getRealName();
        String number = callBackParam.getNumber();
        String type = callBackParam.getType();
        log.info("id:" + id + "realName:" + realName + "number:" + number);
        Certification certificationByAddress = certificationService.selectCertificationById(id);
        if (certificationByAddress == null) {
            return AjaxResult.error("用户不存在", null);
        }
        if (StringUtils.isEmpty(type) || Integer.parseInt(type) != CertificationType.PERSON.getCode()) {
            return AjaxResult.error("用户类型错误", null);
        }
        if (certificationByAddress.getStatus() == CertificationStatus.SUCCESS.getCode()) {
            return AjaxResult.error("不能重复认证", null);
        }
        try {
            // 封装用户实名参数
            UserAuthParam userAuthParam = CopyUtils.copy(callBackParam, UserAuthParam.class);
            return userService.updateUserAuthStatus(userAuthParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("认证失败", null);
    }

    /**
     * Title: companyAuthentication
     * Description: APP腾讯云慧眼SDK认证
     *
     * @param request
     * @throws IOException
     */
    @Log(title = "APP腾讯云慧眼SDK认证", businessType = BusinessType.GET)
    @GetMapping("/getUserSign")
    public Object getUserSign(HttpServletRequest request) throws Exception {
        String appId = TxConfig.faceAppId;
        String userId = request.getParameter("userId");
        String nonce = request.getParameter("nonce");
        String orderNo = request.getParameter("orderNo");
        String secret = "yFFyxFKLVKChzWJsWdpweRYiUSkK4adv0Sf2wr0Qo873qRr8Pt4YhoiBtyaMeImO";
        String version = "1.0.0";

        // 请求接口,如果需要传参拼接到接口后面。
        String tokenUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/access_token?app_id=" + appId + "&secret=" + secret + "&grant_type=client_credential&version=1.0.0";

        String result = HttpClient.doGet(tokenUrl);

        if (StringUtils.isEmpty(result)) {
            return AjaxResult.error("参数错误", null);
        }
        log.info("access_token:" + result);
        String access_token = JSONObject.parseObject(result).getString("access_token");
        String ticketUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket?app_id=" + appId + "&access_token=" + access_token + "&type=NONCE&version=1.0.0&user_id=" + userId;

        result = HttpClient.doGet(ticketUrl);

        if (StringUtils.isEmpty(result)) {
            return AjaxResult.error("参数错误", null);
        }
        log.info("ticketUrl:" + result);
        String ticket = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()).getString("value");
        log.info("---- ticket:" + ticket);

        List<String> list = new ArrayList<>();
        list.add(appId);
        list.add(userId);
        list.add(version);
        list.add(nonce);

        String values = thirdPartyService.sign(list, ticket);
        return StringUtils.isEmpty(values) ? AjaxResult.error("生成失败", null) : AjaxResult.success("生成成功", values);
    }

    /**
     * PC端人脸认证
     *
     * @param request
     * @throws IOException
     */
    @Log(title = "PC端人脸认证", businessType = BusinessType.GET)
    @GetMapping("/getFaceId")
    public AjaxResult getFaceId(HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        String idNo = request.getParameter("idNo");
        String userId = "U" + DateUtils.getNowDate().getTime();
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String orderNo = "O" + DateUtils.getNowDate().getTime();
        String version = "1.0.0";
        String ticketUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket?app_id=" + TxConfig.faceAppId + "&access_token=" + thirdPartyService.getAccessToken() + "&type=SIGN&version=1.0.0";
        String result = HttpClient.doGet(ticketUrl);

        // 获取sign ticket
        String ticket = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()).getString("value");
        log.info("---- ticket:" + ticket);

        // 获取faceID签名
        List<String> list = new ArrayList<>();
        list.add(TxConfig.faceAppId);
        list.add(orderNo);
        list.add(name);
        list.add(idNo);
        list.add(userId);
        list.add(version);
        String sign = thirdPartyService.sign(list, ticket);
        String faceId = thirdPartyService.getFaceId(userId, version, nonce, orderNo, name, idNo, sign);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("userId", userId);
        jsonObj.put("nonce", nonce);
        jsonObj.put("orderNo", orderNo);
        jsonObj.put("faceId", faceId);
        return faceId.equals("") ? AjaxResult.error("生成失败", null) : AjaxResult.success("生成成功", jsonObj);
    }

    /**
     * PC端人脸认证
     *
     * @param request
     * @throws IOException
     */
    @Log(title = "PC端人脸认证", businessType = BusinessType.GET)
    @GetMapping("/startPcFace")
    public Object startPcFace(HttpServletRequest request) throws Exception {
        String userId = request.getParameter("userId");
        String nonce = request.getParameter("nonce");
        String orderNo = request.getParameter("orderNo");
        String faceId = request.getParameter("faceId");
        String from = request.getParameter("from");
        String url = Constants.PC_PERSON_SUCCESS_URL;
        String version = "1.0.0";
        List<String> list = new ArrayList<>();
        //获取Nonce ticket
        String ticketUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket?app_id=" + TxConfig.faceAppId + "&access_token=" + thirdPartyService.getAccessToken() + "&type=NONCE&version=1.0.0&user_id=" + userId;
        String result = HttpClient.doGet(ticketUrl);
        String ticket = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()).getString("value");

        //调用人脸识别签名
        list = new ArrayList<>();
        list.add(TxConfig.faceAppId);
        list.add(userId);
        list.add(nonce);
        list.add(version);
        list.add(faceId);
        list.add(orderNo);
        String sign = thirdPartyService.sign(list, ticket);

        String httpUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/pc/login?webankAppId=" + TxConfig.faceAppId +
                "&version=1.0.0&nonce=" + nonce +
                "&orderNo=" + orderNo +
                "&h5faceId=" + faceId +
                "&url=" + url +
                "&userId=" + userId +
                "&sign=" + sign;

        return AjaxResult.success("查询成功", httpUrl);
    }

    /**
     * H5端人脸认证-V2.3(不对数据加密)
     *
     * @param request
     * @throws Exception
     */
    @Log(title = "H5端人脸认证", businessType = BusinessType.GET)
    @GetMapping("/getFaceIdForH5")
    public Object getFaceIdForH5(HttpServletRequest request) throws Exception {
        log.info("调用getFaceIdForH5的接口，调用成功后进入人脸识别");
        String name = request.getParameter("name");
        String idNo = request.getParameter("idNo");
        String loginId = request.getParameter("loginId");
        log.info("传进来的loginId:" + loginId);
        log.info("" + (Objects.isNull(loginId) || loginId.equals("") || loginId == "null"));
        if (Objects.isNull(loginId) || loginId.equals("")) {
            String walletAddress = request.getParameter("walletAddress");
            log.info("H5端传进来的地址为：" + walletAddress);
            if (Objects.isNull(walletAddress)) {
                return AjaxResult.error("地址为空", null);
            }
            Certification certification = userService.getByWalletAddress(walletAddress);
            if (Objects.isNull(certification)) {
                return AjaxResult.error("参数有误", null);
            }
            loginId = certification.getId().toString();
            log.info("h5端查询机构后得到的loginId：" + loginId);
        }
        log.info(name + "======" + idNo);
        String userId = "U" + DateUtils.getNowDate().getTime();
        String nonce = UUID.randomUUID().toString().replace("-", "");
        String orderNo = "O" + DateUtils.getNowDate().getTime();
        String version = "1.0.0";
        String ticketUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket?app_id=" + TxConfig.faceAppId + "&access_token=" + thirdPartyService.getAccessToken() + "&type=SIGN&version=1.0.0";
        String result = HttpClient.doGet(ticketUrl);
        if (!"0".equals(JSONObject.parseObject(result).get("code"))) {
            return AjaxResult.error(JSONObject.parseObject(result).get("msg").toString(), null);
        }
        log.info("" + JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()));
        //获取sign ticket
        String ticket = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()).getString("value");
        log.info("---- ticket:" + ticket);
        //获取faceID签名
        List<String> list = new ArrayList<>();
        list.add(TxConfig.faceAppId);
        list.add(orderNo);
        list.add(name);
        list.add(idNo);
        list.add(userId);
        list.add(version);
        String sign = thirdPartyService.sign(list, ticket);
        String faceId = thirdPartyService.getFaceId(userId, version, nonce, orderNo, name, idNo, sign);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("userId", userId);
        jsonObj.put("nonce", nonce);
        jsonObj.put("orderNo", orderNo);
        jsonObj.put("faceId", faceId);
        jsonObj.put("loginId", loginId);
        //个人实名移动端需要
        redisCache.setCacheObject(orderNo, JSONObject.toJSONString(jsonObj));
        log.info("startH5FaceKey_" + orderNo + "开始存了：" + redisCache.getCacheObject(orderNo));
        if (faceId.equals("")) {
            return AjaxResult.error("生成失败", null);
        } else {
            return AjaxResult.success("生成成功", jsonObj);
        }
    }

    /**
     * h5端人脸认证-V2.3
     *
     * @param request
     * @throws IOException
     */
    @Log(title = "h5端法人人脸认证", businessType = BusinessType.GET)
    @GetMapping("/startFaceForLeader")
    public String startFaceForLeader(HttpServletRequest request) throws Exception {
        String userId = request.getParameter("userId");
        String nonce = request.getParameter("nonce");
        String orderNo = request.getParameter("orderNo");
        String faceId = request.getParameter("faceId");
        String url = "";
        log.info("=========法人通过短信进入人脸识别，进入人脸识别结果页面");
        //H5端注册机构调用这个接口会传过来from，值为1，当前注册人不是法人
        if (Objects.isNull(request.getParameter("from"))) {
            url = Constants.H5_LEADER_SUCCESS_URL;
        } else if (request.getParameter("from").equals("1")) {
            url = Constants.H5_LEADER_URL_ORG;
        }
        log.info("========法人进入人脸识别时的url：" + url);
        String version = "1.0.0";
        List<String> list = new ArrayList<>();
        //获取Nonce ticket
        String ticketUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket?app_id=" + TxConfig.faceAppId + "&access_token=" +
                thirdPartyService.getAccessToken() + "&type=NONCE&version=1.0.0&user_id=" + userId;
        String result = HttpClient.doGet(ticketUrl);
        String ticket = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()).getString("value");

        //调用人脸识别签名
        list = new ArrayList<>();
        list.add(TxConfig.faceAppId);
        list.add(userId);
        list.add(nonce);
        list.add(version);
        list.add(faceId);
        list.add(orderNo);
        log.info("信息为userId:" + userId + "faceId:" + faceId + "orderNo:" + orderNo + "nonce:" + nonce);
        redisCache.setCacheObject(orderNo, JSONObject.toJSONString(request.getParameterMap()));
        String sign = thirdPartyService.sign(list, ticket);
        String httpUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/web/login?webankAppId=" + TxConfig.faceAppId +
                "&version=1.0.0&nonce=" + nonce +
                "&orderNo=" + orderNo +
                "&h5faceId=" + faceId +
                "&url=" + URLUtil.encode(url) +
                "&resultType=1" +
                "&userId=" + userId +
                "&sign=" + sign +
                "&from=browser";
        return "redirect:" + httpUrl;
    }

    /**
     * 法人人脸识别成功后的回调-H5人脸识别成功后回调-V2.3
     *
     * @return
     * @throws Exception
     */
    @Log(title = "法人人脸识别成功后的回调-H5人脸识别成功后回调", businessType = BusinessType.UPDATE)
    @PostMapping("/faceAuthLeaderH5")
    public Object faceAuthLeaderH5(@RequestBody JSONObject query) throws Exception {
        log.info("调用浦东公证处存储信息");
        if (Objects.isNull(query.get("id"))) {
            return AjaxResult.error("传入的机构id为空", null);
        }
        log.info("====参数id:" + query.getLong("id") + ";orderNo:" + query.getString("orderNo"));
        Long id = query.getLong("id");
        Certification certification = certificationService.selectCertificationById(id);
        if (Objects.isNull(certification)) {
            return AjaxResult.error("该机构不存在", null);
        }
        //可能会重复调用（h5刷新页面，会一直调用）
        if (certification.getStatus() == CertificationStatus.SUCCESS.getCode()) {
            return AjaxResult.error("该机构已认证成功", null);
        }
        String orderNo = query.getString("orderNo");
        String version = "1.0.0";
        Object object = redisCache.getCacheObject(orderNo);
        JSONObject jsonObject = JSONObject.parseObject(object.toString());
        String nonce = "";
        if (jsonObject.get("nonce") instanceof List) {
            nonce = jsonObject.getJSONArray("nonce").get(0).toString();
        } else {
            nonce = jsonObject.get("nonce").toString();
        }
        log.info(nonce);
        //getFile:值为1：返回视频和照片 值为2：返回照片 值为3：返回视频 其他：不返回
        String videoResult = thirdPartyService.getVideo(nonce, orderNo, version, 1);
        log.info("========获得视频的返回结果：" + videoResult);
        String code = JSONObject.parseObject(videoResult).getString("code");
        log.info("=====videoResult转换后查看参数：" + JSONObject.parseObject(videoResult));
        if (!code.equals("0")) {
            return AjaxResult.error("获取人脸识别视频失败", null);
        }
        //如果获取人脸识别视频成功（视频是base64编码），则获取其他参数，并把视频存到服务器上
        String video = JSONObject.parseObject(videoResult).getJSONObject("result").getString("video");
        //把视频存到服务器上
        String filePath = PathUtil.getImgBasePath() + File.separator + certification.getNumber() + File.separator + "legalVideo.MP4";
        Files.write(Paths.get(filePath), java.util.Base64.getDecoder().decode(video), StandardOpenOption.CREATE);
        //更新数据库数据
        certification.setStatus(CertificationStatus.SUCCESS.getCode());
        int result = certificationService.updateCertification(certification);
        if (result != 1) {
            return AjaxResult.error("认证失败", null);
        }
        return AjaxResult.success();
    }

    /**
     * h5端人脸认证
     *
     * @param request
     * @throws IOException
     */
    @Log(title = "h5端人脸认证", businessType = BusinessType.GET)
    @GetMapping("/startH5Face")
    public Object startH5Face(HttpServletRequest request) throws Exception {
        log.info("===========我是法人，人脸识别结果页面展示");
        String userId = request.getParameter("userId");
        String nonce = request.getParameter("nonce");
        String orderNo = request.getParameter("orderNo");
        String faceId = request.getParameter("faceId");
        String url = "";
        //没有值：草田签移动端扫码认证；值为1：H5端注册机构(当前操作人是法人)；值为2：H5注册认证签署合同（当前操作是法人）
        if (Objects.isNull(request.getParameter("from"))) {
            url = Constants.H5_PERSON_SUCCESS_URL;
        } else if (request.getParameter("from").equals("1")) {
            url = Constants.H5_PERSON_LEADER_URL_ORG;
        } else if (request.getParameter("from").equals("2")) {
            url = Constants.H5_LEADER_URL_PERSON;
        }
        log.info("=========url是：" + url);
        String version = "1.0.0";
        List<String> list = new ArrayList<>();
        // 获取Nonce ticket
        String ticketUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/oauth2/api_ticket?app_id=" + TxConfig.faceAppId +
                "&access_token=" + thirdPartyService.getAccessToken() + "&type=NONCE&version=1.0.0&user_id=" + userId;
        String result = HttpClient.doGet(ticketUrl);
        String ticket = JSONObject.parseObject(JSONObject.parseObject(result).getJSONArray("tickets").get(0).toString()).getString("value");
        // 调用人脸识别签名
        list = new ArrayList<>();
        list.add(TxConfig.faceAppId);
        list.add(userId);
        list.add(nonce);
        list.add(version);
        list.add(faceId);
        list.add(orderNo);
        String sign = thirdPartyService.sign(list, ticket);
        String httpUrl = "https://miniprogram-kyc.tencentcloudapi.com/api/web/login?webankAppId=" + TxConfig.faceAppId +
                "&version=1.0.0&nonce=" + nonce +
                "&orderNo=" + orderNo +
                "&h5faceId=" + faceId +
                "&url=" + URLUtil.encode(url) +
                "&resultType=1" +
                "&userId=" + userId +
                "&sign=" + sign +
                "&from=browser";
        return AjaxResult.success("查询成功", httpUrl);
    }

}
