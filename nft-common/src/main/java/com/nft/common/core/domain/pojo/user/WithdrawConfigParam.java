package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户提现配置参数
 *
 * @author nft
 * @date 2021-06-09
 */
@Data
public class WithdrawConfigParam {

    /** 提现用户id */
    @ApiModelProperty(value = "提现用户id", required = true)
    @NotNull(message = "提现用户id不能为空")
    private Long certificationId;

    /** 提现地址 */
    @ApiModelProperty(value = "提现地址", required = true)
    @NotNull(message = "提现地址不能为空")
    @NotBlank
    private String walletAddress;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", required = true)
    @NotNull(message = "手机号不能为空")
    @NotBlank
    private String mobile;

    /** 账号 */
    @ApiModelProperty(value = "账号", required = true)
    @NotNull(message = "账号不能为空")
    @NotBlank
    private String account;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;

    /** 身份证号 */
    @ApiModelProperty(value = "身份证号", required = true)
    private String idNumber;

    /** 开户行 */
    @ApiModelProperty(value = "开户行", required = true)
    @NotNull(message = "开户行不能为空")
    @NotBlank
    private String accountBank;

    /** 开户支行 */
    @ApiModelProperty(value = "开户支行", required = true)
    @NotNull(message = "开户支行不能为空")
    @NotBlank
    private String accountSubBank;

    /** 银行卡号 */
    @ApiModelProperty(value = "银行卡号", required = true)
    @NotNull(message = "银行卡号不能为空")
    @NotBlank
    private String bankCard;

    /** 提现方式：1.微信 2.支付宝 3.银行卡 */
    @ApiModelProperty(value = "提现方式", required = true)
    @NotNull(message = "提现方式不能为空")
    private Integer configType;

    /** 验证码 */
    @ApiModelProperty(value = "验证码", required = true)
    @NotNull(message = "验证码")
    @NotBlank
    private String msgCode;

}
