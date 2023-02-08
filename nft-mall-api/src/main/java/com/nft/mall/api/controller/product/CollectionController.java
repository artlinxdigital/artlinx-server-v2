package com.nft.mall.api.controller.product;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.nft.common.annotation.Log;
import com.nft.common.config.NftConfig;
import com.nft.common.core.controller.BaseController;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.collect.*;
import com.nft.common.enums.BusinessType;
import com.nft.common.enums.CollectStatus;
import com.nft.common.utils.file.FileUploadUtils;
import com.nft.common.utils.http.HttpUtils;
import com.nft.mall.domain.ProductCategory;
import com.nft.mall.domain.TInquiryRecord;
import com.nft.mall.service.ICollectionService;
import com.nft.mall.service.TInquiryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 商城藏品Controller
 *
 * @author nft
 * @date 2021-05-17
 */
@RestController
@RequestMapping("/mall/collection")
public class CollectionController extends BaseController {

    @Autowired
    private ICollectionService collectionService;
    @Autowired
    private TInquiryRecordService tInquiryRecordService;

    /**
     * 查询藏品分类列表
     */
    @Log(title = "查询藏品分类列表", businessType = BusinessType.GET)
    @GetMapping("/categoryList")
    public AjaxResult categoryList(ProductCategory productCategory) {
        return collectionService.getProductCategoryList(productCategory);
    }

    /**
     * 查询藏品列表
     */
    @Log(title = "查询藏品列表", businessType = BusinessType.GET)
    @GetMapping("/getMallCollectionList")
    public AjaxResult getMallCollectionList(CollectionSearchParam collectionSearchParam) {
//        collectionSearchParam.setStatus(CollectStatus.ONLINE.getCode());
        collectionSearchParam.setStatusSet(Sets.newHashSet(
                CollectStatus.ONLINE.getCode(),
                CollectStatus.TRADING.getCode(),
                CollectStatus.TRADED.getCode(),
                CollectStatus.OFFLINE.getCode(),
                CollectStatus.RELAUNCH.getCode()));

        return collectionService.getMallCollectionList(collectionSearchParam);
    }

    /**
     * 查询藏品交易记录
     */
    @Log(title = "查询藏品交易记录", businessType = BusinessType.GET)
    @GetMapping("/getCollectDealRecordList")
    public AjaxResult getCollectDealRecordList(CollectDealRecordParam collectDealRecordParam) {
        if (ObjectUtil.isNull(collectDealRecordParam)) {
            return AjaxResult.error("参数错误");
        }
        return collectionService.getCollectDealRecordList(collectDealRecordParam);
    }

    /**
     * 新增收藏夹
     */
    @Log(title = "新增收藏夹", businessType = BusinessType.INSERT)
    @PostMapping("/addCollectionDir")
    public AjaxResult addCollectionDir(@Validated @RequestBody CollectionDirParam collectionDirParam) {
        return collectionService.addCollectionDir(collectionDirParam);
    }

    /**
     * 用户收藏夹列表
     */
    @Log(title = "查询收藏夹列表", businessType = BusinessType.GET)
    @GetMapping("/getCollectionDirList")
    public AjaxResult getCollectionDirList(CollectionDirParam collectionDirParam) {
        return collectionService.getCollectionDirList(collectionDirParam);
    }

    /**
     * 新增藏品
     */
    @Log(title = "新增藏品", businessType = BusinessType.INSERT)
    @PostMapping("/addMallCollection")
    public AjaxResult addMallCollection(@Validated @RequestBody CollectionParam collectionParam) {
        return collectionService.addMallCollection(collectionParam);
    }

    @Log(title = "询价藏品", businessType = BusinessType.INSERT)
    @PostMapping("/inquiryCollection")
    public AjaxResult inquiryCollection(@Validated @RequestBody TInquiryRecord tInquiryRecord) {
        int effectNum = tInquiryRecordService.insertSelective(tInquiryRecord);
        return effectNum == 1 ? AjaxResult.success("询价藏品成功") : AjaxResult.error("询价藏品失败");
    }

    /**
     * 再次上架藏品
     */
    @Log(title = "再次上架藏品", businessType = BusinessType.INSERT)
    @PostMapping("/addMallCollectionAgain")
    public AjaxResult addMallCollectionAgain(@Validated @RequestBody CollectionAgainParam collectionAgainParam) {
        return collectionService.addMallCollectionAgain(collectionAgainParam);
    }

