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
import com.nft.system.domain.SysCharge;
import com.nft.system.service.ISysChargeService;
import com.nft.common.utils.poi.ExcelUtil;
import com.nft.common.core.page.TableDataInfo;

/**
 * 入账记录Controller
 *
 * @author nft
 * @date 2021-07-25
 */
@RestController
@RequestMapping("/system/charge")
public class SysChargeController extends BaseController {
    @Autowired
    private ISysChargeService sysChargeService;

    /**
     * 查询入账记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:charge:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCharge sysCharge) {
        startPage();
        List<SysCharge> list = sysChargeService.selectSysChargeList(sysCharge);
        return getDataTable(list);
    }

    /**
     * 导出入账记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:charge:export')")
    @Log(title = "入账记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCharge sysCharge) {
        List<SysCharge> list = sysChargeService.selectSysChargeList(sysCharge);
        ExcelUtil<SysCharge> util = new ExcelUtil<SysCharge>(SysCharge.class);
        return util.exportExcel(list, "入账记录数据");
    }

    /**
     * 获取入账记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:charge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(sysChargeService.selectSysChargeById(id));
    }

    /**
     * 新增入账记录
     */
    @PreAuthorize("@ss.hasPermi('system:charge:add')")
    @Log(title = "入账记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCharge sysCharge) {
        return toAjax(sysChargeService.insertSysCharge(sysCharge));
    }

    /**
     * 修改入账记录
     */
    @PreAuthorize("@ss.hasPermi('system:charge:edit')")
    @Log(title = "入账记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCharge sysCharge) {
        return toAjax(sysChargeService.updateSysCharge(sysCharge));
    }

    /**
     * 删除入账记录
     */
    @PreAuthorize("@ss.hasPermi('system:charge:remove')")
    @Log(title = "入账记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(sysChargeService.deleteSysChargeByIds(ids));
    }
}