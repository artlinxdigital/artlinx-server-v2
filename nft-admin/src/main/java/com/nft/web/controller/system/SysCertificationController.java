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
import com.nft.system.domain.SysCertification;
import com.nft.system.service.ISysCertificationService;
import com.nft.common.utils.poi.ExcelUtil;
import com.nft.common.core.page.TableDataInfo;

/**
 * 会员信息Controller
 *
 * @author nft
 * @date 2021-07-24
 */
@RestController
@RequestMapping("/system/certification")
public class SysCertificationController extends BaseController {

    @Autowired
    private ISysCertificationService sysCertificationService;

    /**
     * 查询会员信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:certification:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCertification sysCertification) {
        startPage();
        List<SysCertification> list = sysCertificationService.selectSysCertificationList(sysCertification);
        return getDataTable(list);
    }

    /**
     * 导出会员信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:certification:export')")
    @Log(title = "会员信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCertification sysCertification) {
        List<SysCertification> list = sysCertificationService.selectSysCertificationList(sysCertification);
        ExcelUtil<SysCertification> util = new ExcelUtil<SysCertification>(SysCertification.class);
        return util.exportExcel(list, "会员信息数据");
    }

    /**
     * 获取会员信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:certification:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysCertificationService.selectSysCertificationById(id));
    }

    /**
     * 新增会员信息
     */
    @PreAuthorize("@ss.hasPermi('system:certification:add')")
    @Log(title = "会员信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCertification sysCertification) {
        return toAjax(sysCertificationService.insertSysCertification(sysCertification));
    }

    /**
     * 修改会员信息
     */
    @PreAuthorize("@ss.hasPermi('system:certification:edit')")
    @Log(title = "会员信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCertification sysCertification) {
        return toAjax(sysCertificationService.updateSysCertification(sysCertification));
    }

    /**
     * 删除会员信息
     */
    @PreAuthorize("@ss.hasPermi('system:certification:remove')")
    @Log(title = "会员信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysCertificationService.deleteSysCertificationByIds(ids));
    }

}