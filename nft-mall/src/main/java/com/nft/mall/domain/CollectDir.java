package com.nft.mall.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 用户收藏夹对象 t_collect_dir
 *
 * @author nft
 * @date 2021-06-06
 */
@Data
public class CollectDir extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long collectDirId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long certificationId;

    /** 代币名称 */
    @Excel(name = "代币名称")
    private String tokenName;

    /** 代币符号 */
    @Excel(name = "代币符号")
    private String tokenSymbol;

    /** 代币图片 */
    @Excel(name = "代币图片")
    private String tokenImage;

    /** 短链接 */
    @Excel(name = "短链接")
    private String shortUrl;

    /** 合约地址 */
    @Excel(name = "合约地址")
    private String contractAddress;

    /** 交易哈希 */
    @Excel(name = "交易哈希")
    private String tradeHash;

    /** 代币简介 */
    @Excel(name = "代币简介")
    private String tokenDesc;

    /** 代币类型 1.ERC-721 2.ERC-1155 */
    @Excel(name = "代币类型 1.ERC-721 2.ERC-1155")
    private Integer tokenType;

    /** 收藏夹状态：0.创建中 1.创建成功 2.创建失败 */
    @Excel(name = "收藏夹状态：0.创建中 1.创建成功 2.创建失败")
    private Integer status;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("collectDirId", getCollectDirId())
                .append("certificationId", getCertificationId())
                .append("tokenName", getTokenName())
                .append("tokenSymbol", getTokenSymbol())
                .append("tokenImage", getTokenImage())
                .append("shortUrl", getShortUrl())
                .append("contractAddress", getContractAddress())
                .append("tradeHash", getTradeHash())
                .append("tokenDesc", getTokenDesc())
                .append("tokenType", getTokenType())
                .append("status", getStatus())
                .append("createId", getCreateId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}