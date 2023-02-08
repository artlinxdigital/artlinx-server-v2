package com.nft.web.controller.system;

import com.nft.common.annotation.Log;
import com.nft.common.core.controller.BaseController;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.page.TableDataInfo;
import com.nft.common.enums.BusinessType;
import com.nft.common.enums.CertificationStatus;
import com.nft.system.domain.SysCertification;
import com.nft.system.domain.param.SysRealAuditParam;
import com.nft.system.service.ISysCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 个人/机构实名申请信息Controller
 *
 * @author nft
 * @date 2021-07-24
 */
@RestController
@RequestMapping("/system/realname")
public class SysRealNameApplyController extends BaseController {

    @Autowired
    private ISysCertificationService sysCertificationService;

    /**
     * 查询会员信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:realname:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCertification sysCertification) {
        sysCertification.setStatus(CertificationStatus.WAIT_CHECK.getCode());
        startPage();
        List<SysCertification> list = sysCertificationService.selectSysCertificationList(sysCertification);
        return getDataTable(list);
    }

    /**
     * 实名审核
     */
    @PreAuthorize("@ss.hasPermi('system:realname:verify')")
    @Log(title = "实名审核", businessType = BusinessType.UPDATE)
    @PutMapping("/verifyRealName")
    public AjaxResult verifyRealName(@RequestBody SysRealAuditParam auditParam) {
        return sysCertificationService.verifyRealName(auditParam);
    }

}
