package com.nft.web.controller.system;

import java.util.List;

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
import com.nft.system.domain.SysCertWithdraw;
import com.nft.system.service.ISysCertWithdrawService;
import com.nft.common.utils.poi.ExcelUtil;
import com.nft.common.core.page.TableDataInfo;

/**
 * 出账记录Controller
 *
 * @author nft
 * @date 2021-07-26
 */
@RestController
@RequestMapping("/system/withdraw")
public class SysCertWithdrawController extends BaseController {

    @Autowired
    private ISysCertWithdrawService sysCertWithdrawService;

    /**
     * 查询出账记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCertWithdraw sysCertWithdraw) {
        startPage();
        List<SysCertWithdraw> list = sysCertWithdrawService.selectSysCertWithdrawList(sysCertWithdraw);
        return getDataTable(list);
    }

    /**
     * 导出出账记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:export')")
    @Log(title = "出账记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCertWithdraw sysCertWithdraw) {
        List<SysCertWithdraw> list = sysCertWithdrawService.selectSysCertWithdrawList(sysCertWithdraw);
        ExcelUtil<SysCertWithdraw> util = new ExcelUtil<SysCertWithdraw>(SysCertWithdraw.class);
        return util.exportExcel(list, "出账记录数据");
    }

    /**
     * 获取出账记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:query')")
    @GetMapping(value = "/{certificationWithdrawId}")
    public AjaxResult getInfo(@PathVariable("certificationWithdrawId") Long certificationWithdrawId) {
        return AjaxResult.success(sysCertWithdrawService.selectSysCertWithdrawById(certificationWithdrawId));
    }

    /**
     * 新增出账记录
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:add')")
    @Log(title = "出账记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCertWithdraw sysCertWithdraw) {
        return toAjax(sysCertWithdrawService.insertSysCertWithdraw(sysCertWithdraw));
    }

    /**
     * 修改出账记录
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:edit')")
    @Log(title = "出账记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCertWithdraw sysCertWithdraw) {
        return toAjax(sysCertWithdrawService.updateSysCertWithdraw(sysCertWithdraw));
    }

    /**
     * 删除出账记录
     */
    @PreAuthorize("@ss.hasPermi('system:withdraw:remove')")
    @Log(title = "出账记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{certificationWithdrawIds}")
    public AjaxResult remove(@PathVariable Long[] certificationWithdrawIds) {
        return toAjax(sysCertWithdrawService.deleteSysCertWithdrawByIds(certificationWithdrawIds));
    }
}