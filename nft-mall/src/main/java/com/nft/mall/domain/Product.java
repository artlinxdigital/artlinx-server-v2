package com.nft.mall.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 藏品对象 t_product
 *
 * @author nft
 * @date 2021-05-18
 */
@Data
public class Product extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long productId;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long productCategoryId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件后缀 */
    @Excel(name = "文件后缀")
    private String fileSuffix;

    /** 文件封面图 */
    @Excel(name = "文件封面图")
    private String coverImage;

    /** 藏品单价 */
    @Excel(name = "藏品单价")
    private BigDecimal price;

    /** 藏品名称 */
    @Excel(name = "藏品名称")
    private String productName;

    /** 藏品作者 */
    @Excel(name = "藏品作者")
    private String productAuthor;

    /** 藏品作者简介 */
    @Excel(name = "藏品作者简介")
    private String productAuthorDesc;

    /** 藏品图片 */
    @Excel(name = "藏品图片")
    private String productImage;

    /** 藏品介绍 */
    @Excel(name = "藏品介绍")
    private String productDesc;

    /** 服务费率 */
    @Excel(name = "服务费率")
    private BigDecimal serviceRate;

    /** 版税 */
    @Excel(name = "版税")
    private BigDecimal copyrightRate;

    /** 点赞数量 */
    @Excel(name = "点赞数量")
    private Integer favoriteCount;

    /** 收藏数量 */
    @Excel(name = "收藏数量")
    private Integer collectionCount;

    /** 藏品类型：1.普通文件 2.音频 3.视频 */
    @Excel(name = "藏品类型：1.普通文件 2.音频 3.视频")
    private Integer productType;

    /** 藏品来源：1.铸造 2.Buy 3.导入 */
    @Excel(name = "藏品来源：1.铸造 2.Buy 3.导入")
    private Integer productFrom;

    /** 藏品状态：0.Normal 1.In Transaction  2.Successful Transaction  3.Has Been Removed  */
    @Excel(name = "藏品状态：0.Normal 1.In Transaction  2.Successful Transaction  3.Has Been Removed")
    private Integer status;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createId;

    /** 父藏品ID */
    @Excel(name = "父藏品ID")
    private Long parentProductId;

    /** 源藏品ID */
    @Excel(name = "源藏品ID")
    private Long sourceProductId;

    /** ETH,ERC20,ERC721,ERC1155 */
    @Excel(name = "ETH,ERC20,ERC721,ERC1155")
    private String coinType;

    /** 卖家fixprice签名 */
    @Excel(name = "卖家fixprice签名")
    private String signatureLeft;

    /** 卖家订单信息(json) */
    @Excel(name = "卖家订单信息(json)")
    private String orderLeft;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("productId", getProductId())
                .append("productCategoryId", getProductCategoryId())
                .append("fileName", getFileName())
                .append("fileSuffix", getFileSuffix())
                .append("coverImage", getCoverImage())
                .append("price", getPrice())
                .append("productName", getProductName())
                .append("productAuthorDesc", getProductAuthorDesc())
                .append("productAuthor", getProductAuthor())
                .append("productImage", getProductImage())
                .append("productDesc", getProductDesc())
                .append("serviceRate", getServiceRate())
                .append("copyrightRate", getCopyrightRate())
                .append("favoriteCount", getFavoriteCount())
                .append("collectionCount", getCollectionCount())
                .append("productType", getProductType())
                .append("productFrom", getProductFrom())
                .append("status", getStatus())
                .append("createId", getCreateId())
                .append("parentProductId", getParentProductId())
                .append("sourceProductId", getSourceProductId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
