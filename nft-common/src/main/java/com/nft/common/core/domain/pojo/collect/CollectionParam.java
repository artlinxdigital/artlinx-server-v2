package com.nft.common.core.domain.pojo.collect;

import com.nft.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商城藏品信息
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class CollectionParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long createId;

    /** 收藏夹ID */
    @ApiModelProperty(value = "收藏夹ID", required = true)
    @NotNull(message = "收藏夹ID不能为空")
    private Long collectDirId;

    /** Token ID */
    @ApiModelProperty(value = "Token ID", required = true)
    @NotNull(message = "Token ID不能为空")
    private String tokenId;

    /** 分类ID */
    @ApiModelProperty(value = "分类ID", required = true)
    @NotNull(message = "分类ID不能为空")
    private Long productCategoryId;

    /** 文件名称 */
    @ApiModelProperty(value = "文件名称", required = true)
    @NotNull(message = "文件名称不能为空")
    @NotBlank
    private String fileName;

    /** 文件后缀 */
    @ApiModelProperty(value = "文件后缀", required = true)
    @NotNull(message = "文件后缀不能为空")
    @NotBlank
    private String fileSuffix;

    /** 文件封面 */
    @ApiModelProperty(value = "文件封面", required = true)
    @NotNull(message = "文件封面不能为空")
    @NotBlank
    private String coverImage;

    /** 藏品单价 */
    @ApiModelProperty(value = "藏品单价", required = true)
    @NotNull(message = "藏品单价不能为空")
    private BigDecimal price;

    /** 藏品名称 */
    @ApiModelProperty(value = "藏品名称", required = true)
    @NotNull(message = "藏品名称不能为空")
    @NotBlank
    private String productName;

    /** 藏品作者 */
    @ApiModelProperty(value = "藏品作者", required = true)
    private String productAuthor;

    /** 藏品作者介绍 */
    @ApiModelProperty(value = "藏品作者介绍", required = true)
    private String productAuthorDesc;

    /** 藏品图片 */
    @ApiModelProperty(value = "藏品图片", required = true)
    @NotNull(message = "藏品图片不能为空")
    @NotBlank
    private String productImage;

    /** 藏品介绍 */
    @ApiModelProperty(value = "藏品介绍", required = true)
    @NotNull(message = "藏品介绍不能为空")
    @NotBlank
    private String productDesc;

    /** 藏品类型 */
    @ApiModelProperty(value = "藏品类型", required = true)
    @NotNull(message = "藏品介绍不能为空")
    private Integer productType;

    /** 服务费率 */
    @ApiModelProperty(value = "服务费率", required = true)
    @NotNull(message = "服务费率不能为空")
    private BigDecimal serviceRate;

    /** 版税 */
    @ApiModelProperty(value = "版税", required = true)
    @NotNull(message = "版税不能为空")
    private BigDecimal copyrightRate;

    /** 交易哈希 */
    @ApiModelProperty(value = "交易哈希", required = true)
    @NotNull(message = "交易哈希不能为空")
    @NotBlank
    private String tradeHash;

    /** 手续费 */
    @ApiModelProperty(value = "手续费", required = true)
    @NotNull(message = "手续费不能为空")
    private BigDecimal fee;

    /** ETH,ERC20,ERC721,ERC1155 */
    @ApiModelProperty(value = "ETH,ERC20,ERC721,ERC1155", required = true)
    @NotNull(message = "ETH,ERC20,ERC721,ERC1155不能为空")
    private String coinType;

    /** 卖家fixprice签名 */
    @ApiModelProperty(value = "卖家fixprice签名", required = true)
    @NotNull(message = "卖家fixprice签名不能为空")
    private String signatureLeft;

    /** 卖家订单信息(json) */
    @ApiModelProperty(value = "卖家订单信息(JSON)", required = true)
    @NotNull(message = "卖家订单信息(json)不能为空")
    private String orderLeft;

    /** 备注 */
    @ApiModelProperty(value = "备注", required = false)
    private String remark;

}
