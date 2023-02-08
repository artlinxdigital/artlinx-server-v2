package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 藏品操作参数
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class CollectionOpeParam {

    /** 藏品ID */
    @ApiModelProperty(value = "藏品ID", required = true)
    @NotNull(message = "藏品ID不能为空")
    private Long productId;

    /** 操作者id */
    @ApiModelProperty(value = "操作者ID", required = true)
    @NotNull(message = "操作者ID不能为空")
    private Long certificationId;

    /** 操作类型：1.点赞 2.收藏 */
    @ApiModelProperty(value = "操作类型", required = true)
    @NotNull(message = "操作类型不能为空")
    private Integer opeType;

}
