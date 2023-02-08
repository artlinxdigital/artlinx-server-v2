package com.nft.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nft.system.domain.SysCertification;
import com.nft.system.domain.SysOrder;
import com.nft.system.domain.SysProductCategory;
import com.nft.system.mapper.SysCertificationMapper;
import com.nft.system.mapper.SysOrderMapper;
import com.nft.system.mapper.SysProductCategoryMapper;
import com.nft.system.service.ISysOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单信息Service业务层处理
 *
 * @author nft
 * @date 2021-07-24
 */
@Service
public class SysOrderServiceImpl implements ISysOrderService {

    @Autowired
    private SysOrderMapper sysOrderMapper;
    
    @Autowired
    private SysCertificationMapper sysCertificationMapper;

    @Autowired
    private SysProductCategoryMapper sysProductCategoryMapper;

    /**
     * 查询订单信息列表
     *
     * @param sysOrder 订单信息
     * @return 订单信息集合
     */
    @Override
    public List<SysOrder> selectSysOrderList(SysOrder sysOrder) {
        List<SysOrder> orderList = sysOrderMapper.selectSysOrderList(sysOrder);
        if (CollUtil.isNotEmpty(orderList)) {
            List<SysProductCategory> categoryList = sysProductCategoryMapper.selectSysProductCategoryList(new SysProductCategory());
            Map<Long, SysProductCategory> categoryMap = categoryList.stream().collect(Collectors.toMap(SysProductCategory::getProductCategoryId, Function.identity()));
            Set<Long> fromIdSet = orderList.stream().map(SysOrder::getFromId).collect(Collectors.toSet());
            List<SysCertification> sellerList = sysCertificationMapper.selectByCertificationIdSet(fromIdSet);
            Map<Long, SysCertification> sellerMap = sellerList.stream().collect(Collectors.toMap(SysCertification::getId, Function.identity()));
            orderList.stream().forEach(order -> {
                SysProductCategory category = categoryMap.getOrDefault(order.getCategoryId(), null);
                order.setCategoryName(ObjectUtil.isNull(category) ? "" : category.getCategoryName());
                SysCertification seller = sellerMap.getOrDefault(order.getFromId(), null);
                order.setSeller(Optional.ofNullable(seller.getRealName()).orElse(""));
                order.setSellerAccount(Optional.ofNullable(seller.getMobile()).orElse(""));
            });
        }
        return orderList;
    }
}
