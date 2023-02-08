package com.nft.mall.service;

import java.util.List;
import com.nft.mall.domain.Product;

/**
 * 藏品Service接口
 *
 * @author nft
 * @date 2021-05-17
 */
public interface IProductService
{
    /**
     * 查询藏品
     *
     * @param productId 藏品ID
     * @return 藏品
     */
    public Product selectProductById(Long productId);

    /**
     * 查询藏品列表
     *
     * @param product 藏品
     * @return 藏品集合
     */
    public List<Product> selectProductList(Product product);

    /**
     * 新增藏品
     *
     * @param product 藏品
     * @return 结果
     */
    public int insertProduct(Product product);

    /**
     * 修改藏品
     *
     * @param product 藏品
     * @return 结果
     */
    public int updateProduct(Product product);

    /**
     * 批量删除藏品
     *
     * @param productIds 需要删除的藏品ID
     * @return 结果
     */
    public int deleteProductByIds(Long[] productIds);

    /**
     * 删除藏品信息
     *
     * @param productId 藏品ID
     * @return 结果
     */
    public int deleteProductById(Long productId);
}
