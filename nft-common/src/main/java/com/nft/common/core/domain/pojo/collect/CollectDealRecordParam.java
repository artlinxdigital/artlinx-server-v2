package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 藏品交易记录参数
 *
 * @author nft
 * @date 2021-06-15
 */
@Data
public class CollectDealRecordParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long certificationId;

    /** 藏品 ID */
    @ApiModelProperty(value = "藏品ID", required = true)
    @NotNull(message = "藏品ID不能为空")
    private Long productId;

    /** Token ID */
    @ApiModelProperty(value = "Token ID", required = true)
    @NotNull(message = "Token ID不能为空")
    private String tokenId;

    /** 合约地址 */
    @ApiModelProperty(value = "合约地址", required = true)
    @NotNull(message = "合约地址不能为空")
    @NotBlank
    private String contractAddress;
}
