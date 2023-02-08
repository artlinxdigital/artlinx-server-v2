package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户提现信息
 *
 * @author nft
 * @date 2021-06-09
 */
@Data
public class WithdrawInfo {

    /** 主键ID */
    @ApiModelProperty(value = "主键ID", required = true)
    private Long certificationWithdrawId;

    /** 订单编号 */
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNumber;

    /** 提现地址 */
    @ApiModelProperty(value = "提现地址", required = true)
    private String walletAddress;

    /** 提现账号 */
    @ApiModelProperty(value = "提现账号", required = true)
    private String withdrawAccount;

    /** 提现金额 */
    @ApiModelProperty(value = "提现金额", required = true)
    private BigDecimal withdrawAmount;

    /** 提现方式：1.微信 2.支付宝 3.银行卡 */
    @ApiModelProperty(value = "提现方式", required = true)
    private Integer withdrawType;

    /** 出账描述：0.提现中 1.提现成功 2.提现失败 */
    @ApiModelProperty(value = "提现状态", required = true)
    private String typeDesc;

    /** 出账状态：0.提现中 1.提现成功 2.提现失败 */
    @ApiModelProperty(value = "提现状态", required = true)
    private Integer withdrawStatus;

    /** 出账描述：0.提现中 1.提现成功 2.提现失败 */
    @ApiModelProperty(value = "提现状态", required = true)
    private String statusDesc;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", required = true)
    private Date createTime;

    /** 出账时间 */
    @ApiModelProperty(value = "出账时间", required = true)
    private String withdrawTime;

    /** 出账目标 */
    @ApiModelProperty(value = "出账目标", required = true)
    private String withdrawGoal;

}
