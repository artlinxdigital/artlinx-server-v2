package com.nft.mall.mapper;

import java.util.List;

import com.nft.mall.domain.ProductCoinConfig;

/**
 * 藏品支付币种Mapper接口
 *
 * @author nft
 * @date 2021-05-18
 */
public interface ProductCoinConfigMapper {
    /**
     * 查询藏品支付币种
     *
     * @param productCoinConfigId 藏品支付币种ID
     * @return 藏品支付币种
     */
    public ProductCoinConfig selectProductCoinConfigById(Long productCoinConfigId);

    /**
     * 查询藏品支付币种列表
     *
     * @param productCoinConfig 藏品支付币种
     * @return 藏品支付币种集合
     */
    public List<ProductCoinConfig> selectProductCoinConfigList(ProductCoinConfig productCoinConfig);

    /**
     * 新增藏品支付币种
     *
     * @param productCoinConfig 藏品支付币种
     * @return 结果
     */
    public int insertProductCoinConfig(ProductCoinConfig productCoinConfig);

    /**
     * 修改藏品支付币种
     *
     * @param productCoinConfig 藏品支付币种
     * @return 结果
     */
    public int updateProductCoinConfig(ProductCoinConfig productCoinConfig);

    /**
     * 删除藏品支付币种
     *
     * @param productCoinConfigId 藏品支付币种ID
     * @return 结果
     */
    public int deleteProductCoinConfigById(Long productCoinConfigId);

    /**
     * 批量删除藏品支付币种
     *
     * @param productCoinConfigIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductCoinConfigByIds(Long[] productCoinConfigIds);

    /**
     * 查询藏品支付币种
     *
     * @param coinType 藏品币种类型
     * @return 藏品支付币种
     */
    ProductCoinConfig selectByCoinType(Integer coinType);
}
