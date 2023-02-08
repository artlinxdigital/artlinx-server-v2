package com.nft.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nft.common.utils.DateUtils;
import com.nft.system.domain.SysProductCategory;
import com.nft.system.mapper.SysProductCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.system.mapper.SysProductMapper;
import com.nft.system.domain.SysProduct;
import com.nft.system.service.ISysProductService;

/**
 * 藏品Service业务层处理
 *
 * @author nft
 * @date 2021-07-25
 */
@Service
public class SysProductServiceImpl implements ISysProductService {

    @Autowired
    private SysProductMapper sysProductMapper;

    @Autowired
    private SysProductCategoryMapper sysProductCategoryMapper;

    /**
     * 查询藏品
     *
     * @param productId 藏品ID
     * @return 藏品
     */
    @Override
    public SysProduct selectSysProductById(Long productId) {
        return sysProductMapper.selectSysProductById(productId);
    }

    /**
     * 查询藏品列表
     *
     * @param sysProduct 藏品
     * @return 藏品
     */
    @Override
    public List<SysProduct> selectSysProductList(SysProduct sysProduct) {
        List<SysProduct> productList = sysProductMapper.selectSysProductList(sysProduct);
        if (CollectionUtil.isNotEmpty(productList)) {
            List<SysProductCategory> categoryList = sysProductCategoryMapper.selectSysProductCategoryList(new SysProductCategory());
            Map<Long, SysProductCategory> categoryMap = categoryList.stream().collect(Collectors.toMap(SysProductCategory::getProductCategoryId, Function.identity()));
            productList.stream().forEach(product -> {
                SysProductCategory category = categoryMap.getOrDefault(product.getProductCategoryId(), null);
                product.setProductCategoryName(ObjectUtil.isNull(category) ? "" : category.getCategoryName());
            });
        }
        return productList;
    }

    /**
     * 新增藏品
     *
     * @param sysProduct 藏品
     * @return 结果
     */
    @Override
    public int insertSysProduct(SysProduct sysProduct) {
        sysProduct.setCreateTime(DateUtils.getNowDate());
        return sysProductMapper.insertSysProduct(sysProduct);
    }

    /**
     * 修改藏品
     *
     * @param sysProduct 藏品
     * @return 结果
     */
    @Override
    public int updateSysProduct(SysProduct sysProduct) {
        sysProduct.setUpdateTime(DateUtils.getNowDate());
        return sysProductMapper.updateSysProduct(sysProduct);
    }

    /**
     * 批量删除藏品
     *
     * @param productIds 需要删除的藏品ID
     * @return 结果
     */
    @Override
    public int deleteSysProductByIds(Long[] productIds) {
        return sysProductMapper.deleteSysProductByIds(productIds);
    }

    /**
     * 删除藏品信息
     *
     * @param productId 藏品ID
     * @return 结果
     */
    @Override
    public int deleteSysProductById(Long productId) {
        return sysProductMapper.deleteSysProductById(productId);
    }

    /**
     * 查询藏品分类列表
     *
     * @return 藏品分类集合
     */
    @Override
    public List<SysProductCategory> selectSysProductCategoryList() {
        return sysProductCategoryMapper.selectSysProductCategoryList(new SysProductCategory());
    }

    /**
     * 批量上下架藏品
     *
     * @param productIds 需要上下架的藏品ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int onOrOffSysProductByIds(Long[] productIds, Integer status) {
        return sysProductMapper.onOrOffSysProductByIds(productIds, status);
    }
}