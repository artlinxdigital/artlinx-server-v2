package com.nft.mall.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 藏品操作对象 t_product_ope
 *
 * @author nft
 * @date 2021-05-17
 */
@Data
public class ProductOpe extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long productOpeId;

    /** 藏品ID */
    @Excel(name = "藏品ID")
    private Long productId;

    /** 操作者id */
    @Excel(name = "操作者id")
    private Long certificationId;

    /** 操作类型：1.点赞 1.收藏 */
    @Excel(name = "操作类型：1.点赞 1.收藏")
    private Integer opeType;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("productOpeId", getProductOpeId())
                .append("productId", getProductId())
                .append("certificationId", getCertificationId())
                .append("opeType", getOpeType())
                .append("createTime", getCreateTime())
                .toString();
    }
}
