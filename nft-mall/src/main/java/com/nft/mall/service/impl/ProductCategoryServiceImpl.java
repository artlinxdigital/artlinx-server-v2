package com.nft.mall.service.impl;

import java.util.List;
import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.ProductCategoryMapper;
import com.nft.mall.domain.ProductCategory;
import com.nft.mall.service.IProductCategoryService;

/**
 * 藏品分类Service业务层处理
 *
 * @author nft
 * @date 2021-05-17
 */
@Service
public class ProductCategoryServiceImpl implements IProductCategoryService
{
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    /**
     * 查询藏品分类
     *
     * @param productCategoryId 藏品分类ID
     * @return 藏品分类
     */
    @Override
    public ProductCategory selectProductCategoryById(Long productCategoryId)
    {
        return productCategoryMapper.selectProductCategoryById(productCategoryId);
    }

    /**
     * 查询藏品分类列表
     *
     * @param productCategory 藏品分类
     * @return 藏品分类
     */
    @Override
    public List<ProductCategory> selectProductCategoryList(ProductCategory productCategory)
    {
        return productCategoryMapper.selectProductCategoryList(productCategory);
    }

    /**
     * 新增藏品分类
     *
     * @param productCategory 藏品分类
     * @return 结果
     */
    @Override
    public int insertProductCategory(ProductCategory productCategory)
    {
        productCategory.setCreateTime(DateUtils.getNowDate());
        return productCategoryMapper.insertProductCategory(productCategory);
    }

    /**
     * 修改藏品分类
     *
     * @param productCategory 藏品分类
     * @return 结果
     */
    @Override
    public int updateProductCategory(ProductCategory productCategory)
    {
        productCategory.setUpdateTime(DateUtils.getNowDate());
        return productCategoryMapper.updateProductCategory(productCategory);
    }

    /**
     * 批量删除藏品分类
     *
     * @param productCategoryIds 需要删除的藏品分类ID
     * @return 结果
     */
    @Override
    public int deleteProductCategoryByIds(Long[] productCategoryIds)
    {
        return productCategoryMapper.deleteProductCategoryByIds(productCategoryIds);
    }

    /**
     * 删除藏品分类信息
     *
     * @param productCategoryId 藏品分类ID
     * @return 结果
     */
    @Override
    public int deleteProductCategoryById(Long productCategoryId)
    {
        return productCategoryMapper.deleteProductCategoryById(productCategoryId);
    }
}
