package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 银行配置参数
 *
 * @author nft
 * @date 2021-11-02
 */
@Data
public class CertificationBankParam {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id")
    private Long certificationId;

    /**
     * 邮箱
     */
//    @ApiModelProperty(value = "邮箱", required = true)
//    @NotNull(message = "邮箱")
//    @NotBlank
//    private String email;

    /**
     * 银行用户名
     */
    @ApiModelProperty(value = "银行用户名", required = true)
    @NotNull(message = "银行用户名")
    @NotBlank
    private String accountName;

    /**
     * 国家/地区
     */
    @ApiModelProperty(value = "国家/地区", required = true)
    @NotNull(message = "国家/地区")
    @NotBlank
    private String country;

    /**
     * 银行名
     */
    @ApiModelProperty(value = "银行名", required = true)
    @NotNull(message = "银行名")
    @NotBlank
    private String bankName;

    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号", required = true)
    @NotNull(message = "银行账号")
    @NotBlank
    private String accountNumber;

    /**
     * 币种
     */
    @ApiModelProperty(value = "币种", required = true)
    @NotNull(message = "币种")
    @NotBlank
    private String accountCurrency;

    /**
     * 账号类型
     */
    @ApiModelProperty(value = "账号类型", required = true)
    @NotNull(message = "账号类型")
    private Integer accountType;

    /**
     * 银行代码
     */
    @ApiModelProperty(value = "银行代码", required = true)
    private String bankSwiftCode;

    /**
     * 本地银行编码
     */
    @ApiModelProperty(value = "本地银行编码", required = true)
    private String localBankCode;

    /**
     * 联系地址
     */
    @ApiModelProperty(value = "联系地址", required = true)
    private String contactAddress;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市", required = true)
    private String city;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道", required = true)
    private String street;

    /** 验证码 */
//    @ApiModelProperty(value = "验证码", required = true)
//    @NotNull(message = "验证码")
//    @NotBlank
//    private String msgCode;

}
