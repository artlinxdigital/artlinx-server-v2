package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 充值参数
 *
 * @author nft
 * @date 2021-05-19
 */
@Data
public class ChargeParam {

    /** 充值金额 */
    @ApiModelProperty(value = "充值金额", required = true)
    @NotNull(message = "充值金额不能为空")
    private BigDecimal money;

    /** 充值方式：1.微信 2.支付宝 */
    @ApiModelProperty(value = "充值方式", required = true)
    @NotNull(message = "充值方式不能为空")
    private Integer type;

    /** 充值人地址 */
    @ApiModelProperty(value = "充值地址", required = true)
    @NotNull(message = "充值地址不能为空")
    @NotBlank
    private String address;

}
