package com.nft.web.controller.system;

import java.util.List;

import com.google.common.collect.Sets;
import com.nft.common.enums.CollectStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nft.common.annotation.Log;
import com.nft.common.core.controller.BaseController;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.enums.BusinessType;
import com.nft.system.domain.SysProduct;
import com.nft.system.service.ISysProductService;
import com.nft.common.utils.poi.ExcelUtil;
import com.nft.common.core.page.TableDataInfo;

/**
 * 藏品Controller
 *
 * @author nft
 * @date 2021-07-25
 */
@RestController
@RequestMapping("/system/product")
public class SysProductController extends BaseController {

    @Autowired
    private ISysProductService sysProductService;

    /**
     * 查询藏品列表
     */
    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysProduct sysProduct) {
        sysProduct.setExcludeStatusSet(Sets.newHashSet(CollectStatus.TRADED.getCode()));
        startPage();
        List<SysProduct> list = sysProductService.selectSysProductList(sysProduct);
        return getDataTable(list);
    }

    /**
     * 导出藏品列表
     */
    @PreAuthorize("@ss.hasPermi('system:product:export')")
    @Log(title = "藏品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysProduct sysProduct) {
        List<SysProduct> list = sysProductService.selectSysProductList(sysProduct);
        ExcelUtil<SysProduct> util = new ExcelUtil<SysProduct>(SysProduct.class);
        return util.exportExcel(list, "藏品数据");
    }

    /**
     * 获取藏品详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable("productId") Long productId) {
        return AjaxResult.success(sysProductService.selectSysProductById(productId));
    }

    /**
     * 新增藏品
     */
    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @Log(title = "藏品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysProduct sysProduct) {
        return toAjax(sysProductService.insertSysProduct(sysProduct));
    }

    /**
     * 修改藏品
     */
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "藏品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysProduct sysProduct) {
        return toAjax(sysProductService.updateSysProduct(sysProduct));
    }

    /**
     * 删除藏品
     */
    @PreAuthorize("@ss.hasPermi('system:product:remove')")
    @Log(title = "藏品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds) {
        return toAjax(sysProductService.deleteSysProductByIds(productIds));
    }

    /**
     * 藏品分类
     */
    @PreAuthorize("@ss.hasPermi('system:product:category')")
    @Log(title = "藏品分类", businessType = BusinessType.GET)
    @GetMapping("/category")
    public AjaxResult category() {
        return AjaxResult.success(sysProductService.selectSysProductCategoryList());
    }

    /**
     * 藏品上下架
     */
    @PreAuthorize("@ss.hasPermi('system:product:onoroff')")
    @Log(title = "藏品上下架", businessType = BusinessType.UPDATE)
    @PutMapping("/{productIds}/{status}")
    public AjaxResult onOrOff(@PathVariable Long[] productIds, @PathVariable("status") Integer status) {
        return toAjax(sysProductService.onOrOffSysProductByIds(productIds, status));
    }
}