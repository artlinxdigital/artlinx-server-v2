package com.nft.web.controller.system;

import com.nft.common.annotation.Log;
import com.nft.common.core.controller.BaseController;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.page.TableDataInfo;
import com.nft.common.enums.BusinessType;
import com.nft.common.enums.WithdrawType;
import com.nft.common.utils.SecurityUtils;
import com.nft.common.utils.poi.ExcelUtil;
import com.nft.system.domain.SysCertWithdraw;
import com.nft.system.service.ISysCertWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提现管理Controller
 *
 * @author nft
 * @date 2021-07-26
 */
@RestController
@RequestMapping("/system/bill")
public class SysCertBillController extends BaseController {

    @Autowired
    private ISysCertWithdrawService sysCertWithdrawService;

    /**
     * 查询提现记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:bill:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCertWithdraw sysCertWithdraw) {
        sysCertWithdraw.setBillTypeList(WithdrawType.getBillTypeList());
        startPage();
        List<SysCertWithdraw> list = sysCertWithdrawService.selectSysCertBillList(sysCertWithdraw);
        return getDataTable(list);
    }

    /**
     * 导出提现记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:bill:export')")
    @Log(title = "提现管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCertWithdraw sysCertWithdraw) {
        List<SysCertWithdraw> list = sysCertWithdrawService.selectSysCertWithdrawList(sysCertWithdraw);
        ExcelUtil<SysCertWithdraw> util = new ExcelUtil<SysCertWithdraw>(SysCertWithdraw.class);
        return util.exportExcel(list, "出账记录数据");
    }

    /**
     * 获取提现记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:bill:query')")
    @GetMapping(value = "/{certificationWithdrawId}")
    public AjaxResult getInfo(@PathVariable("certificationWithdrawId") Long certificationWithdrawId) {
        return AjaxResult.success(sysCertWithdrawService.selectSysCertWithdrawById(certificationWithdrawId));
    }

    /**
     * 提现审核
     */
    @PreAuthorize("@ss.hasPermi('system:bill:verify')")
    @Log(title = "提现管理", businessType = BusinessType.UPDATE)
    @PutMapping("/verifyBill")
    public AjaxResult verifyBill(@RequestBody SysCertWithdraw sysCertWithdraw) {
        sysCertWithdraw.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysCertWithdrawService.updateSysCertBillStatus(sysCertWithdraw));
    }

}
