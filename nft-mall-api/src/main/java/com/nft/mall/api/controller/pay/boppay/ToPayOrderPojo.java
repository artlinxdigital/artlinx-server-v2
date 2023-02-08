package com.nft.mall.api.controller.pay.boppay;

import com.nft.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 本系统中的支付订单
 */
@Data
public class ToPayOrderPojo  implements Serializable {

    /**
     * 用户id
     */
    private Long certificationId;

    /**
     * 藏品id
     */
    private Long productId;

    @Excel(name = "交易状态：0.待支付 1.In Transaction  2.Successful Transaction  3.交易失败")
    private Integer status;

    @Excel(name = "藏品单价")
    private BigDecimal price;

    /** 交易金额 */
    @Excel(name = "交易金额")
    private BigDecimal tradeAmount;

    /** 交易费用 */
    @Excel(name = "交易费用")
    private BigDecimal tradeFee;

    /** 服务费率 */
    @Excel(name = "服务费率")
    private BigDecimal serviceRate;

    /** 版税 */
    @Excel(name = "版税")
    private BigDecimal copyrightRate;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createId;

    /**
     * 卖家
     */
    private Long formId;

    /** 购买人 */
    private Long toId;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** Token ID */
    @Excel(name = "Token ID")
    private Long tokenId;

}
