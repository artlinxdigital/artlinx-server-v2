package com.nft.web.controller.nft;

import com.nft.common.annotation.Log;
import com.nft.common.core.controller.BaseController;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.page.TableDataInfo;
import com.nft.common.enums.BusinessType;
import com.nft.mall.domain.Balance;
import com.nft.mall.mapper.BalanceMapper;
import com.nft.mall.service.IBalanceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 余额管理Controller
 *
 * @author nft
 * @date 2021-10-25
 */
@Api("用户余额管理")
@RestController
@RequestMapping("/system/balance")
public class BalanceController extends BaseController {

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private IBalanceService balanceService;

    /**
     * 查询余额列表
     */
    @PreAuthorize("@ss.hasPermi('nft:balance:list')")
    @GetMapping("/list")
    public TableDataInfo userBalanceList(Balance balance) {
        startPage();
        List<Balance> list = balanceMapper.selectBalanceList(balance);
        return getDataTable(list);
    }

    /**
     * 管理员提现
     */
    @Log(title = "管理员给用户手工提现", businessType = BusinessType.WITHDRAWFROMADMIN)
    @PreAuthorize("@ss.hasPermi('nft:balance:withDrawFromAdmin')")
    @PutMapping("/withDrawFromAdmin")
    public AjaxResult withDrawFromAdmin(@RequestBody Balance balance) {
        return balanceService.withDrawFromAdmin(balance.getCertificationId(), balance.getBalance());
    }
}
