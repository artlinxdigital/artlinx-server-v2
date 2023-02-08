package com.nft.mall.api.controller.login;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nft.common.annotation.Log;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.user.RegisterUserParam;
import com.nft.common.core.domain.pojo.user.ResetPwdParam;
import com.nft.common.core.redis.RedisCache;
import com.nft.common.enums.BusinessType;
import com.nft.common.enums.CertificationType;
import com.nft.common.utils.RandomUtils;
import com.nft.common.utils.StringUtils;
import com.nft.common.utils.ValidatorUtils;
import com.nft.mall.service.IThirdPartyService;
import com.nft.mall.service.IUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录Controller
 *
 * @author nft
 * @date 2021-05-12
 */
@Slf4j
@RestController
@RequestMapping("/mall/login")
@Api(tags = "登录相关接口")
public class LoginController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IThirdPartyService thirdPartyService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询账号是否已经存在
     */
    @Log(title = "查询账号是否已经存在", businessType = BusinessType.GET)
    @GetMapping(value = "/checkAccountIsExist/{account}")
    public AjaxResult checkAccountIsExist(@PathVariable("account") String account) {
        if (StringUtils.isEmpty(account)) {
            return AjaxResult.error("请输入账号");
        }
        // 校验账号格式
        if (!ValidatorUtils.checkAccount(account)) {
            return AjaxResult.error("请输入正确的手机号或者邮箱");
        }
        return userService.checkAccountIsExist(account, CertificationType.PERSON.getCode());
    }

    /**
     * 查询地址是否已经存在
     */
    @Log(title = "查询地址是否已经存在", businessType = BusinessType.GET)
    @GetMapping(value = "/checkAddressIsExist/{address}")
    public AjaxResult checkAddressIsExist(@PathVariable("address") String address) {
        if (StringUtils.isEmpty(address) || !address.startsWith("0x")) {
            return AjaxResult.error("请输入账号");
        }
        return userService.checkAddressIsExist(address);
    }

    /**
     * 获取验证码(注册或者登录)
     */
    @Log(title = "商城用户获取验证码", businessType = BusinessType.GET)
    @GetMapping(value = "/getCode/{account}")
    public AjaxResult getCode(@PathVariable("account") String account) {
        if (StringUtils.isEmpty(account)) {
            return AjaxResult.error("请输入账号");
        }
        // 校验账号格式
        if (!ValidatorUtils.checkAccount(account)) {
            return AjaxResult.error("请输入正确的手机号或者邮箱");
        }
        // 生成验证码
        String msgCode = RandomUtils.getDigitString(6);
        log.info("msgCode:" + msgCode);
        Object object = redisCache.getCacheObject("time_" + account);
        if (ObjectUtil.isNotNull(object)) {
            if (ValidatorUtils.checkTimeInterval(object, 30000L)) {
                return AjaxResult.error("Frequent operation.");
            }
        }
        // 发送邮件
        if (ValidatorUtils.isEmail(account)) {
            return thirdPartyService.sendHtmlMail(account, msgCode, 5, TimeUnit.MINUTES);
        }
        // 发送短信
        if (ValidatorUtils.isMobile(account)) {
            return thirdPartyService.sendSmsByTxYun(account, msgCode, 5, TimeUnit.MINUTES);
        }
        return AjaxResult.error("Failed to send.");
    }

    /**
     * 商城用户注册
     */
    @Log(title = "商城用户注册", businessType = BusinessType.INSERT)
    @PostMapping("/register")
    public AjaxResult register(@Validated @RequestBody RegisterUserParam registerUserParam) {
        // 账号
        String account = registerUserParam.getAccount();
        // 校验账号格式
        if (!ValidatorUtils.checkAccount(account)) {
            return AjaxResult.error("请输入正确的手机号或者邮箱.");
        }
        // 验证码
        String msgCode = registerUserParam.getMsgCode();
        Object object = redisCache.getCacheObject("code_" + account);
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
        return userService.register(registerUserParam);
    }

    /**
     * 登录(密码)
     */
    @Log(title = "商城用户密码登录", businessType = BusinessType.GET)
    @PostMapping("/loginByPwd")
    public AjaxResult loginByPwd(String account) {
        if (StringUtils.isEmpty(account)) {
            return AjaxResult.error("请输入账号");
        }
        // 校验账号格式
        if (!ValidatorUtils.checkAccount(account)) {
            return AjaxResult.error("请输入正确的手机号或者邮箱");
        }
        return userService.loginByPwd(account);
    }

    /**
     * 登录(地址)
     */
    @Log(title = "商城用户私钥登录", businessType = BusinessType.GET)
    @PostMapping("/loginByWalletAddress")
    public AjaxResult loginByWalletAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return AjaxResult.error("请输入地址");
        }
        return userService.loginByWalletAddress(address);
    }

    /**
     * 通过私钥重置托管密码
     */
    @Log(title = "商城用户通过私钥重置托管密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwdByPrivateKey")
    public AjaxResult resetPwdByPrivateKey(@Validated @RequestBody ResetPwdParam resetPwdParam) {
        if (Objects.isNull(resetPwdParam) || CollectionUtil.isEmpty(resetPwdParam.getResetEncryptParamList())) {
            return AjaxResult.error("重置失败");
        }
        return userService.resetPwdByPrivateKey(resetPwdParam);
    }

    /**
     * 根据account和type获得详细信息
     */
    @Log(title = "根据account和type获得详细信息", businessType = BusinessType.GET)
    @GetMapping("/getUserByAccountAndType/{account}/{type}")
    public AjaxResult getUserByAccountAndType(@PathVariable("account") String account, @PathVariable("type") Integer type) {
        if (StringUtils.isBlank(account) || ObjectUtil.isNull(type)) {
            return AjaxResult.error("参数错误");
        }
        return userService.getUserByAccountAndType(account, type);
    }

    /**
     * 根据id和type获得详细信息
     */
    @Log(title = "根据id和type获得详细信息", businessType = BusinessType.GET)
    @GetMapping("/getMsgByIds")
    public AjaxResult getMsgByIds(Long certificationId, Integer type) {
        if (ObjectUtil.isNull(certificationId) || ObjectUtil.isNull(type)) {
            return AjaxResult.error("参数错误");
        }
        return userService.getMsgByIds(certificationId, type);
    }
}
