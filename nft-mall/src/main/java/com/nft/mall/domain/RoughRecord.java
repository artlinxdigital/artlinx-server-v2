package com.nft.mall.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 原石记录对象 t_rough_record
 *
 * @author nft
 * @date 2021-07-01
 */
@Data
public class RoughRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long roughRecordId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long certificationId;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 原石数量 */
    @Excel(name = "原石数量")
    private BigDecimal amount;

    /** 交易哈希 */
    @Excel(name = "交易哈希")
    private String hash;

    /** 奖励来源：1.推荐 2.交易 */
    @Excel(name = "奖励来源：1.推荐 2.交易")
    private Integer rewardFrom;

    /** 奖励状态：0.奖励中 1.奖励成功 2.奖励失败 */
    @Excel(name = "奖励状态：0.奖励中 1.奖励成功 2.奖励失败")
    private Integer status;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("roughRecordId", getRoughRecordId())
                .append("certificationId", getCertificationId())
                .append("address", getAddress())
                .append("amount", getAmount())
                .append("hash", getHash())
                .append("rewardFrom", getRewardFrom())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
