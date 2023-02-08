package com.nft.mall.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 藏品分类对象 t_product_category
 *
 * @author nft
 * @date 2021-05-17
 */
@Data
public class ProductCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long productCategoryId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 分类状态：0.Normal 1.停用 */
    @Excel(name = "分类状态：0.Normal 1.停用")
    private Integer status;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("productCategoryId", getProductCategoryId())
                .append("categoryName", getCategoryName())
                .append("status", getStatus())
                .append("createId", getCreateId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
