package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 藏品下单参数
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class CollectionBuyParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long certificationId;

    /** 藏品ID */
    @ApiModelProperty(value = "藏品ID", required = true)
    @NotNull(message = "藏品ID不能为空")
    private Long productId;

    /** 支付金额 */
    @ApiModelProperty(value = "支付金额", required = true)
    @NotNull(message = "支付金额不能为空")
    private BigDecimal totalAmount;

    /** 支付方式 */
    @ApiModelProperty(value = "支付方式", required = true)
    @NotNull(message = "支付方式不能为空")
    private Integer payType;

    /** 服务费率 */
    @ApiModelProperty(value = "服务费率", required = true)
    @NotNull(message = "服务费率不能为空")
    private BigDecimal serviceRate;

    /** 邀请人费率 */
    @ApiModelProperty(value = "邀请人费率", required = true)
    @NotNull(message = "邀请人费率不能为空")
    private BigDecimal invitorRate;

    /** 合约地址 */
    @ApiModelProperty(value = "合约地址", required = true)
    @NotNull(message = "合约地址不能为空")
    @NotBlank
    private String contractAddress;

    /** 交易哈希 */
    @ApiModelProperty(value = "交易哈希", required = true)
    @NotNull(message = "交易哈希不能为空")
    @NotBlank
    private String tradeHash;

    /** 邀请人地址 */
    @ApiModelProperty(value = "邀请人地址", required = true)
    @NotNull(message = "邀请人地址不能为空")
    @NotBlank
    private String invitorAddress;

    /** 代币地址 */
    @ApiModelProperty(value = "代币地址", required = true)
    @NotNull(message = "代币地址不能为空")
    @NotBlank
    private String tokenAddress;

    /** 手续费 */
    @ApiModelProperty(value = "手续费", required = true)
    @NotNull(message = "手续费不能为空")
    private BigDecimal fee;

    /** 支付来源 */
    private Integer payFrom;

}
