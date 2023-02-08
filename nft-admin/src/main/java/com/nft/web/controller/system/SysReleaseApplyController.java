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
import com.nft.system.domain.SysReleaseApply;
import com.nft.system.service.ISysReleaseApplyService;
import com.nft.common.utils.poi.ExcelUtil;
import com.nft.common.core.page.TableDataInfo;

/**
 * 铸造权限申请信息Controller
 *
 * @author nft
 * @date 2021-07-24
 */
@RestController
@RequestMapping("/system/apply")
public class SysReleaseApplyController extends BaseController {

    @Autowired
    private ISysReleaseApplyService sysReleaseApplyService;

    /**
     * 查询铸造权限申请信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysReleaseApply sysReleaseApply) {
        startPage();
        List<SysReleaseApply> list = sysReleaseApplyService.selectSysReleaseApplyList(sysReleaseApply);
        return getDataTable(list);
    }

    /**
     * 导出铸造权限申请信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:apply:export')")
    @Log(title = "铸造权限申请信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysReleaseApply sysReleaseApply) {
        List<SysReleaseApply> list = sysReleaseApplyService.selectSysReleaseApplyList(sysReleaseApply);
        ExcelUtil<SysReleaseApply> util = new ExcelUtil<SysReleaseApply>(SysReleaseApply.class);
        return util.exportExcel(list, "铸造权限申请信息数据");
    }

    /**
     * 获取铸造权限申请信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:apply:query')")
    @GetMapping(value = "/{releaseApplyId}")
    public AjaxResult getInfo(@PathVariable("releaseApplyId") Long releaseApplyId) {
        return AjaxResult.success(sysReleaseApplyService.selectSysReleaseApplyById(releaseApplyId));
    }

    /**
     * 新增铸造权限申请信息
     */
    @PreAuthorize("@ss.hasPermi('system:apply:add')")
    @Log(title = "铸造权限申请信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysReleaseApply sysReleaseApply) {
        return toAjax(sysReleaseApplyService.insertSysReleaseApply(sysReleaseApply));
    }

    /**
     * 修改铸造权限申请信息
     */
    @PreAuthorize("@ss.hasPermi('system:apply:edit')")
    @Log(title = "铸造权限申请信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysReleaseApply sysReleaseApply) {
        return toAjax(sysReleaseApplyService.updateSysReleaseApply(sysReleaseApply));
    }

    /**
     * 删除铸造权限申请信息
     */
    @PreAuthorize("@ss.hasPermi('system:apply:remove')")
    @Log(title = "铸造权限申请信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{releaseApplyIds}")
    public AjaxResult remove(@PathVariable Long[] releaseApplyIds) {
        return toAjax(sysReleaseApplyService.deleteSysReleaseApplyByIds(releaseApplyIds));
    }

    /**
     * 审核铸造权限申请信息
     */
    @PreAuthorize("@ss.hasPermi('system:apply:audit')")
    @Log(title = "铸造权限申请审核信息", businessType = BusinessType.UPDATE)
    @PutMapping("/{releaseApplyIds}/{status}")
    public AjaxResult audit(@PathVariable Long[] releaseApplyIds, @PathVariable("status") Integer status) {
        return toAjax(sysReleaseApplyService.auditSysReleaseApplyByIds(releaseApplyIds, status));
    }
}