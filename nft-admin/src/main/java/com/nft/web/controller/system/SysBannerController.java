package com.nft.web.controller.system;

import java.io.IOException;
import java.util.List;

import com.nft.common.config.NftConfig;
import com.nft.common.utils.file.FileUploadUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nft.common.annotation.Log;
import com.nft.common.core.controller.BaseController;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.enums.BusinessType;
import com.nft.system.domain.SysBanner;
import com.nft.system.service.ISysBannerService;
import com.nft.common.utils.poi.ExcelUtil;
import com.nft.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 轮播图Controller
 *
 * @author nft
 * @date 2021-10-20
 */
@RestController
@RequestMapping("/system/banner")
public class SysBannerController extends BaseController {

    @Autowired
    private ISysBannerService sysBannerService;

    /**
     * 查询轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('system:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysBanner sysBanner) {
        startPage();
        List<SysBanner> list = sysBannerService.selectSysBannerList(sysBanner);
        return getDataTable(list);
    }

    /**
     * 导出轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('system:banner:export')")
    @Log(title = "轮播图", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysBanner sysBanner) {
        List<SysBanner> list = sysBannerService.selectSysBannerList(sysBanner);
        ExcelUtil<SysBanner> util = new ExcelUtil<SysBanner>(SysBanner.class);
        return util.exportExcel(list, "轮播图数据");
    }

    /**
     * 获取轮播图详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:banner:query')")
    @GetMapping(value = "/{bannerId}")
    public AjaxResult getInfo(@PathVariable("bannerId") Long bannerId) {
        return AjaxResult.success(sysBannerService.selectSysBannerById(bannerId));
    }

    /**
     * 新增轮播图
     */
    @PreAuthorize("@ss.hasPermi('system:banner:add')")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysBanner sysBanner) {
        return toAjax(sysBannerService.insertSysBanner(sysBanner));
    }

    /**
     * 修改轮播图
     */
    @PreAuthorize("@ss.hasPermi('system:banner:edit')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysBanner sysBanner) {
        return toAjax(sysBannerService.updateSysBanner(sysBanner));
    }

    /**
     * 删除轮播图
     */
    @PreAuthorize("@ss.hasPermi('system:banner:remove')")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bannerIds}")
    public AjaxResult remove(@PathVariable Long[] bannerIds) {
        return toAjax(sysBannerService.deleteSysBannerByIds(bannerIds));
    }

    /**
     * 图片上传
     */
    @Log(title = "新闻图片" , businessType = BusinessType.UPDATE)
    @PostMapping("/uploadImg")
    public AjaxResult uploadImg(@RequestParam("imgFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String avatar = FileUploadUtils.upload(NftConfig.getAvatarPath(), file);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("imgUrl" , avatar);
            return ajax;
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
