package com.nft.mall.api.controller.common;

import cn.hutool.core.util.ObjectUtil;
import com.nft.common.annotation.Log;
import com.nft.common.config.NftConfig;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.enums.BusinessType;
import com.nft.common.enums.EnableStatus;
import com.nft.common.utils.ServletUtils;
import com.nft.common.utils.StringUtils;
import com.nft.common.utils.file.FileUploadUtils;
import com.nft.common.utils.web3.Web3jUtils;
import com.nft.mall.domain.BankConfig;
import com.nft.mall.domain.Banner;
import com.nft.mall.service.ICommonService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 公共Controller
 *
 * @author nft
 * @date 2021-05-12
 */
@Slf4j
@RestController
@RequestMapping("/mall/common")
@Api(tags = "公共相关接口")
public class CommonController {

    @Autowired
    private ICommonService commonService;

    /**
     * 查询银行配置列表
     */
    @Log(title = "查询银行配置列表", businessType = BusinessType.GET)
    @GetMapping(value = "/getBankConfigList")
    public AjaxResult getBankConfigList() {
        BankConfig bankConfig = new BankConfig();
        bankConfig.setStatus(EnableStatus.ENABLE.getCode());
        return commonService.getBankConfigList(new BankConfig());
    }

    /**
     * 查询GasPrice
     */
    @Log(title = "查询GasPrice", businessType = BusinessType.GET)
    @GetMapping(value = "/getGasPrice")
    public AjaxResult getGasPrice() {
        return AjaxResult.success(Web3jUtils.getGasPrice());
    }

    /**
     * 查询新闻列表
     */
    @GetMapping(value = "/listBanner")
    public AjaxResult listBanner(Banner banner) {
        Integer pageNum = ServletUtils.getParameterToInt("pageNum");
        Integer pageSize = ServletUtils.getParameterToInt("pageSize");
        pageSize = ObjectUtil.isNull(pageSize) ? 10 : pageSize;
        Map<String, Object> params = banner.getParams();
        if (StringUtils.isNotNull(pageNum)) {
            params.put("offset" , (pageNum - 1) * pageSize);
            params.put("limit" , pageSize);
            params.put("orderBy" , "create_time desc");
            banner.setParams(params);
        }
        banner.setStatus(0);
        return AjaxResult.success(commonService.listBanner(banner));
    }

    /**
     * 图片上传
     */
    @Log(title = "实名认证图片" , businessType = BusinessType.UPDATE)
    @PostMapping("/uploadImg")
    public AjaxResult uploadImg(@RequestParam("imgFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String avatar = FileUploadUtils.upload(NftConfig.getAvatarPath(), file);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("imgUrl" , avatar);
            return ajax;
        }
        return AjaxResult.error("上传图片异常");
    }

    /**
     * 统计藏品信息
     */
    @GetMapping(value = "/statProductInfo")
    public AjaxResult statProductInfo() {
        return AjaxResult.success(commonService.statProductInfo());
    }
}
