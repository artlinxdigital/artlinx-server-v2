package com.nft.mall.mapper;

import java.util.List;

import com.nft.common.core.domain.pojo.collect.CollectionDirParam;
import com.nft.mall.domain.ProductToken;

/**
 * 藏品TokenMapper接口
 *
 * @author nft
 * @date 2021-05-18
 */
public interface ProductTokenMapper
{
    /**
     * 查询藏品Token
     *
     * @param productTokenId 藏品TokenID
     * @return 藏品Token
     */
    public ProductToken selectProductTokenById(Long productTokenId);

    /**
     * 查询藏品Token列表
     *
     * @param productToken 藏品Token
     * @return 藏品Token集合
     */
    public List<ProductToken> selectProductTokenList(ProductToken productToken);

    /**
     * 新增藏品Token
     *
     * @param productToken 藏品Token
     * @return 结果
     */
    public int insertProductToken(ProductToken productToken);

    /**
     * 修改藏品Token
     *
     * @param productToken 藏品Token
     * @return 结果
     */
    public int updateProductToken(ProductToken productToken);

    /**
     * 删除藏品Token
     *
     * @param productTokenId 藏品TokenID
     * @return 结果
     */
    public int deleteProductTokenById(Long productTokenId);

    /**
     * 批量删除藏品Token
     *
     * @param productTokenIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductTokenByIds(Long[] productTokenIds);

    /**
     * 查询藏品Token
     *
     * @param productId 藏品ID
     * @return 结果
     */
    List<ProductToken>  selectByProductId(Long productId);

    /**
     * 查询藏品列表
     *
     * @param collectionDirParam 藏品
     * @return 藏品集合
     */
    public List<ProductToken> selectCollectionDirList(CollectionDirParam collectionDirParam);

    /**
     * 查询藏品Token
     *
     * @param tokenId 藏品TokenID
     * @return 藏品Token
     */
    public List<ProductToken> selectByTokenId(String tokenId);

}
