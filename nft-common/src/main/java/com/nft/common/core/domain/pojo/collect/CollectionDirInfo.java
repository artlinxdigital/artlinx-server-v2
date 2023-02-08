package com.nft.common.core.domain.pojo.collect;

import lombok.Data;

/**
 * 收藏夹信息
 *
 * @author nft
 * @date 2021-06-03
 */
@Data
public class CollectionDirInfo {

    /** 主键ID */
    private Long collectDirId;

    /** 代币名称 */
    private String tokenName;

    /** 代币符号 */
    private String tokenSymbol;

    /** 代币图片 */
    private String tokenImage;

    /** 短链接 */
    private String shortUrl;

    /** 合约地址 */
    private String contractAddress;

    /** 代币简介 */
    private String tokenDesc;

}
