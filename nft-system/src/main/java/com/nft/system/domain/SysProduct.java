package com.nft.system.domain;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 藏品对象 t_product
 *
 * @author nft
 * @date 2021-07-25
 */
@Data
public class SysProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long productId;

    /**
     * 分类ID
     */
    private Long productCategoryId;

    /**
     * 藏品分类
     */
    @Excel(name = "藏品分类")
    private String productCategoryName;

    /**
     * 藏品单价
     */
    @Excel(name = "藏品单价")
    private BigDecimal price;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件封面图
     */
    private String coverImage;

    /**
     * 藏品名称
     */
    @Excel(name = "藏品名称")
    private String productName;

    /**
     * 藏品作者
     */
    @Excel(name = "藏品作者")
    private String productAuthor;

    /**
     * 藏品图片
     */
    private String productImage;

    /**
     * 藏品介绍
     */
    @Excel(name = "藏品介绍")
    private String productDesc;

    /**
     * 服务费率
     */
    @Excel(name = "服务费率")
    private BigDecimal serviceRate;

    /**
     * 版税
     */
    @Excel(name = "版税")
    private BigDecimal copyrightRate;

    /**
     * 点赞数量
     */
    private Integer favoriteCount;

    /**
     * 收藏数量
     */
    private Integer collectionCount;

    /**
     * 藏品类型 1.普通文件 2.音频 3.视频
     */
    @Excel(name = "藏品类型", readConverterExp = "1=普通文件,2=音频,3=视频")
    private Integer productType;

    /**
     * 藏品状态：0.上架 1.In Transaction  2.已交易 3.Has Been Removed
     */
    @Excel(name = "藏品状态", readConverterExp = "0=上架,1=In Transaction,2=已交易,3=Has Been Removed")
    private Integer status;

    /**
     * 创建人id
     */
    private Long createId;

    /**
     * 父级藏品ID
     */
    private Long parentProductId;

    /**
     * 源藏品ID
     */
    private Long sourceProductId;

    /**
     * 状态集合
     */
    private Set<Integer> excludeStatusSet;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("productId", getProductId())
                .append("productCategoryId", getProductCategoryId())
                .append("productCategoryName", getProductCategoryName())
                .append("price", getPrice())
                .append("fileName", getFileName())
                .append("fileSuffix", getFileSuffix())
                .append("coverImage", getCoverImage())
                .append("productName", getProductName())
                .append("productAuthor", getProductAuthor())
                .append("productImage", getProductImage())
                .append("productDesc", getProductDesc())
                .append("serviceRate", getServiceRate())
                .append("copyrightRate", getCopyrightRate())
                .append("favoriteCount", getFavoriteCount())
                .append("collectionCount", getCollectionCount())
                .append("productType", getProductType())
                .append("status", getStatus())
                .append("createId", getCreateId())
                .append("parentProductId", getParentProductId())
                .append("sourceProductId", getSourceProductId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}