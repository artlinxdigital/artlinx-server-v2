package com.nft.mall.service;

import java.util.List;
import com.nft.mall.domain.ProductCategory;

/**
 * 藏品分类Service接口
 *
 * @author nft
 * @date 2021-05-17
 */
public interface IProductCategoryService
{
    /**
     * 查询藏品分类
     *
     * @param productCategoryId 藏品分类ID
     * @return 藏品分类
     */
    public ProductCategory selectProductCategoryById(Long productCategoryId);

    /**
     * 查询藏品分类列表
     *
     * @param productCategory 藏品分类
     * @return 藏品分类集合
     */
    public List<ProductCategory> selectProductCategoryList(ProductCategory productCategory);

    /**
     * 新增藏品分类
     *
     * @param productCategory 藏品分类
     * @return 结果
     */
    public int insertProductCategory(ProductCategory productCategory);

    /**
     * 修改藏品分类
     *
     * @param productCategory 藏品分类
     * @return 结果
     */
    public int updateProductCategory(ProductCategory productCategory);

    /**
     * 批量删除藏品分类
     *
     * @param productCategoryIds 需要删除的藏品分类ID
     * @return 结果
     */
    public int deleteProductCategoryByIds(Long[] productCategoryIds);

    /**
     * 删除藏品分类信息
     *
     * @param productCategoryId 藏品分类ID
     * @return 结果
     */
    public int deleteProductCategoryById(Long productCategoryId);
}
