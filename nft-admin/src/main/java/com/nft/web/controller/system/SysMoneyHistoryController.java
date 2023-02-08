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
import com.nft.system.domain.SysMoneyHistory;
import com.nft.system.service.ISysMoneyHistoryService;
import com.nft.common.utils.poi.ExcelUtil;
import com.nft.common.core.page.TableDataInfo;

/**
 * 钱支出收入记录Controller
 *
 * @author nft
 * @date 2021-11-04
 */
@RestController
@RequestMapping("/system/history")
public class SysMoneyHistoryController extends BaseController {

    @Autowired
    private ISysMoneyHistoryService sysMoneyHistoryService;

    /**
     * 查询钱支出收入记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysMoneyHistory sysMoneyHistory) {
        startPage();
        List<SysMoneyHistory> list = sysMoneyHistoryService.selectSysMoneyHistoryList(sysMoneyHistory);
        return getDataTable(list);
    }

    /**
     * 导出钱支出收入记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:history:export')")
    @Log(title = "钱支出收入记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysMoneyHistory sysMoneyHistory) {
        List<SysMoneyHistory> list = sysMoneyHistoryService.selectSysMoneyHistoryList(sysMoneyHistory);
        ExcelUtil<SysMoneyHistory> util = new ExcelUtil<SysMoneyHistory>(SysMoneyHistory.class);
        return util.exportExcel(list, "钱支出收入记录数据");
    }

    /**
     * 获取钱支出收入记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(sysMoneyHistoryService.selectSysMoneyHistoryById(id));
    }

    /**
     * 新增钱支出收入记录
     */
    @PreAuthorize("@ss.hasPermi('system:history:add')")
    @Log(title = "钱支出收入记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMoneyHistory sysMoneyHistory) {
        return toAjax(sysMoneyHistoryService.insertSysMoneyHistory(sysMoneyHistory));
    }

    /**
     * 修改钱支出收入记录
     */
    @PreAuthorize("@ss.hasPermi('system:history:edit')")
    @Log(title = "钱支出收入记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMoneyHistory sysMoneyHistory) {
        return toAjax(sysMoneyHistoryService.updateSysMoneyHistory(sysMoneyHistory));
    }

    /**
     * 删除钱支出收入记录
     */
    @PreAuthorize("@ss.hasPermi('system:history:remove')")
    @Log(title = "钱支出收入记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(sysMoneyHistoryService.deleteSysMoneyHistoryByIds(ids));
    }
}