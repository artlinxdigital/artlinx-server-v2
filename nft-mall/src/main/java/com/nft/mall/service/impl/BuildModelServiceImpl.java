package com.nft.mall.service.impl;

import com.nft.common.core.domain.pojo.collect.*;
import com.nft.common.core.domain.pojo.user.ChargeParam;
import com.nft.common.enums.CollectDirStatus;
import com.nft.common.enums.TokenType;
import com.nft.common.utils.CopyUtils;
import com.nft.common.utils.uuid.IdUtils;
import com.nft.mall.domain.*;
import com.nft.mall.service.IBuildModelService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 构建实体类Service业务层处理
 *
 * @author nft
 * @date 2021-06-29
 */
@Service
public class BuildModelServiceImpl implements IBuildModelService {

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
    @Override
    public ProductTrade buildProductTrade(Product product, CollectionBuyParam buyParam, String tokenId, Certification owner, Certification buyer, Integer status) {
        ProductTrade productTrade = new ProductTrade();
        productTrade.setProductId(product.getProductId());
        productTrade.setCategoryId(product.getProductCategoryId());
        productTrade.setPrice(product.getPrice());
        productTrade.setTokenId(tokenId);
        productTrade.setFromId(product.getSourceProductId());
        productTrade.setToId(buyParam.getCertificationId());
        productTrade.setFromAddress(owner.getWalletAddress());
        productTrade.setToAddress(buyer.getWalletAddress());
        productTrade.setTradeHash(buyParam.getTradeHash());
        productTrade.setContractAddress(buyParam.getContractAddress());
        productTrade.setTradeAmount(product.getPrice());
        productTrade.setTradeFee(buyParam.getFee());
        productTrade.setCopyrightRate(product.getCopyrightRate());
        productTrade.setServiceRate(buyParam.getServiceRate());
        productTrade.setStatus(status);
        productTrade.setCreateId(buyParam.getCertificationId());
        productTrade.setCreateTime(new Date());
        return productTrade;
    }

    /**
     * 构建藏品操作信息
     *
     * @param certificationId 用户ID
     * @param productId       藏品ID
     * @param opeType         操作类型
     * @return 藏品操作信息
     */
    @Override
    public ProductOpe buildProductOpe(Long certificationId, Long productId, Integer opeType) {
        ProductOpe buyProductOpe = new ProductOpe();
        buyProductOpe.setCertificationId(certificationId);
        buyProductOpe.setProductId(productId);
        buyProductOpe.setOpeType(opeType);
        buyProductOpe.setCreateTime(new Date());
        return buyProductOpe;
    }

    /**
     * 构建出账记录信息
     *
     * @param certification 用户
     * @param totalAmount   金额
     * @param type          类型
     * @param status        状态
     * @return 藏品操作信息
     */
    @Override
    public CertificationWithdraw buildCertificationWithdraw(Certification certification, BigDecimal totalAmount, Integer type, Integer status) {
        CertificationWithdraw withdraw = new CertificationWithdraw();
        withdraw.setCertificationId(certification.getId());
        withdraw.setOrderNumber(IdUtils.getTradeNo());
        withdraw.setWithdrawAccount(certification.getMobile());
        withdraw.setWalletAddress(certification.getWalletAddress());
        withdraw.setWithdrawAmount(totalAmount);
        withdraw.setWithdrawType(type);
        withdraw.setWithdrawStatus(status);
        withdraw.setCreateTime(new Date());
        return withdraw;
    }

    /**
     * 构建入账记录信息
     *
     * @param certification 用户
     * @param money         金额
     * @param type          类型
     * @param status        状态
     * @return 藏品操作信息
     */
    @Override
    public Charge buildCharge(Certification certification, BigDecimal money, Integer type, Integer status) {
        // 充值参数
        ChargeParam chargeParam = new ChargeParam();
        chargeParam.setAddress(certification.getWalletAddress());
        chargeParam.setMoney(money);
        chargeParam.setType(type);

        // 充值信息
        Charge charge = CopyUtils.copy(chargeParam, Charge.class);
        charge.setCertificationId(certification.getId());
        charge.setId(IdUtils.getTradeNo());
        charge.setStatus(status);
        charge.setCreateTime(new Date());
        return charge;
    }

    /**
     * 构建藏品信息
     *
     * @param existProduct 藏品信息
     * @param againParam   藏品信息
     * @param realName     真实姓名
     * @param status       状态
     * @return 藏品信息
     */
    @Override
    public Product buildProduct(Product existProduct, CollectionAgainParam againParam, String realName, Integer status) {
        Product product = CopyUtils.copy(againParam, Product.class);
        product.setProductCategoryId(existProduct.getProductCategoryId());
        product.setFileName(existProduct.getFileName());
        product.setFileSuffix(existProduct.getFileSuffix());
        product.setCopyrightRate(existProduct.getCopyrightRate());
        product.setServiceRate(existProduct.getServiceRate());
        product.setCoverImage(existProduct.getCoverImage());
        product.setProductImage(existProduct.getProductImage());
        product.setProductName(existProduct.getProductName());
        product.setProductAuthor(existProduct.getProductAuthor());
        product.setProductAuthorDesc(existProduct.getProductAuthorDesc());
        product.setProductDesc(existProduct.getProductDesc());
        product.setProductType(existProduct.getProductType());
        product.setParentProductId(existProduct.getProductId());
        product.setSourceProductId(existProduct.getSourceProductId().equals(0L) ? existProduct.getProductId() : existProduct.getSourceProductId());
        product.setStatus(status);
        product.setCreateTime(new Date());
        return product;
    }

