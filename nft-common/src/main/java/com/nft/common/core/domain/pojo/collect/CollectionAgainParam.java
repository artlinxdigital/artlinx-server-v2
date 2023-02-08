package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商城藏品再次上架信息
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class CollectionAgainParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long createId;

    /** 藏品ID */
    @ApiModelProperty(value = "藏品ID", required = true)
    @NotNull(message = "藏品ID不能为空")
    private Long productId;

    /** 收藏夹ID */
    @ApiModelProperty(value = "收藏夹ID", required = true)
    @NotNull(message = "收藏夹ID不能为空")
    private Long collectDirId;

    /** Token ID */
    @ApiModelProperty(value = "Token ID", required = true)
    @NotNull(message = "Token ID不能为空")
    private String tokenId;

    /** 藏品单价 */
    @ApiModelProperty(value = "藏品单价", required = true)
    @NotNull(message = "藏品单价不能为空")
    private BigDecimal price;

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
