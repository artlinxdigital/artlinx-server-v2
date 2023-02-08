package com.nft.system.domain;

import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 订单对象 t_product_trade
 *
 * @author nft
 * @date 2021-05-18
 */
@Data
public class SysOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long productTradeId;

    /**
     * 藏品ID
     */
    @Excel(name = "藏品ID")
    private Long productId;

    /**
     * 藏品名称
     */
    @Excel(name = "藏品名称")
    private String productName;

    /**
     * 藏品作者
     */
    @Excel(name = "藏品作者")
    private String productAuthor;

    /**
     * 交易创建者(买家)
     */
    @Excel(name = "交易创建者")
    private String creator;

    /**
     * 买家账号
     */
    @Excel(name = "交易账号")
    private String account;

    /**
     * 藏品创建者(卖家)
     */
    @Excel(name = "藏品创建者")
    private String seller;

    /**
     * 卖家账号
     */
    @Excel(name = "卖家账号")
    private String sellerAccount;

    /**
     * 交易卡号
     */
    @Excel(name = "交易卡号")
    private String bankCard;

    /**
     * 文件封面图
     */
    private String coverImage;

    /**
     * 源文件
     */
    private String productImage;

    /**
     * 分类ID
     */
    @Excel(name = "分类ID")
    private Long categoryId;

    /**
     * 藏品分类
     */
    @Excel(name = "藏品分类")
    private String categoryName;

    /**
     * Token ID
     */
    @Excel(name = "Token ID")
    private Long tokenId;

    /**
     * 藏品单价
     */
    @Excel(name = "藏品单价")
    private BigDecimal price;

    /**
     * 藏品作者
     */
    @Excel(name = "藏品作者")
    private Long fromId;

    /**
     * 藏品拥有者
     */
    @Excel(name = "藏品拥有者")
    private Long toId;

    /**
     * 藏品作者
     */
    @Excel(name = "藏品作者")
    private String fromAddress;

    /**
     * 藏品拥有者
     */
    @Excel(name = "藏品拥有者")
    private String toAddress;

    /**
     * 合约地址
     */
    @Excel(name = "合约地址")
    private String contractAddress;

    /**
     * 交易哈希
     */
    @Excel(name = "交易哈希")
    private String tradeHash;

    /**
     * 交易金额
     */
    @Excel(name = "交易金额")
    private BigDecimal tradeAmount;

    /**
     * 交易费用
     */
    @Excel(name = "交易费用")
    private BigDecimal tradeFee;

    /**
     * 服务费率
     */
    @Excel(name = "服务费率")
    private BigDecimal serviceRate;

    /**
     * 版税
     */
    @Excel(name = "版税")
    private BigDecimal copyrightRate;

    /**
     * 交易状态
     */
    @Excel(name = "交易状态：0.待支付 1.In Transaction  2.Successful Transaction  3.交易失败")
    private Integer status;

    /**
     * 创建人id
     */
    @Excel(name = "创建人id")
    private Long createId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("productTradeId", getProductTradeId())
                .append("productId", getProductId())
                .append("productName", getProductName())
                .append("productAuthor", getProductAuthor())
                .append("creator", getCreator())
                .append("account", getAccount())
                .append("bankCard", getBankCard())
                .append("coverImage", getCoverImage())
                .append("categoryId", getCategoryId())
                .append("categoryName", getCategoryName())
                .append("tokenId", getTokenId())
                .append("price", getPrice())
                .append("fromId", getFromId())
                .append("toId", getToId())
                .append("fromAddress", getFromAddress())
                .append("toAddress", getToAddress())
                .append("contractAddress", getContractAddress())
                .append("tradeHash", getTradeHash())
                .append("tradeAmount", getTradeAmount())
                .append("tradeFee", getTradeFee())
                .append("serviceRate", getServiceRate())
                .append("copyrightRate", getCopyrightRate())
                .append("status", getStatus())
                .append("createId", getCreateId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
