package com.nft.mall.mapper;

import java.util.List;
import com.nft.mall.domain.ProductSellConfig;

/**
 * 藏品售卖方式Mapper接口
 *
 * @author nft
 * @date 2021-05-18
 */
public interface ProductSellConfigMapper
{
    /**
     * 查询藏品售卖方式
     *
     * @param productSellConfigId 藏品售卖方式ID
     * @return 藏品售卖方式
     */
    public ProductSellConfig selectProductSellConfigById(Long productSellConfigId);

    /**
     * 查询藏品售卖方式列表
     *
     * @param productSellConfig 藏品售卖方式
     * @return 藏品售卖方式集合
     */
    public List<ProductSellConfig> selectProductSellConfigList(ProductSellConfig productSellConfig);

    /**
     * 新增藏品售卖方式
     *
     * @param productSellConfig 藏品售卖方式
     * @return 结果
     */
    public int insertProductSellConfig(ProductSellConfig productSellConfig);

    /**
     * 修改藏品售卖方式
     *
     * @param productSellConfig 藏品售卖方式
     * @return 结果
     */
    public int updateProductSellConfig(ProductSellConfig productSellConfig);

    /**
     * 删除藏品售卖方式
     *
     * @param productSellConfigId 藏品售卖方式ID
     * @return 结果
     */
    public int deleteProductSellConfigById(Long productSellConfigId);

    /**
     * 批量删除藏品售卖方式
     *
     * @param productSellConfigIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductSellConfigByIds(Long[] productSellConfigIds);
}
