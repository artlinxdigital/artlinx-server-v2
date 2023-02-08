package com.nft.system.mapper;

import java.util.List;

import com.nft.system.domain.SysProductCategory;

/**
 * 藏品分类Mapper接口
 *
 * @author nft
 * @date 2021-07-25
 */
public interface SysProductCategoryMapper {

    /**
     * 查询藏品分类
     *
     * @param productCategoryId 藏品分类ID
     * @return 藏品分类
     */
    public SysProductCategory selectSysProductCategoryById(Long productCategoryId);

    /**
     * 查询藏品分类列表
     *
     * @param sysProductCategory 藏品分类
     * @return 藏品分类集合
     */
    public List<SysProductCategory> selectSysProductCategoryList(SysProductCategory sysProductCategory);

    /**
     * 新增藏品分类
     *
     * @param sysProductCategory 藏品分类
     * @return 结果
     */
    public int insertSysProductCategory(SysProductCategory sysProductCategory);

    /**
     * 修改藏品分类
     *
     * @param sysProductCategory 藏品分类
     * @return 结果
     */
    public int updateSysProductCategory(SysProductCategory sysProductCategory);

    /**
     * 删除藏品分类
     *
     * @param productCategoryId 藏品分类ID
     * @return 结果
     */
    public int deleteSysProductCategoryById(Long productCategoryId);

    /**
     * 批量删除藏品分类
     *
     * @param productCategoryIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysProductCategoryByIds(Long[] productCategoryIds);
}