    /**
     * 构建藏品Token信息
     *
     * @param againParam 藏品信息
     * @return 藏品信息
     */
    @Override
    public ProductToken buildProductToken(CollectionAgainParam againParam) {
        ProductToken productToken = new ProductToken();
        productToken.setProductId(againParam.getProductId());
        productToken.setTokenId(againParam.getTokenId());
        productToken.setCollectDirId(againParam.getCollectDirId());
        productToken.setTradeHash(againParam.getTradeHash());
        productToken.setFee(againParam.getFee());
        productToken.setCreateId(againParam.getCreateId());
        productToken.setCreateTime(new Date());
        productToken.setCoinType(againParam.getCoinType());
        productToken.setSignatureLeft(againParam.getSignatureLeft());
        productToken.setOrderLeft(againParam.getOrderLeft());
        productToken.setRemark(againParam.getRemark());
        return productToken;
    }

    /**
     * 构建藏品Token信息
     *
     * @param revokeParam 藏品信息
     * @return 藏品信息
     */
    @Override
    public ProductToken buildProductToken(RevokeParam revokeParam) {
        ProductToken productToken = new ProductToken();
        productToken.setProductId(revokeParam.getProductId());
        productToken.setTokenId(revokeParam.getTokenId());
        productToken.setCollectDirId(revokeParam.getCollectDirId());
        productToken.setTradeHash(revokeParam.getTradeHash());
        productToken.setCreateId(revokeParam.getCertificationId());
        productToken.setCreateTime(new Date());
        return productToken;
    }

    /**
     * 构建藏品Token信息
     *
     * @param productId       藏品ID
     * @param collectionParam 藏品信息
     * @return 藏品信息
     */
    @Override
    public ProductToken buildProductToken(Long productId, CollectionParam collectionParam) {
        ProductToken productToken = new ProductToken();
        productToken.setProductId(productId);
        productToken.setTokenId(collectionParam.getTokenId());
        productToken.setCollectDirId(collectionParam.getCollectDirId());
        productToken.setTradeHash(collectionParam.getTradeHash());
        productToken.setFee(collectionParam.getFee());
        productToken.setCreateId(collectionParam.getCreateId());
        productToken.setCreateTime(new Date());
        productToken.setCoinType(collectionParam.getCoinType());
        productToken.setSignatureLeft(collectionParam.getSignatureLeft());
        productToken.setOrderLeft(collectionParam.getOrderLeft());
        productToken.setRemark(collectionParam.getRemark());
        return productToken;
    }

    /**
     * 构建藏品Token信息
     *
     * @param productId   藏品ID
     * @param importParam 藏品信息
     * @return 藏品信息
     */
    @Override
    public ProductToken buildProductToken(Long productId, CollectionImportParam importParam) {
        ProductToken productToken = new ProductToken();
        productToken.setProductId(productId);
        productToken.setTokenId(importParam.getTokenId());
        productToken.setCollectDirId(importParam.getCollectDirId());
        productToken.setTradeHash(importParam.getTradeHash());
        productToken.setFee(importParam.getFee());
        productToken.setCreateId(importParam.getCreateId());
        productToken.setCreateTime(new Date());
        return productToken;
    }

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
    @Override
    public RoughRecord buildRoughRecord(Certification certification, BigDecimal amount, String hash, Integer rewardFrom, Integer rewardStatus) {
        RoughRecord roughRecord = new RoughRecord();
        roughRecord.setCertificationId(certification.getId());
        roughRecord.setAddress(certification.getWalletAddress());
        roughRecord.setAmount(amount);
        roughRecord.setHash(hash);
        roughRecord.setRewardFrom(rewardFrom);
        roughRecord.setStatus(rewardStatus);
        roughRecord.setCreateTime(new Date());
        return roughRecord;
    }

    /**
     * 构建收藏夹信息
     *
     * @param certification 用户信息
     * @param importParam   导入参数
     * @return 收藏夹信息
     */
    @Override
    public CollectDir buildCollectDir(Certification certification, CollectionImportParam importParam) {
        CollectDir collectDir = new CollectDir();
        collectDir.setCertificationId(certification.getId());
        collectDir.setCreateId(certification.getId());
        collectDir.setContractAddress(importParam.getContractAddress());
        collectDir.setTokenType(TokenType.ERC721.getCode());
        collectDir.setStatus(CollectDirStatus.YES.getCode());
        collectDir.setCreateTime(new Date());
        collectDir.setUpdateTime(new Date());
        return collectDir;
    }
}
