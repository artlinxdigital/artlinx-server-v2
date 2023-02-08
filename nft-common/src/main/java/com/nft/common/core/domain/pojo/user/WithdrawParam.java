package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 用户提现申请参数
 *
 * @author nft
 * @date 2021-06-09
 */
@Data
public class WithdrawParam {

    /** 提现用户id */
    @ApiModelProperty(value = "提现用户id", required = true)
    @NotNull(message = "提现用户id不能为空")
    private Long certificationId;

    /** 提现地址 */
    @ApiModelProperty(value = "提现地址", required = true)
    @NotNull(message = "提现地址不能为空")
    @NotBlank
    private String walletAddress;

    /** 提现金额 */
    @ApiModelProperty(value = "提现金额", required = true)
    @NotNull(message = "提现金额不能为空")
    private BigDecimal withdrawAmount;

    /** 提现方式：1.微信 2.支付宝 3.银行卡 */
    @ApiModelProperty(value = "提现方式", required = true)
    @NotNull(message = "提现方式不能为空")
    private Integer withdrawType;

    /** 提现配置id */
    @ApiModelProperty(value = "提现配置id", required = true)
    @NotNull(message = "提现配置id不能为空")
    private Long withdrawConfigId;

    /** 手续费 */
    @ApiModelProperty(value = "手续费", required = true)
    @NotNull(message = "手续费不能为空")
    private BigDecimal fee;

}