    /**
     * 获取藏品详细信息
     */
    @Log(title = "获取藏品详细信息", businessType = BusinessType.GET)
    @GetMapping(value = "/getMallCollectionInfo/{certificationId}/{productId}")
    public AjaxResult getMallCollectionInfo(@PathVariable("certificationId") Long certificationId, @PathVariable("productId") Long productId) {
        return collectionService.getMallCollectionById(certificationId, productId);
    }

    /**
     * 获取藏品作者信息
     */
    @Log(title = "获取藏品作者信息", businessType = BusinessType.GET)
    @GetMapping(value = "/getCollectAuthorInfo/{authorName}")
    public AjaxResult getCollectAuthorInfo(@PathVariable("authorName") String authorName) {
        return collectionService.getCollectAuthorByName(authorName);
    }

    /**
     * 获取藏品艺术家列表
     */
    @Log(title = "获取藏品艺术家列表", businessType = BusinessType.GET)
    @GetMapping(value = "/getCollectArtist")
    public AjaxResult getCollectArtist(@RequestParam("artName") String artName) {
        return collectionService.getCollectArtistByName(artName);
    }

    /**
     * 查询藏品是否点赞/收藏
     */
    @Log(title = "查询藏品是否点赞/收藏", businessType = BusinessType.GET)
    @GetMapping(value = "/checkIsFavoriteAndCollect/{certificationId}/{productId}")
    public AjaxResult checkIsFavoriteAndCollect(@PathVariable("certificationId") Long certificationId, @PathVariable("productId") Long productId) {
        return collectionService.checkIsFavoriteAndCollect(certificationId, productId);
    }

    /**
     * 查询同类型或者同作者藏品列表
     */
    @Log(title = "查询同类型或者同作者藏品列表", businessType = BusinessType.GET)
    @GetMapping("/getSameAuthorOrCategoryCollectionList")
    public AjaxResult getSameAuthorOrCategoryCollectionList(CollectionSearchParam collectionSearchParam) {
        collectionSearchParam.setStatus(CollectStatus.ONLINE.getCode());
        return collectionService.getSameAuthorOrCategoryCollectionList(collectionSearchParam);
    }

    /**
     * 新增藏品操作
     */
    @Log(title = "新增藏品操作", businessType = BusinessType.INSERT)
    @PostMapping("/addProductOpe")
    public AjaxResult addProductOpe(@Validated @RequestBody CollectionOpeParam productOpeInfo) {
        return collectionService.addProductOpe(productOpeInfo);
    }

    /**
     * 我的收藏/点赞列表
     */
    @Log(title = "我的收藏/点赞列表", businessType = BusinessType.GET)
    @GetMapping("/myCollectionOpeList")
    public AjaxResult myCollectionOpeList(CollectionSearchParam collectionSearchParam) {
        return collectionService.myCollectionOpeList(collectionSearchParam);
    }

    /**
     * 我的NFT列表
     */
    @Log(title = "我的NFT列表", businessType = BusinessType.GET)
    @GetMapping("/myNftList")
    public AjaxResult myNftList(CollectionSearchParam collectionSearchParam) {
        collectionSearchParam.setStatusSet(Sets.newHashSet(
                CollectStatus.ONLINE.getCode(),
                CollectStatus.TRADING.getCode(),
                CollectStatus.TRADED.getCode(),
                CollectStatus.OFFLINE.getCode(),
                CollectStatus.RELAUNCH.getCode()));

        return collectionService.myNftList(collectionSearchParam);
    }

    /**
     * 上传藏品文件
     */
    @Log(title = "上传藏品文件", businessType = BusinessType.INSERT)
    @PostMapping("/uploadCollectFile")
    public AjaxResult uploadCollectFile(@RequestParam("collectFile") MultipartFile collectFile) throws IOException {
        if (!collectFile.isEmpty()) {
            String fileUrl = FileUploadUtils.upload(NftConfig.getAvatarPath(), collectFile);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileUrl", fileUrl);
            return ajax;
        }
        return AjaxResult.error("上传文件异常");
    }

    /**
     * 下单购买藏品
     */
    @Log(title = "下单购买藏品", businessType = BusinessType.INSERT)
    @PostMapping("/buyCollect")
    public AjaxResult buyCollect(@Validated @RequestBody CollectionBuyParam collectionBuyParam) {
        return collectionService.buyCollect(collectionBuyParam);
    }

