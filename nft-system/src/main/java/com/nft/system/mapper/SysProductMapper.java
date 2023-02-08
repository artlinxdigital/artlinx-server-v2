package com.nft.system.mapper;

import java.util.List;

import com.nft.system.domain.SysProduct;
import org.apache.ibatis.annotations.Param;

/**
 * 藏品Mapper接口
 *
 * @author nft
 * @date 2021-07-25
 */
public interface SysProductMapper {

    /**
     * 查询藏品
     *
     * @param productId 藏品ID
     * @return 藏品
     */
    public SysProduct selectSysProductById(Long productId);

    /**
     * 查询藏品列表
     *
     * @param sysProduct 藏品
     * @return 藏品集合
     */
    public List<SysProduct> selectSysProductList(SysProduct sysProduct);

    /**
     * 新增藏品
     *
     * @param sysProduct 藏品
     * @return 结果
     */
    public int insertSysProduct(SysProduct sysProduct);

    /**
     * 修改藏品
     *
     * @param sysProduct 藏品
     * @return 结果
     */
    public int updateSysProduct(SysProduct sysProduct);

    /**
     * 删除藏品
     *
     * @param productId 藏品ID
     * @return 结果
     */
    public int deleteSysProductById(Long productId);

    /**
     * 批量删除藏品
     *
     * @param productIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysProductByIds(Long[] productIds);

    /**
     * 批量上下架藏品
     *
     * @param productIds 需要上下架的藏品ID
     * @param status 状态
     * @return 结果
     */
    public int onOrOffSysProductByIds(@Param("productIds") Long[] productIds, @Param("status") Integer status);
}