package com.nft.system.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 入账记录对象 t_charge
 *
 * @author nft
 * @date 2021-07-25
 */
@Data
public class SysCharge extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "订单编号")
    private String id;

    /**
     * 入账金额
     */
    @Excel(name = "入账金额")
    private BigDecimal money;

    /**
     * 入账方式：1.微信 2.支付宝 3.交易 4.提成 5.版税
     */
    @Excel(name = "入账方式", readConverterExp = "1=微信,2=支付宝,3=交易,4=提成,5=版税")
    private Integer type;

    /**
     * 充值人ID
     */
    private Long certificationId;

    /**
     * 入账会员
     */
    @Excel(name = "入账会员")
    private String realName;

    /**
     * 入账地址
     */
    @Excel(name = "入账地址", width = 50)
    private String address;

    /**
     * 入账状态：0.入账中 1.成功 2.失败
     */
    @Excel(name = "入账状态", readConverterExp = "0=入账中,1=成功,2=失败")
    private Integer status;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("money", getMoney())
                .append("type", getType())
                .append("certificationId", getCertificationId())
                .append("realName", getRealName())
                .append("address", getAddress())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}