package com.nft.mall.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 【请填写功能名称】对象 nft_token_id
 * 
 * @author nft
 * @date 2021-06-10
 */
public class NftTokenId extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键




token + tokenid + owner + buyToken + buyTokenId +  salt +  */
    private String id;

    /** 收藏夹地址 */
    @Excel(name = "收藏夹地址")
    private String token;

    /** tokenId */
    @Excel(name = "tokenId")
    private Long tokenId;

    /** $column.columnComment */
    @Excel(name = "tokenId")
    private String assettype;

    /** tokenId持有者地址 */
    @Excel(name = "tokenId持有者地址")
    private String owner;

    /** 随机数 */
    @Excel(name = "随机数")
    private String saltValue;

    /** 随机数类型 */
    @Excel(name = "随机数类型")
    private String saltType;

    /** 购买金额 */
    @Excel(name = "购买金额")
    private Integer buyValue;

    /** 购买的收藏夹地址 */
    @Excel(name = "购买的收藏夹地址")
    private String buyToken;

    /** 购买的tokenId */
    @Excel(name = "购买的tokenId")
    private String buyTokenId;

    /** 币种类型 */
    @Excel(name = "币种类型")
    private String buyAssetType;

    /** $column.columnComment */
    @Excel(name = "币种类型")
    private String value;

    /** 卖家 Fix price 签名值 */
    @Excel(name = "卖家 Fix price 签名值")
    private String signature;

    /** 卖家 Fix price 签名值 */
    @Excel(name = "卖家 Fix price 签名值")
    private String signatureV;

    /** 卖家 Fix price 签名值 */
    @Excel(name = "卖家 Fix price 签名值")
    private String signatureS;

    /** 卖家 Fix price 签名值 */
    @Excel(name = "卖家 Fix price 签名值")
    private String signatureR;

    /** $column.columnComment */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "卖家 Fix price 签名值", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    /** $column.columnComment */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "卖家 Fix price 签名值", width = 30, dateFormat = "yyyy-MM-dd")
    private Date importantUpdateDate;

    /** $column.columnComment */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "卖家 Fix price 签名值", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateStateDate;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private Long contractVersion;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private String fee;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private Long sold;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private Long canceld;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private Long buyPriceEth;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private Long version;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private String buyPrice;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private Integer active;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private Long buyStock;

    /** $column.columnComment */
    @Excel(name = "卖家 Fix price 签名值")
    private String sellPrice;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setToken(String token) 
    {
        this.token = token;
    }

    public String getToken() 
    {
        return token;
    }
    public void setTokenId(Long tokenId) 
    {
        this.tokenId = tokenId;
    }

    public Long getTokenId() 
    {
        return tokenId;
    }
    public void setAssettype(String assettype) 
    {
        this.assettype = assettype;
    }

    public String getAssettype() 
    {
        return assettype;
    }
    public void setOwner(String owner) 
    {
        this.owner = owner;
    }

    public String getOwner() 
    {
        return owner;
    }
    public void setSaltValue(String saltValue) 
    {
        this.saltValue = saltValue;
    }

    public String getSaltValue() 
    {
        return saltValue;
    }
    public void setSaltType(String saltType) 
    {
        this.saltType = saltType;
    }

    public String getSaltType() 
    {
        return saltType;
    }
    public void setBuyValue(Integer buyValue) 
    {
        this.buyValue = buyValue;
    }

    public Integer getBuyValue() 
    {
        return buyValue;
    }
    public void setBuyToken(String buyToken) 
    {
        this.buyToken = buyToken;
    }

    public String getBuyToken() 
    {
        return buyToken;
    }
    public void setBuyTokenId(String buyTokenId) 
    {
        this.buyTokenId = buyTokenId;
    }

    public String getBuyTokenId() 
    {
        return buyTokenId;
    }
    public void setBuyAssetType(String buyAssetType) 
    {
        this.buyAssetType = buyAssetType;
    }

    public String getBuyAssetType() 
    {
        return buyAssetType;
    }
    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
    public void setSignature(String signature) 
    {
        this.signature = signature;
    }

    public String getSignature() 
    {
        return signature;
    }
    public void setSignatureV(String signatureV) 
    {
        this.signatureV = signatureV;
    }

    public String getSignatureV() 
    {
        return signatureV;
    }
    public void setSignatureS(String signatureS) 
    {
        this.signatureS = signatureS;
    }

    public String getSignatureS() 
    {
        return signatureS;
    }
    public void setSignatureR(String signatureR) 
    {
        this.signatureR = signatureR;
    }

    public String getSignatureR() 
    {
        return signatureR;
    }
    public void setUpdateDate(Date updateDate) 
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() 
    {
        return updateDate;
    }
    public void setImportantUpdateDate(Date importantUpdateDate) 
    {
        this.importantUpdateDate = importantUpdateDate;
    }

    public Date getImportantUpdateDate() 
    {
        return importantUpdateDate;
    }
    public void setUpdateStateDate(Date updateStateDate) 
    {
        this.updateStateDate = updateStateDate;
    }

    public Date getUpdateStateDate() 
    {
        return updateStateDate;
    }
    public void setContractVersion(Long contractVersion) 
    {
        this.contractVersion = contractVersion;
    }

    public Long getContractVersion()
    {
        return contractVersion;
    }
    public void setFee(String fee)
    {
        this.fee = fee;
    }

    public String getFee()
    {
        return fee;
    }
    public void setSold(Long sold) 
    {
        this.sold = sold;
    }

    public Long getSold() 
    {
        return sold;
    }
    public void setCanceld(Long canceld) 
    {
        this.canceld = canceld;
    }

    public Long getCanceld() 
    {
        return canceld;
    }
    public void setBuyPriceEth(Long buyPriceEth) 
    {
        this.buyPriceEth = buyPriceEth;
    }

    public Long getBuyPriceEth() 
    {
        return buyPriceEth;
    }
    public void setVersion(Long version) 
    {
        this.version = version;
    }

    public Long getVersion() 
    {
        return version;
    }
    public void setBuyPrice(String buyPrice)
    {
        this.buyPrice = buyPrice;
    }

    public String getBuyPrice()
    {
        return buyPrice;
    }
    public void setActive(Integer active) 
    {
        this.active = active;
    }

    public Integer getActive() 
    {
        return active;
    }
    public void setBuyStock(Long buyStock) 
    {
        this.buyStock = buyStock;
    }

    public Long getBuyStock() 
    {
        return buyStock;
    }
    public void setSellPrice(String sellPrice)
    {
        this.sellPrice = sellPrice;
    }

    public String getSellPrice()
    {
        return sellPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("token", getToken())
            .append("tokenId", getTokenId())
            .append("assettype", getAssettype())
            .append("owner", getOwner())
            .append("saltValue", getSaltValue())
            .append("saltType", getSaltType())
            .append("buyValue", getBuyValue())
            .append("buyToken", getBuyToken())
            .append("buyTokenId", getBuyTokenId())
            .append("buyAssetType", getBuyAssetType())
            .append("value", getValue())
            .append("signature", getSignature())
            .append("signatureV", getSignatureV())
            .append("signatureS", getSignatureS())
            .append("signatureR", getSignatureR())
            .append("updateDate", getUpdateDate())
            .append("importantUpdateDate", getImportantUpdateDate())
            .append("updateStateDate", getUpdateStateDate())
            .append("contractVersion", getContractVersion())
            .append("fee", getFee())
            .append("sold", getSold())
            .append("canceld", getCanceld())
            .append("buyPriceEth", getBuyPriceEth())
            .append("version", getVersion())
            .append("buyPrice", getBuyPrice())
            .append("active", getActive())
            .append("buyStock", getBuyStock())
            .append("sellPrice", getSellPrice())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
