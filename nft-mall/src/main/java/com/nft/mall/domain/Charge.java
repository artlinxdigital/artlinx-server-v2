package com.nft.mall.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 充值对象 t_charge
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class Charge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal money;

    /** 充值方式：1.微信 2.支付宝 */
    @Excel(name = "充值方式：1.微信 2.支付宝")
    private Integer type;

    /** 充值人ID */
    @Excel(name = "充值人ID")
    private Long certificationId;

    /** 充值人地址 */
    @Excel(name = "充值人地址")
    private String address;

    /** 充值状态：0.充值中 1.充值成功 2.充值失败 */
    @Excel(name = "充值状态：0.充值中 1.充值成功 2.充值失败")
    private Integer status;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("money", getMoney())
                .append("type", getType())
                .append("certificationId", getCertificationId())
                .append("address", getAddress())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}