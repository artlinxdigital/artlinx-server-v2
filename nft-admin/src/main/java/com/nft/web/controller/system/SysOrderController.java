package com.nft.web.controller.system;

import com.nft.common.core.controller.BaseController;
import com.nft.common.core.page.TableDataInfo;
import com.nft.system.domain.SysOrder;
import com.nft.system.service.ISysOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单信息Controller
 *
 * @author nft
 * @date 2021-07-24
 */
@RestController
@RequestMapping("/system/order")
public class SysOrderController extends BaseController {

    @Autowired
    private ISysOrderService sysOrderService;

    /**
     * 查询订单信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOrder sysOrder) {
        startPage();
        List<SysOrder> list = sysOrderService.selectSysOrderList(sysOrder);
        return getDataTable(list);
    }
}
