package com.nft.mall.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 藏品Token对象 t_product_token
 *
 * @author nft
 * @date 2021-06-06
 */
@Data
public class ProductToken extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long productTokenId;

    /** 藏品ID */
    @Excel(name = "藏品ID")
    private Long productId;

    /** tokenId */
    @Excel(name = "tokenId")
    private String tokenId;

    /** 收藏夹ID */
    @Excel(name = "收藏夹ID")
    private Long collectDirId;

    /** 手续费 */
    @ApiModelProperty(value = "手续费", required = true)
    @NotNull(message = "手续费不能为空")
    private BigDecimal fee;

    /** 价格 */
    private BigDecimal price;

    /** 交易哈希 */
    @Excel(name = "交易哈希")
    private String tradeHash;

    /** 操作类型：1.铸造 2.上架 3.下架 4.交易 5.导入 */
    private Integer opeType;

    /** 代币状态：0.Normal 1.下架 */
    @Excel(name = "代币状态：0.Normal 1.下架")
    private Integer status;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createId;

    /** ETH,ERC20,ERC721,ERC1155 */
    @Excel(name = "ETH,ERC20,ERC721,ERC1155")
    private String coinType;

    /** 卖家fixprice签名 */
    @Excel(name = "卖家fixprice签名")
    private String signatureLeft;

    /** 卖家订单信息(json) */
    @Excel(name = "卖家订单信息(json)")
    private String orderLeft;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("productTokenId", getProductTokenId())
                .append("productId", getProductId())
                .append("tokenId", getTokenId())
                .append("collectDirId", getCollectDirId())
                .append("fee", getFee())
                .append("tradeHash", getTradeHash())
                .append("status", getStatus())
                .append("createId", getCreateId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("coinType", getCoinType())
                .append("signatureLeft", getSignatureLeft())
                .append("orderLeft", getOrderLeft())
                .append("remark", getRemark())
                .toString();
    }
}