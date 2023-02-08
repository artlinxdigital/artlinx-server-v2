package com.nft.common.core.domain.pojo.collect;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 藏品交易返回信息
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class CollectionTradeInfo {

    /** 主键ID */
    private Long productTradeId;

    /** 藏品ID */
    private Long productId;

    /** 分类ID */
    private Long categoryId;

    /** Token ID */
    private Long tokenId;

    /** 藏品单价 */
    private BigDecimal price;

    /** 藏品作者 */
    private Long fromId;

    /** 藏品拥有者 */
    private Long toId;

    /** 藏品作者 */
    private String fromAddress;

    /** 藏品拥有者 */
    private String toAddress;

    /** 合约地址 */
    private String contractAddress;

    /** 交易金额 */
    private BigDecimal tradeAmount;

    /** 交易费用 */
    private BigDecimal tradeFee;

    /** 服务费率 */
    private BigDecimal serviceRate;

    /** 版税 */
    private BigDecimal copyrightRate;

    /** 藏品类型：1.普通文件 2.音频 3.视频 */
    private Integer productType;

    /** 交易类型：1.Buy 2.Sell */
    private Integer tradeType;

    /** 交易类型 */
    private String tradeTypeDesc;

    /** 交易状态：0.待支付 1.In Transaction  2.Successful Transaction  3.交易失败 */
    private Integer status;

    /** 交易状态 */
    private String tradeStatusDesc;

    /** 藏品名称 */
    private String productName;

    /** 封面图片 */
    private String coverImage;

    /** 藏品图片 */
    private String productImage;

    /** 分类名称 */
    private String categoryName;

    /** 交易时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** ETH,ERC20,ERC721,ERC1155 */
    private String coinType;

    /** 卖家fixprice签名 */
    private String signatureLeft;

    /** 卖家订单信息(json) */
    private String orderLeft;

    /** 备注 */
    private String remark;


}
