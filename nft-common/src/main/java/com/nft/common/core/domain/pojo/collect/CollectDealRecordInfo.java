package com.nft.common.core.domain.pojo.collect;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 藏品交易记录信息
 *
 * @author nft
 * @date 2021-06-15
 */
@Data
public class CollectDealRecordInfo {

    /** 主键ID */
    private Long productTradeId;

    /** 藏品单价 */
    private BigDecimal price;

    /** 藏品拥有者 */
    private Long toId;

    /** 藏品拥有者 */
    private String owner;

    /** 藏品拥有者昵称 */
    private String ownerNickName;

    /** 藏品拥有者 */
    private String toAddress;

    /** 交易哈希 */
    private String tradeHash;

    /** 藏品交易时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String coinType;

}
