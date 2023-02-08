package com.nft.mall.mapper;

import java.util.List;
import java.util.Set;

import com.nft.mall.domain.ProductCategory;
import org.apache.ibatis.annotations.Param;

/**
 * 藏品分类Mapper接口
 *
 * @author nft
 * @date 2021-05-17
 */
public interface ProductCategoryMapper
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
     * 删除藏品分类
     *
     * @param productCategoryId 藏品分类ID
     * @return 结果
     */
    public int deleteProductCategoryById(Long productCategoryId);

    /**
     * 批量删除藏品分类
     *
     * @param productCategoryIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductCategoryByIds(Long[] productCategoryIds);

    /**
     * 批量查询分类列表
     *
     * @param categoryIdSet 需要查询的数据ID集合
     * @return 结果
     */
    List<ProductCategory> selectByCategoryIdSet(@Param("categoryIdSet") Set<Long> categoryIdSet);
}
