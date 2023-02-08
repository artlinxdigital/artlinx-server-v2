package com.nft.mall.service.impl;

import java.util.List;
import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.ProductMapper;
import com.nft.mall.domain.Product;
import com.nft.mall.service.IProductService;

/**
 * 藏品Service业务层处理
 *
 * @author nft
 * @date 2021-05-17
 */
@Service
public class ProductServiceImpl implements IProductService
{
    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询藏品
     *
     * @param productId 藏品ID
     * @return 藏品
     */
    @Override
    public Product selectProductById(Long productId)
    {
        return productMapper.selectProductById(productId);
    }

    /**
     * 查询藏品列表
     *
     * @param product 藏品
     * @return 藏品
     */
    @Override
    public List<Product> selectProductList(Product product)
    {
        return productMapper.selectProductList(product);
    }

    /**
     * 新增藏品
     *
     * @param product 藏品
     * @return 结果
     */
    @Override
    public int insertProduct(Product product)
    {
        product.setCreateTime(DateUtils.getNowDate());
        return productMapper.insertProduct(product);
    }

    /**
     * 修改藏品
     *
     * @param product 藏品
     * @return 结果
     */
    @Override
    public int updateProduct(Product product)
    {
        product.setUpdateTime(DateUtils.getNowDate());
        return productMapper.updateProduct(product);
    }

    /**
     * 批量删除藏品
     *
     * @param productIds 需要删除的藏品ID
     * @return 结果
     */
    @Override
    public int deleteProductByIds(Long[] productIds)
    {
        return productMapper.deleteProductByIds(productIds);
    }

    /**
     * 删除藏品信息
     *
     * @param productId 藏品ID
     * @return 结果
     */
    @Override
    public int deleteProductById(Long productId)
    {
        return productMapper.deleteProductById(productId);
    }
}
