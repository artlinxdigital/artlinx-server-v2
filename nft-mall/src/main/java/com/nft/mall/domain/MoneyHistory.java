package com.nft.mall.domain;

import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 金钱进出对象 t_charge
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class MoneyHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal money;


    @Excel(name = "类型：1.买入支付 2.卖出收入 3.卖出手续费 4.提现")
    private Integer type;

    /** 充值人ID */
    @Excel(name = "充值人ID")
    private Long certificationId;

    /** 充值人地址 */
    @Excel(name = "充值人地址")
    private String address;


    @Excel(name = "状态：1.进行中，2已完成 3.已取消")
    private Integer status;

    private Long tradeId;

    /**
     * 支付到期时间
     */
    private Date payExpireTime;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 交易类型描述
     */
    private String tradeTypeDesc;

    /**
     * 交易备注
     */
    private String orderRemark;

}
