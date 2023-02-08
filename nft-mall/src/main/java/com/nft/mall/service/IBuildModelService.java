package com.nft.mall.service;

import com.nft.common.core.domain.pojo.collect.*;
import com.nft.mall.domain.*;

import java.math.BigDecimal;

/**
 * 构建实体类Service接口
 *
 * @author nft
 * @date 2021-06-29
 */
public interface IBuildModelService {

    /**
     * 构建藏品交易信息
     *
     * @param product  藏品信息
     * @param buyParam 藏品下单信息
     * @param tokenId  TokenID
     * @param owner    卖家
     * @param buyer    买家
     * @param status   交易状态
     * @return 藏品交易信息
     */
    ProductTrade buildProductTrade(Product product, CollectionBuyParam buyParam, String tokenId, Certification owner, Certification buyer, Integer status);

    /**
     * 构建藏品操作信息
     *
     * @param certificationId 用户ID
     * @param productId       藏品ID
     * @param opeType         操作类型
     * @return 藏品操作信息
     */
    ProductOpe buildProductOpe(Long certificationId, Long productId, Integer opeType);

    /**
     * 构建出账记录信息
     *
     * @param certification 用户
     * @param totalAmount   金额
     * @param type          类型
     * @param status        状态
     * @return 藏品操作信息
     */
    CertificationWithdraw buildCertificationWithdraw(Certification certification, BigDecimal totalAmount, Integer type, Integer status);

    /**
     * 构建入账记录信息
     *
     * @param certification 用户
     * @param money         金额
     * @param type          类型
     * @param status        状态
     * @return 藏品操作信息
     */
    Charge buildCharge(Certification certification, BigDecimal money, Integer type, Integer status);

    /**
     * 构建藏品信息
     *
     * @param product    藏品信息
     * @param againParam 藏品信息
     * @param realName   真实姓名
     * @param status     状态
     * @return 藏品信息
     */
    Product buildProduct(Product product, CollectionAgainParam againParam, String realName, Integer status);

    /**
     * 构建藏品Token信息
     *
     * @param againParam 藏品信息
     * @return 藏品信息
     */
    ProductToken buildProductToken(CollectionAgainParam againParam);

    /**
     * 构建藏品Token信息
     *
     * @param revokeParam 藏品信息
     * @return 藏品信息
     */
    ProductToken buildProductToken(RevokeParam revokeParam);

    /**
     * 构建藏品Token信息
     *
     * @param productId   藏品ID
     * @param importParam 藏品信息
     * @return 藏品信息
     */
    ProductToken buildProductToken(Long productId, CollectionImportParam importParam);

    /**
     * 构建藏品Token信息
     *
     * @param productId       藏品ID
     * @param collectionParam 藏品信息
     * @return 藏品信息
     */
    ProductToken buildProductToken(Long productId, CollectionParam collectionParam);

    /**
     * 构建原石记录信息
     *
     * @param certification 用户信息
     * @param amount        数量
     * @param hash          交易哈希
     * @param rewardFrom    原石来源
     * @param rewardStatus  状态
     * @return 原石信息
     */
    RoughRecord buildRoughRecord(Certification certification, BigDecimal amount, String hash, Integer rewardFrom, Integer rewardStatus);

    /**
     * 构建收藏夹信息
     *
     * @param certification 用户信息
     * @param importParam   导入参数
     * @return 收藏夹信息
     */
    CollectDir buildCollectDir(Certification certification, CollectionImportParam importParam);

}
