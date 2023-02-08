package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户提现配置信息
 *
 * @author nft
 * @date 2021-06-09
 */
@Data
public class WithdrawConfigInfo {

    /** 主键ID */
    private Long withdrawConfigId;

    /** 提现用户id */
    @ApiModelProperty(value = "提现用户id", required = true)
    private Long certificationId;

    /** 提现地址 */
    @ApiModelProperty(value = "提现地址", required = true)
    private String walletAddress;

    /** 手机号 */
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /** 账号 */
    @ApiModelProperty(value = "账号", required = true)
    private String account;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;

    /** 身份证号 */
    @ApiModelProperty(value = "身份证号", required = true)
    private String idNumber;

    /** 开户行 */
    @ApiModelProperty(value = "开户行", required = true)
    private String accountBank;

    /** 开户支行 */
    @ApiModelProperty(value = "开户支行", required = true)
    private String accountSubBank;

    /** 银行卡号 */
    @ApiModelProperty(value = "银行卡号", required = true)
    private String bankCard;

    /** 提现方式：1.微信 2.支付宝 3.银行卡 */
    @ApiModelProperty(value = "提现方式", required = true)
    private Integer configType;

    /** 银行类型 */
    @ApiModelProperty(value = "银行类型", required = true)
    private String bankType;

    /** 银行样式 */
    @ApiModelProperty(value = "银行样式", required = true)
    private String bankStyle;

}