    /**
     * 更新藏品交易状态
     */
    @Log(title = "更新藏品交易状态", businessType = BusinessType.UPDATE)
    @PostMapping("/updateCollectTrade")
    public AjaxResult updateCollectTrade(@Validated @RequestBody CollectionTradeParam collectionTradeParam) {
        return collectionService.updateCollectTrade(collectionTradeParam);
    }

    /**
     * 更新藏品状态
     */
    @Log(title = "更新藏品状态", businessType = BusinessType.UPDATE)
    @PostMapping("/updateCollectionStatus")
    public AjaxResult updateCollectionStatus(Long productId, Integer status) {
        if (ObjectUtil.isNull(productId) || ObjectUtil.isNull(status)) {
            return AjaxResult.error("参数错误");
        }
        CollectStatus statusEnum = CollectStatus.getByCode(status);
        if (ObjectUtil.isNull(statusEnum)) {
            return AjaxResult.error("参数错误");
        }
        return collectionService.updateCollectionStatus(productId, status);
    }

    /**
     * 查询藏品交易列表
     */
    @Log(title = "查询藏品交易列表", businessType = BusinessType.GET)
    @GetMapping("/getCollectionTradeList")
    public AjaxResult getCollectionTradeList(CollectionTradeSearchParam collectionTradeSearchParam) {
        return collectionService.getCollectionTradeList(collectionTradeSearchParam);
    }

    /**
     * 获取币种配置信息
     */
    @Log(title = "获取币种配置信息", businessType = BusinessType.GET)
    @GetMapping("/getCoinConfig/{coinType}")
    public AjaxResult getCoinConfig(@PathVariable("coinType") Integer coinType) {
        if (ObjectUtil.isNull(coinType)) {
            return AjaxResult.error("参数错误");
        }
        return collectionService.getCoinConfig(coinType);
    }

    /**
     * 撤回藏品
     */
    @PostMapping("/revokeMallCollection")
    public AjaxResult revokeMallCollection(@Validated @RequestBody RevokeParam revokeParam) {
        return collectionService.revokeMallCollection(revokeParam);
    }

    /**
     * 导入藏品
     */
    @Log(title = "导入藏品", businessType = BusinessType.INSERT)
    @PostMapping("/importMallCollection")
    public AjaxResult importMallCollection(@Validated @RequestBody CollectionImportParam importParam) {
        return collectionService.importMallCollection(importParam);
    }

    /**
     * 上传资源到cloudflare
     */
    @GetMapping("/upload/{ipfsHash}")
    public AjaxResult uploadToResources(@PathVariable("ipfsHash") String ipfsHash) {
        if (StrUtil.isBlank(ipfsHash)) {
            return AjaxResult.error("Parameter error");
        }

        //        上传资源到cloudflare
        String ipfsHashRet = HttpUtils.sendPostCloud(ipfsHash);
        try{

//            if(ipfsHashRet.indexOf("Resource already exists") != -1) {
//                System.out.println("Resource already exists");
//            }
//
//            if(ipfsHashRet.indexOf("Client was sending upload too slowly") != -1) {
//                System.out.println("Client was sending upload too slowly");
//                return AjaxResult.error("上传失败!", ipfsHashRet);
//            }

            JSONObject jsonObject = JSONObject.parseObject(ipfsHashRet);
            System.out.println(jsonObject);
            if(jsonObject.getBoolean("success")) {
                return AjaxResult.success("上传成功!", jsonObject.getJSONObject("result"));
            }
            return AjaxResult.error("上传失败!", jsonObject);
        } catch (Exception e) {
            System.out.println(ipfsHashRet);
            return AjaxResult.error("上传失败!", ipfsHashRet);

        }
    }

    /**
     * 查询TokenID
     */
    @GetMapping("/checkTokenId/{tokenId}")
    public AjaxResult checkTokenId(@PathVariable("tokenId") String tokenId) {
        if (StrUtil.isBlank(tokenId)) {
            return AjaxResult.error("参数错误");
        }
        return collectionService.checkTokenId(tokenId);
    }

    /**
     * 查询藏品订单支付信息
     */
    @Log(title = "查询藏品订单支付信息", businessType = BusinessType.GET)
    @GetMapping("/checkProductPayInfo")
    public AjaxResult checkProductPayInfo(PayCheckParam payCheckParam) {
        if (ObjectUtil.isNull(payCheckParam) || ObjectUtil.isNull(payCheckParam.getCertificationId()) || ObjectUtil.isNull(payCheckParam.getProductId())) {
            return AjaxResult.error("Parameter error");
        }
        return collectionService.checkProductPayInfo(payCheckParam);
    }

}
