package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 支付检查参数
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class PayCheckParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long certificationId;

    /** 藏品ID */
    @ApiModelProperty(value = "藏品ID", required = true)
    @NotNull(message = "藏品ID不能为空")
    private Long productId;

}
