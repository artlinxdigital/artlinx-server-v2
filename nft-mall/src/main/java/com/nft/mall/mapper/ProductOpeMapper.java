package com.nft.mall.mapper;

import java.util.List;
import java.util.Set;

import com.nft.mall.domain.ProductOpe;
import org.apache.ibatis.annotations.Param;

/**
 * 藏品操作Mapper接口
 *
 * @author nft
 * @date 2021-05-17
 */
public interface ProductOpeMapper
{
    /**
     * 查询藏品操作
     *
     * @param productOpeId 藏品操作ID
     * @return 藏品操作
     */
    public ProductOpe selectProductOpeById(Long productOpeId);

    /**
     * 查询藏品操作列表
     *
     * @param productOpe 藏品操作
     * @return 藏品操作集合
     */
    public List<ProductOpe> selectProductOpeList(ProductOpe productOpe);

    /**
     * 新增藏品操作
     *
     * @param productOpe 藏品操作
     * @return 结果
     */
    public int insertProductOpe(ProductOpe productOpe);

    /**
     * 修改藏品操作
     *
     * @param productOpe 藏品操作
     * @return 结果
     */
    public int updateProductOpe(ProductOpe productOpe);

    /**
     * 删除藏品操作
     *
     * @param productOpeId 藏品操作ID
     * @return 结果
     */
    public int deleteProductOpeById(Long productOpeId);

    /**
     * 批量删除藏品操作
     *
     * @param productOpeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductOpeByIds(Long[] productOpeIds);

    /**
     * 查询藏品操作信息
     *
     * @param productId 藏品ID
     * @param certificationId 操作者ID
     * @param opeType 操作类型
     * @return 结果
     */
    ProductOpe selectByProductIdAndCertificationIdAndType(@Param("productId") Long productId, @Param("certificationId") Long certificationId, @Param("opeType") Integer opeType);

    /**
     * 删除藏品操作信息
     *
     * @param productId 藏品ID
     * @param certificationId 操作者ID
     * @param opeType 操作类型
     * @return 结果
     */
    int delByProductIdAndCertificationIdAndType(@Param("productId") Long productId, @Param("certificationId") Long certificationId, @Param("opeType") Integer opeType);

    /**
     * 批量查询藏品操作
     *
     * @param productIdSet 需要查询的数据ID集合
     * @return 结果
     */
    List<ProductOpe> selectByProductIdSet(@Param("productIdSet") Set<Long> productIdSet);

    /**
     * 查询收藏列表
     *
     * @param certificationIdSet 操作者ID集合
     * @param opeTypeSet 操作类型集合
     * @return 结果
     */
    List<ProductOpe> selectByCertificationIdSetAndTypeSet(@Param("certificationIdSet") Set<Long> certificationIdSet, @Param("opeTypeSet") Set<Integer> opeTypeSet);
}
