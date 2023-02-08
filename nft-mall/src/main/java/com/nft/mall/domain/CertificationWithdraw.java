package com.nft.mall.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 用户提现对象 t_certification_withdraw
 *
 * @author nft
 * @date 2021-06-09
 */
@Data
public class CertificationWithdraw extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long certificationWithdrawId;

    /** 提现用户id */
    @Excel(name = "提现用户id")
    private Long certificationId;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNumber;

    /** 提现地址 */
    @Excel(name = "提现地址")
    private String walletAddress;

    /** 提现账号 */
    @Excel(name = "提现账号")
    private String withdrawAccount;

    /** 提现金额 */
    @Excel(name = "提现金额")
    private BigDecimal withdrawAmount;

    /** 提现方式：1.微信 2.支付宝 3.银行卡 */
    @Excel(name = "提现方式：1.微信 2.支付宝 3.银行卡")
    private Integer withdrawType;

    /** 提现状态：0.提现中 1.提现成功 2.提现失败 */
    @Excel(name = "提现状态：0.提现中 1.提现成功 2.提现失败")
    private Integer withdrawStatus;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("certificationWithdrawId", getCertificationWithdrawId())
                .append("certificationId", getCertificationId())
                .append("orderNumber", getOrderNumber())
                .append("walletAddress", getWalletAddress())
                .append("withdrawAccount", getWithdrawAccount())
                .append("withdrawAmount", getWithdrawAmount())
                .append("withdrawType", getWithdrawType())
                .append("withdrawStatus", getWithdrawStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}