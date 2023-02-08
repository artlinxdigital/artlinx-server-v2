package com.nft.system.service;

import com.nft.system.domain.SysOrder;

import java.util.List;

/**
 * 订单信息Service接口
 *
 * @author nft
 * @date 2021-07-24
 */
public interface ISysOrderService {

    /**
     * 查询订单信息列表
     *
     * @param sysOrder 订单信息
     * @return 订单信息集合
     */
    List<SysOrder> selectSysOrderList(SysOrder sysOrder);

}
