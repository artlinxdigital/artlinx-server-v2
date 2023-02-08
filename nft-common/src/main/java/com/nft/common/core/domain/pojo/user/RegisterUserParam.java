package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 商城用户注册信息
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class RegisterUserParam {

    /**
     * 账号
     */
    @ApiModelProperty(value = "手机号或邮箱", required = true)
    @NotNull(message = "手机号或者邮箱不能为空")
    @NotBlank
    private String account;

    /**
     * 这个账号注册的id
     */
    @ApiModelProperty(value = "账号注册的ID")
    private Long loginId;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码", required = true)
    @NotNull(message = "验证码不能为空")
    @NotBlank
    private String msgCode;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址", required = true)
    @NotNull(message = "地址不能为空")
    @NotBlank
    private String walletAddress;

    /**
     * 公钥
     */
    @ApiModelProperty(value = "公钥", required = true)
    @NotNull(message = "公钥不能为空")
    @NotBlank
    private String publicKey;

    /**
     * 托管密码加密私钥
     */
    @ApiModelProperty(value = "托管密码加密私钥", required = true)
    @NotNull(message = "托管密码加密私钥不能为空")
    @NotBlank
    private String encryptPrivateKey;

    /**
     * 私钥加密托管密码
     */
    @ApiModelProperty(value = "私钥加密托管密码", required = true)
    @NotNull(message = "私钥加密托管密码不能为空")
    @NotBlank
    private String encryptPassword;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String otherCode;

}
