package com.nft.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 钱支出收入记录对象 t_money_history
 *
 * @author nft
 * @date 2021-11-04
 */
@Data
public class SysMoneyHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 充值金额
     */
    @Excel(name = "充值金额")
    private BigDecimal money;

    /**
     * 类型：1.买入支付 2.卖出收入 3.卖出手续费 4.提现
     */
    @Excel(name = "类型：1.买入支付 2.卖出收入 3.卖出手续费 4.提现")
    private Integer type;

    /**
     * 充值人ID
     */
    @Excel(name = "充值人ID")
    private Long certificationId;

    /**
     * 充值人地址
     */
    @Excel(name = "充值人地址")
    private String address;

    /**
     * 状态：1.进行中，2已完成 3.已取消
     */
    @Excel(name = "状态：1.进行中，2已完成 3.已取消")
    private Integer status;

    /**
     * $column.columnComment
     */
    @Excel(name = "状态：1.进行中，2已完成 3.已取消")
    private Long tradeId;

    /**
     * 支付到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付到期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payExpireTime;

    /**
     * 交易会员
     */
    private String userName;

    /**
     * 交易账号
     */
    private String account;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("money", getMoney())
                .append("type", getType())
                .append("certificationId", getCertificationId())
                .append("address", getAddress())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("tradeId", getTradeId())
                .append("payExpireTime", getPayExpireTime())
                .toString();
    }
}