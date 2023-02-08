package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 收藏夹参数
 *
 * @author nft
 * @date 2021-06-03
 */
@Data
public class CollectionDirParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long certificationId;

    /** 代币名称 */
    @ApiModelProperty(value = "代币名称", required = true)
    @NotNull(message = "代币名称不能为空")
    @NotBlank
    private String tokenName;

    /** 代币符号 */
    @ApiModelProperty(value = "代币符号", required = true)
    @NotNull(message = "代币符号不能为空")
    @NotBlank
    private String tokenSymbol;

    /** 代币图片 */
    @ApiModelProperty(value = "代币图片", required = true)
    @NotNull(message = "代币图片不能为空")
    @NotBlank
    private String tokenImage;

    /** 短链接 */
    @ApiModelProperty(value = "短链接", required = true)
    private String shortUrl;

    /** 合约地址 */
    @ApiModelProperty(value = "合约地址", required = true)
    @NotNull(message = "合约地址不能为空")
    @NotBlank
    private String contractAddress;

    /** 代币简介 */
    @ApiModelProperty(value = "代币简介", required = true)
    private String tokenDesc;

    /** 代币类型 1.ERC-721 2.ERC-1155 */
    @ApiModelProperty(value = "代币类型", required = true)
    @NotNull(message = "代币类型 1.ERC-721 2.ERC-1155")
    private Integer tokenType;

    /** 收藏夹状态：0.创建中 1.创建成功 2.创建失败 */
    @ApiModelProperty(value = "收藏夹状态", required = true)
    private Integer status;

    /** 交易哈希 */
    @ApiModelProperty(value = "交易哈希", required = true)
    @NotNull(message = "交易哈希不能为空")
    @NotBlank
    private String tradeHash;

    /** 手续费 */
    @ApiModelProperty(value = "手续费", required = true)
    @NotNull(message = "手续费不能为空")
    private BigDecimal fee;

}
