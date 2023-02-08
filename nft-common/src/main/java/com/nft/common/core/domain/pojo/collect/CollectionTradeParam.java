package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 藏品交易信息
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class CollectionTradeParam {

    /** 藏品交易ID */
    @ApiModelProperty(value = "藏品交易ID", required = true)
    @NotNull(message = "藏品交易ID不能为空")
    private Long productTradeId;

    /** 藏品ID */
    @ApiModelProperty(value = "藏品交易状态", required = true)
    @NotNull(message = "藏品交易状态不能为空")
    private Integer status;

}
