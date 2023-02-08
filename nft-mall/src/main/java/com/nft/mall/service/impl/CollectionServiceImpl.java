package com.nft.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.collect.*;
import com.nft.common.enums.*;
import com.nft.common.utils.CopyUtils;
import com.nft.common.utils.HttpClient;
import com.nft.common.utils.StringUtils;
import com.nft.common.utils.sign.Md5Utils;
import com.nft.common.utils.sql.SqlUtil;
import com.nft.common.utils.uuid.IdUtils;
import com.nft.mall.domain.*;
import com.nft.mall.domain.vo.*;
import com.nft.mall.mapper.*;
import com.nft.mall.service.IBalanceService;
import com.nft.mall.service.IBuildModelService;
import com.nft.mall.service.ICollectionService;
import com.nft.mall.service.IThirdPartyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商城藏品Service业务层处理
 *
 * @author nft
 * @date 2021-05-17
 */
@Slf4j
@Service
public class CollectionServiceImpl implements ICollectionService {

    @Value("${pay.bop.notifyurl}")
    private String notify_url;

    @Value("${pay.bop.expire}")
    private String expire;

    @Value("${pay.bop.h5_redirect_url}")
    private String h5_back_url;

    @Autowired
    private MoneyHistoryMapper moneyHistoryMapper;

    @Autowired
    private IBalanceService balanceService;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTokenMapper productTokenMapper;

    @Autowired
    private ProductTradeMapper productTradeMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductOpeMapper productOpeMapper;

    @Autowired
    private CertificationMapper certificationMapper;

    @Autowired
    private CertificationAttachMapper attachMapper;

    @Autowired
    private CollectDirMapper collectDirMapper;

    @Autowired
    private ProductCoinConfigMapper productCoinConfigMapper;

    @Autowired
    private CertificationWithdrawMapper withdrawMapper;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private RoughRecordMapper roughRecordMapper;

    @Autowired
    private IBuildModelService buildModelService;

    @Autowired
    private IThirdPartyService thirdPartyService;

    /**
     * 查询藏品分类列表
     *
     * @param productCategory 藏品分类
     * @return 结果
     */
    @Override
    public AjaxResult getProductCategoryList(ProductCategory productCategory) {
        List<ProductCategory> categoryList = productCategoryMapper.selectProductCategoryList(productCategory);
        List<ProductCategoryVO> categoryVOList = Lists.newArrayList();
        ProductCategoryVO categoryVO;
        for (ProductCategory category : categoryList) {
            categoryVO = new ProductCategoryVO();
            categoryVO.setCategoryName(category.getCategoryName());
            categoryVO.setProductCategoryId(String.valueOf(category.getProductCategoryId()));
            categoryVOList.add(categoryVO);
        }
        return AjaxResult.success(categoryVOList);
    }

    /**
     * 查询藏品列表
     *
     * @param collectionSearchParam 藏品
     * @return 结果
     */
    @Override
    public AjaxResult getMallCollectionList(CollectionSearchParam collectionSearchParam) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("totalCount", 0);
        resultMap.put("list", Lists.newArrayList());
        if (!StringUtils.isEmpty(collectionSearchParam.getKeyword())) {
            CertificationAttach attach = new CertificationAttach();
            attach.setNickName(collectionSearchParam.getKeyword());
            List<CertificationAttach> certificationAttaches = attachMapper.selectCertificationAttachList(attach);
            Set<Long> certIdSet = certificationAttaches.stream().map(CertificationAttach::getCertificationId).collect(Collectors.toSet());
            collectionSearchParam.setCertIdSet(certIdSet);
        }
        startPage(collectionSearchParam);
        // 查询藏品列表
//        List<Product> productList = productMapper.selectCollectionList(collectionSearchParam);
        List<Product> productList = productMapper.selectCollectionListCON(collectionSearchParam);

        if (CollectionUtil.isEmpty(productList)) {
            return AjaxResult.success("查询成功", resultMap);
        }
        PageInfo<Product> pageInfo = new PageInfo<>(productList);
        // 藏品ID列表
        Set<Long> productIdSet = productList.stream().map(Product::getProductId).collect(Collectors.toSet());
        // 藏品作者ID列表
        Set<Long> createIdSet = productList.stream().map(Product::getCreateId).collect(Collectors.toSet());
        // 藏品作者列表
        List<Certification> certificationList = createIdSet.size() == 0 ? Lists.newArrayList() : certificationMapper.selectByCertificateIdSet(createIdSet);
        Map<Long, Certification> certificationMap = CollUtil.isEmpty(certificationList) ? Maps.newHashMap() : certificationList.stream().collect(Collectors.toMap(Certification::getId, Function.identity()));
        // 藏品作者附属信息列表
        List<CertificationAttach> attachList = createIdSet.size() == 0 ? Lists.newArrayList() : attachMapper.selectByCertificationIdSet(createIdSet);
        Map<Long, CertificationAttach> attachMap = CollUtil.isEmpty(attachList) ? Maps.newHashMap() : attachList.stream().collect(Collectors.toMap(CertificationAttach::getCertificationId, Function.identity()));
        // 藏品分类ID列表
        Set<Long> categoryIdSet = productList.stream().map(Product::getProductCategoryId).collect(Collectors.toSet());
        // 查询藏品操作列表
        List<ProductOpe> productOpeList = productOpeMapper.selectByProductIdSet(productIdSet);
        // 藏品操作映射关系
        Map<Long, List<ProductOpe>> productOpeListMap = productOpeList.stream().collect(Collectors.groupingBy(ProductOpe::getProductId));
        // 查询分类列表
        List<ProductCategory> categoryList = productCategoryMapper.selectByCategoryIdSet(categoryIdSet);
        // 分类映射关系
        Map<Long, ProductCategory> categoryMap = categoryList.stream().collect(Collectors.toMap(ProductCategory::getProductCategoryId, Function.identity()));
        List<ProductVO> productVOList = CopyUtils.copyList(productList, ProductVO.class);
        // 用户ID
        Long certificationId = collectionSearchParam.getLoginId();
        List<ProductOpe> productOpeByUser = ObjectUtil.isNull(certificationId) ? Lists.newArrayList() : productOpeMapper.selectByCertificationIdSetAndTypeSet(Sets.newHashSet(certificationId), CollectOpeType.opeTypeSet());
        Map<Long, List<ProductOpe>> productOpeUserMap = productOpeByUser.stream().collect(Collectors.groupingBy(ProductOpe::getProductId));
        productVOList.stream().forEach(productVO -> {
            List<ProductOpe> opeListMap = productOpeListMap.getOrDefault(productVO.getProductId(), null);
            ProductCategory category = categoryMap.getOrDefault(productVO.getProductCategoryId(), null);
            productVO.setProductCategoryName(ObjectUtil.isNull(category) ? "" : StringUtils.trimToEmpty(category.getCategoryName()));
            if (CollectionUtil.isNotEmpty(opeListMap)) {
                // 点赞数量
                long favoriteCount = countProductOpe(opeListMap, CollectOpeType.FAVORITE.getCode());
                productVO.setFavoriteCount(favoriteCount);
                long collectCount = countProductOpe(opeListMap, CollectOpeType.COLLECT.getCode());
                // 收藏数量
                productVO.setCollectionCount(collectCount);
            }
            if (CollectionUtil.isNotEmpty(productOpeByUser)) {
                List<ProductOpe> productOpeUserMapList = productOpeUserMap.getOrDefault(productVO.getProductId(), Lists.newArrayList());
                List<ProductOpe> productOpeListByFavorite = productOpeUserMapList.stream()
                        .filter(productOpeUser -> ObjectUtil.isNotNull(productOpeUser) && productOpeUser.getOpeType().equals(CollectOpeType.FAVORITE.getCode()))
                        .collect(Collectors.toList());
                Set<Long> setByFavorite = productOpeListByFavorite.stream().map(ProductOpe::getCertificationId).collect(Collectors.toSet());
                productVO.setIsFavorite(setByFavorite.contains(certificationId) ? 1 : 0);
                List<ProductOpe> productOpeListByCollect = productOpeUserMapList.stream()
                        .filter(productOpeUser -> ObjectUtil.isNotNull(productOpeUser) && productOpeUser.getOpeType().equals(CollectOpeType.COLLECT.getCode()))
                        .collect(Collectors.toList());
                Set<Long> setByCollect = productOpeListByCollect.stream().map(ProductOpe::getCertificationId).collect(Collectors.toSet());
                productVO.setIsCollection(setByCollect.contains(certificationId) ? 1 : 0);
            }
            CertificationAttach attach = attachMap.getOrDefault(productVO.getCreateId(), null);
            String nickName = attach.getNickName();
            if (StringUtils.isBlank(nickName)) {
                Certification creator = certificationMap.get(productVO.getCreateId());
                nickName = creator.getMobile();
            }
            productVO.setProductCreatorNickName(nickName);
            productVO.setProductCreatorAvatar(ObjectUtil.isNull(attach) ? "" : StringUtils.trimToEmpty(attach.getAvatarUrl()));
        });
        resultMap.put("totalCount", pageInfo.getTotal());
        resultMap.put("list", productVOList);
        return AjaxResult.success("查询成功", resultMap);
    }

    /**
     * 查询藏品交易记录列表
     *
     * @param dealRecordParam 藏品
     * @return 结果
     */
    @Override
    public AjaxResult getCollectDealRecordList(CollectDealRecordParam dealRecordParam) {
        // 查询用户信息
        Certification certification = null;
        if (dealRecordParam.getCertificationId() > 0) {
            certification = certificationMapper.selectCertificationById(dealRecordParam.getCertificationId());
            if (ObjectUtil.isNull(certification)) {
                return AjaxResult.error("用户不存在");
            }
        }
        // 查询Token铸造信息
        String tokenId = dealRecordParam.getTokenId();
        List<ProductToken> productTokenList = productTokenMapper.selectByTokenId(tokenId);
        if (CollUtil.isEmpty(productTokenList)) {
            return AjaxResult.success("查询成功", Lists.newArrayList());
        }
        Set<Long> productIdSet = productTokenList.stream().map(ProductToken::getProductId).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);

//        Map<Long, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getProductId,Product::getProductId, (oldVal, currVal) -> currVal, Function.identity()));
        Map<Long, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getProductId, Function.identity(), (oldVal, currVal) -> currVal));
        // 查询Token交易记录
//        List<ProductTrade> dealRecordList = productTradeMapper.selectByContractAddressAndTokenId(dealRecordParam.getContractAddress(), tokenId);
        List<ProductTrade> dealRecordList = productTradeMapper.selectByContractAddressAndTokenIdCON(dealRecordParam.getContractAddress(), tokenId);
        ProductTrade productTrade;
        for (ProductToken token : productTokenList) {
            productTrade = new ProductTrade();
            productTrade.setTradeHash(token.getTradeHash());
            productTrade.setToId(token.getCreateId());
            Product product = productMap.getOrDefault(token.getProductId(), null);
            productTrade.setPrice(ObjectUtil.isNull(product) ? token.getFee() : product.getPrice());
            productTrade.setCreateTime(token.getCreateTime());
            productTrade.setCoinType(token.getCoinType());
            dealRecordList.add(productTrade);
        }
        if (CollectionUtil.isEmpty(dealRecordList)) {
            return AjaxResult.success("查询成功", Lists.newArrayList());
        }
        // 藏品拥有者ID集合
        Set<Long> ownerIdSet = dealRecordList.stream().map(ProductTrade::getToId).collect(Collectors.toSet());
        List<Certification> ownerList = certificationMapper.selectByCertificateIdSet(ownerIdSet);
        Map<Long, Certification> ownerMap = ownerList.stream().collect(Collectors.toMap(Certification::getId, Function.identity()));
        // 藏品作者附属信息列表
        List<CertificationAttach> attachList = ownerIdSet.size() == 0 ? Lists.newArrayList() : attachMapper.selectByCertificationIdSet(ownerIdSet);
        Map<Long, CertificationAttach> attachMap = CollUtil.isEmpty(attachList) ? Maps.newHashMap() : attachList.stream().collect(Collectors.toMap(CertificationAttach::getCertificationId, Function.identity()));
        List<CollectDealRecordInfo> recordInfoList = CopyUtils.copyList(dealRecordList, CollectDealRecordInfo.class);
        recordInfoList.stream().forEach(recordInfo -> {
            Certification owner = ownerMap.getOrDefault(recordInfo.getToId(), null);
            recordInfo.setOwner(ObjectUtil.isNull(owner) ? "" : owner.getRealName());
            if (StringUtils.isBlank(recordInfo.getTradeHash())) {
                recordInfo.setTradeHash(recordInfo.getToAddress());
            }
            CertificationAttach attach = attachMap.getOrDefault(owner.getId(), null);
            String nickName = attach.getNickName();
            if (StringUtils.isBlank(nickName)) {
                nickName = owner.getMobile();
            }
            recordInfo.setOwnerNickName(nickName);
        });
        recordInfoList = recordInfoList.stream().sorted(Comparator.comparing(CollectDealRecordInfo::getCreateTime).reversed()).collect(Collectors.toList());
        return AjaxResult.success("查询成功", recordInfoList);
    }

    /**
     * 新增收藏夹
     *
     * @param collectionDirParam 收藏夹
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult addCollectionDir(CollectionDirParam collectionDirParam) {
        // 查询用户
        Long certificationId = collectionDirParam.getCertificationId();
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }

        // 查询收藏夹是否已存在
        CollectDir existDir = collectDirMapper.selectByCertificationIdAndNameAndType(certificationId, collectionDirParam.getTokenName(), collectionDirParam.getTokenType());
        if (ObjectUtil.isNotNull(existDir)) {
            return AjaxResult.error("文件夹已存在");
        }
        // 新建收藏夹
        CollectDir collectDir = CopyUtils.copy(collectionDirParam, CollectDir.class);
        collectDir.setCertificationId(collectionDirParam.getCertificationId());
        collectDir.setCreateTime(new Date());
        collectDir.setStatus(CollectDirStatus.YES.getCode());
        collectDirMapper.insertCollectDir(collectDir);

        // 新增出账记录
        CertificationWithdraw withdraw = buildModelService.buildCertificationWithdraw(certification, collectionDirParam.getFee(), WithdrawType.FAVORITE.getCode(), WithdrawStatus.YES.getCode());
        int effectNum = withdrawMapper.insertCertificationWithdraw(withdraw);
        return effectNum == 1 ? AjaxResult.success("创建成功", collectDir.getCollectDirId()) : AjaxResult.error("创建失败");
    }

    /**
     * 查询收藏夹列表
     *
     * @param collectionDirParam 收藏夹
     * @return 结果
     */
    @Override
    public AjaxResult getCollectionDirList(CollectionDirParam collectionDirParam) {
        if (ObjectUtil.isNull(collectionDirParam.getCertificationId())) {
            return AjaxResult.error("参数错误");
        }
        Certification certification = certificationMapper.selectCertificationById(collectionDirParam.getCertificationId());
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 查询用户收藏夹列表
        CollectDir collectDir = CopyUtils.copy(collectionDirParam, CollectDir.class);
        List<CollectDir> collectDirList = collectDirMapper.selectCollectDirList(collectDir);
        List<CollectionDirInfo> dirInfoList = CopyUtils.copyList(collectDirList, CollectionDirInfo.class);
        return AjaxResult.success("查询成功", dirInfoList);
    }

    /**
     * 我的收藏列表
     *
     * @param collectionSearchParam 查询信息
     * @return 结果
     */
    @Override
    public AjaxResult myCollectionOpeList(CollectionSearchParam collectionSearchParam) {
        // 用户ID
        Long certificationId = collectionSearchParam.getCertificationId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("totalCount", 0);
        resultMap.put("list", Lists.newArrayList());
        // 藏品操作列表
        List<ProductOpe> productOpeList = productOpeMapper.selectByCertificationIdSetAndTypeSet(Sets.newHashSet(certificationId), Sets.newHashSet(collectionSearchParam.getOpeType()));
        if (CollectionUtil.isEmpty(productOpeList)) {
            return AjaxResult.success("查询成功", resultMap);
        }
        Set<Long> productIdSet = productOpeList.stream().map(ProductOpe::getProductId).collect(Collectors.toSet());
        startPage(collectionSearchParam);
        // 藏品列表
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        PageInfo<Product> pageInfo = new PageInfo<>(productList);
        // 藏品作者ID列表
        Set<Long> createIdSet = productList.stream().map(Product::getCreateId).collect(Collectors.toSet());
        // 藏品作者列表
        List<Certification> certificationList = createIdSet.size() == 0 ? Lists.newArrayList() : certificationMapper.selectByCertificateIdSet(createIdSet);
        Map<Long, Certification> certificationMap = CollUtil.isEmpty(certificationList) ? Maps.newHashMap() : certificationList.stream().collect(Collectors.toMap(Certification::getId, Function.identity()));
        // 藏品作者附属信息列表
        List<CertificationAttach> attachList = createIdSet.size() == 0 ? Lists.newArrayList() : attachMapper.selectByCertificationIdSet(createIdSet);
        Map<Long, CertificationAttach> attachMap = CollUtil.isEmpty(attachList) ? Maps.newHashMap() : attachList.stream().collect(Collectors.toMap(CertificationAttach::getCertificationId, Function.identity()));
        // 藏品分类ID列表
        Set<Long> categoryIdSet = productList.stream().map(Product::getProductCategoryId).collect(Collectors.toSet());
        productOpeList = productOpeMapper.selectByCertificationIdSetAndTypeSet(Sets.newHashSet(collectionSearchParam.getCertificationId()), Sets.newHashSet(CollectOpeType.opeTypeSet()));
        // 藏品操作映射关系
        Map<Long, List<ProductOpe>> productOpeListMap = productOpeList.stream().collect(Collectors.groupingBy(ProductOpe::getProductId));
        // 查询分类列表
        List<ProductCategory> categoryList = productCategoryMapper.selectByCategoryIdSet(categoryIdSet);
        // 分类映射关系
        Map<Long, ProductCategory> categoryMap = categoryList.stream().collect(Collectors.toMap(ProductCategory::getProductCategoryId, Function.identity()));
        List<ProductVO> productVOList = CopyUtils.copyList(productList, ProductVO.class);
        productVOList.stream().forEach(productVO -> {
            List<ProductOpe> opeListMap = productOpeListMap.getOrDefault(productVO.getProductId(), null);
            ProductCategory category = categoryMap.getOrDefault(productVO.getProductCategoryId(), null);
            productVO.setProductCategoryName(ObjectUtil.isNull(category) ? "" : StringUtils.trimToEmpty(category.getCategoryName()));
            if (CollectionUtil.isNotEmpty(opeListMap)) {
                List<ProductOpe> productOpeListByFavorite = opeListMap.stream()
                        .filter(productOpeUser -> ObjectUtil.isNotNull(productOpeUser) && productOpeUser.getOpeType().equals(CollectOpeType.FAVORITE.getCode()))
                        .collect(Collectors.toList());
                Set<Long> setByFavorite = productOpeListByFavorite.stream().map(ProductOpe::getCertificationId).collect(Collectors.toSet());
                productVO.setIsFavorite(setByFavorite.contains(certificationId) ? 1 : 0);
                // 点赞数量
                long favoriteCount = countProductOpe(opeListMap, CollectOpeType.FAVORITE.getCode());
                productVO.setFavoriteCount(favoriteCount);
                List<ProductOpe> productOpeListByCollect = opeListMap.stream()
                        .filter(productOpeUser -> ObjectUtil.isNotNull(productOpeUser) && productOpeUser.getOpeType().equals(CollectOpeType.COLLECT.getCode()))
                        .collect(Collectors.toList());
                Set<Long> setByCollect = productOpeListByCollect.stream().map(ProductOpe::getCertificationId).collect(Collectors.toSet());
                productVO.setIsCollection(setByCollect.contains(certificationId) ? 1 : 0);
                long collectionCount = countProductOpe(opeListMap, CollectOpeType.COLLECT.getCode());
                // 收藏数量
                productVO.setCollectionCount(collectionCount);
            } else {
                productVO.setFavoriteCount(0L);
                productVO.setCollectionCount(0L);
            }
            CertificationAttach attach = attachMap.getOrDefault(productVO.getCreateId(), null);
            String nickName = attach.getNickName();
            if (StringUtils.isBlank(nickName)) {
                Certification creator = certificationMap.get(productVO.getCreateId());
                nickName = creator.getMobile();
            }
            productVO.setProductCreatorNickName(nickName);
            productVO.setProductCreatorAvatar(ObjectUtil.isNull(attach) ? "" : StringUtils.trimToEmpty(attach.getAvatarUrl()));
        });
        resultMap.put("totalCount", pageInfo.getTotal());
        resultMap.put("list", productVOList);
        return AjaxResult.success("查询成功", resultMap);
    }

    /**
     * 我的NFT列表
     *
     * @param collectionSearchParam 藏品信息
     * @return 结果
     */
    @Override
    public AjaxResult myNftList(CollectionSearchParam collectionSearchParam) {
        // 用户ID
        Long certificationId = collectionSearchParam.getCertificationId();
        // 操作类型
        Integer opeType = collectionSearchParam.getOpeType();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("totalCount", 0);
        resultMap.put("list", Lists.newArrayList());
        Set<Long> productIdSet = Sets.newHashSet();
        // 我买入的
//        List<ProductTrade> productTradeListByBuy = productTradeMapper.selectByCertificationIdSetAndStatus(Sets.newHashSet(certificationId), Sets.newHashSet(CollectTradeStatus.SUCCESS.getCode()));
//        Map<String, Long> stringIntegerMap = new HashMap<>();
//        for (ProductTrade productTrade : productTradeListByBuy) {
//            stringIntegerMap.put(productTrade.getTokenId(), productTrade.getProductId());
//        }
//        Collection<Long> productIdSetByBuy = stringIntegerMap.values();

        Set<Long> holdProductIdSet = Sets.newHashSet();
        if (opeType.equals(CollectOpeType.BUY.getCode())) {
            /**
             * 现持有(Buy(未二次上架)+我创建的(未交易的))
             */
            // 我买入的(未二次上架+上架售卖中)
//            for (Long productIdByBuy : productIdSetByBuy) {
//                Product product = productMapper.selectByParentProductId(productIdByBuy);
//                // 未二次上架
//                if (ObjectUtil.isNull(product)) {
//                    holdProductIdSet.add(productIdByBuy);
//                    continue;
//                }
//            }
            // 我创建的(未交易的)
            List<Product> createProductListByOnline = productMapper.selectByCreateIdSetAndsourceProductIdSet1(Sets.newHashSet(certificationId), 0);
            Set<Long> productIdSetByOnline = createProductListByOnline.stream().map(Product::getProductId).collect(Collectors.toSet());
            holdProductIdSet.addAll(productIdSetByOnline);
            productIdSet = holdProductIdSet;
        }
        if (opeType.equals(CollectOpeType.CREATE.getCode())) {  // sold out
            // 我买入的(已二次上架交易)
//            for (Long productIdByBuy : productIdSetByBuy) {
//                Product product = productMapper.selectByParentProductId(productIdByBuy);
//                if (ObjectUtil.isNotNull(product) && product.getStatus().equals(CollectStatus.TRADED.getCode())) {
//                    holdProductIdSet.add(product.getProductId());
//                    continue;
//                }
//            }
            // 我创建的(已交易的))
            List<Product> productListBySold = productMapper.selectByCreateIdSetAndsourceProductIdSet(Sets.newHashSet(certificationId), 1);

            Set<Long> productIdSetByOnline = productListBySold.stream().map(Product::getProductId).collect(Collectors.toSet());
            holdProductIdSet.addAll(productIdSetByOnline);
            productIdSet = holdProductIdSet;
        }
        if (CollectionUtil.isEmpty(productIdSet)) {
            return AjaxResult.success("查询成功", resultMap);
        }
        // 藏品操作列表
        List<ProductOpe> productOpeList = productOpeMapper.selectByProductIdSet(productIdSet);
        if (CollectionUtil.isEmpty(productOpeList)) {
            return AjaxResult.success("查询成功", resultMap);
        }
        startPage(collectionSearchParam);
        // 藏品列表
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);

        PageInfo<Product> pageInfo = new PageInfo<>(productList);
        // 藏品作者ID列表
        Set<Long> createIdSet = productList.stream().map(Product::getCreateId).collect(Collectors.toSet());
        // 藏品作者列表
        List<Certification> certificationList = createIdSet.size() == 0 ? Lists.newArrayList() : certificationMapper.selectByCertificateIdSet(createIdSet);
        Map<Long, Certification> certificationMap = CollUtil.isEmpty(certificationList) ? Maps.newHashMap() : certificationList.stream().collect(Collectors.toMap(Certification::getId, Function.identity()));
        // 藏品作者附属信息列表
        List<CertificationAttach> attachList = createIdSet.size() == 0 ? Lists.newArrayList() : attachMapper.selectByCertificationIdSet(createIdSet);
        Map<Long, CertificationAttach> attachMap = CollUtil.isEmpty(attachList) ? Maps.newHashMap() : attachList.stream().collect(Collectors.toMap(CertificationAttach::getCertificationId, Function.identity()));
        // 藏品分类ID列表
        Set<Long> categoryIdSet = productList.stream().map(Product::getProductCategoryId).collect(Collectors.toSet());
        // 藏品操作映射关系
        Map<Long, List<ProductOpe>> productOpeListMap = productOpeList.stream().collect(Collectors.groupingBy(ProductOpe::getProductId));
        // 查询分类列表
        List<ProductCategory> categoryList = productCategoryMapper.selectByCategoryIdSet(categoryIdSet);
        // 分类映射关系
        Map<Long, ProductCategory> categoryMap = categoryList.stream().collect(Collectors.toMap(ProductCategory::getProductCategoryId, Function.identity()));
        List<ProductVO> productVOList = CopyUtils.copyList(productList, ProductVO.class);
        productVOList.stream().forEach(productVO -> {
            List<ProductOpe> opeListMap = productOpeListMap.getOrDefault(productVO.getProductId(), null);
            ProductCategory category = categoryMap.getOrDefault(productVO.getProductCategoryId(), null);
            productVO.setProductCategoryName(ObjectUtil.isNull(category) ? "" : StringUtils.trimToEmpty(category.getCategoryName()));
            boolean isFrom = productVO.getCreateId().equals(certificationId);
            int productProm = CollectFrom.CASTING.getCode();
            if (!isFrom) {
                Product product = productMapper.selectByParentProductId(productVO.getProductId());
                productProm = ObjectUtil.isNull(product) ? CollectFrom.BUY.getCode() : productProm;
            }
            CollectFrom collectFrom = CollectFrom.getByCode(productProm);
            productVO.setProductFrom(collectFrom.getCode());
            productVO.setProductFromDesc(collectFrom.getMessage());
            if (CollectionUtil.isNotEmpty(opeListMap)) {
                // 点赞数量
                long favoriteCount = countProductOpe(opeListMap, CollectOpeType.FAVORITE.getCode());
                productVO.setFavoriteCount(favoriteCount);
                long collectionCount = countProductOpe(opeListMap, CollectOpeType.COLLECT.getCode());
                // 收藏数量
                productVO.setCollectionCount(collectionCount);
            } else {
                productVO.setFavoriteCount(0L);
                productVO.setCollectionCount(0L);
            }
            CertificationAttach attach = attachMap.getOrDefault(productVO.getCreateId(), null);
            String nickName = attach.getNickName();
            if (StringUtils.isBlank(nickName)) {
                Certification creator = certificationMap.get(productVO.getCreateId());
                nickName = creator.getMobile();
            }
            productVO.setProductCreatorNickName(nickName);
            productVO.setProductCreatorAvatar(ObjectUtil.isNull(attach) ? "" : StringUtils.trimToEmpty(attach.getAvatarUrl()));
        });
        resultMap.put("totalCount", pageInfo.getTotal());
        resultMap.put("list", productVOList);
        return AjaxResult.success("查询成功", resultMap);
    }

    /**
     * 新增藏品
     *
     * @param collectionParam 藏品
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult addMallCollection(CollectionParam collectionParam) {
        // 用户ID
        Long createId = collectionParam.getCreateId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(createId);
        if (ObjectUtil.isNull(certification)) {
//            System.out.println("用户不存在");
            return AjaxResult.error("用户不存在");

        }
        if (!ReleaseStatus.checkRelease(certification.getReleaseStatus())) {
            return AjaxResult.error("没有发布权限");
        }

        // 查询收藏夹信息
        Long collectDirId = collectionParam.getCollectDirId();
        CollectDir collectDir = collectDirMapper.selectCollectDirById(collectDirId);
        if (ObjectUtil.isNull(collectDir)) {
            return AjaxResult.error("收藏夹不存在");
        }

        // 查询Token ID信息
//        String tokenId = Web3jUtils.getTokenId(collectionParam.getTradeHash());
//        if ("0".equals(tokenId)) {
//            return AjaxResult.error("token信息异常,铸造失败");
//        }

//        collectionParam.setTokenId(tokenId);

        // 新增藏品
        Product product = CopyUtils.copy(collectionParam, Product.class);
        product.setParentProductId(0L);
        product.setSourceProductId(createId);
        product.setCreateTime(new Date());
        productMapper.insertProduct(product);

        // 新增ProductToken信息
        ProductToken productToken = buildModelService.buildProductToken(product.getProductId(), collectionParam);
        productTokenMapper.insertProductToken(productToken);

        // 新增藏品操作信息
        ProductOpe productOpe = buildModelService.buildProductOpe(createId, product.getProductId(), CollectOpeType.CREATE.getCode());
        int effectNum = productOpeMapper.insertProductOpe(productOpe);

        // 新增出账记录
//        CertificationWithdraw withdraw = buildModelService.buildCertificationWithdraw(certification, collectionParam.getFee(), WithdrawType.COLLECTION.getCode(), WithdrawStatus.YES.getCode());
//        int effectNum = withdrawMapper.insertCertificationWithdraw(withdraw);
        // 发送邮件
        thirdPartyService.sendHtmlMailForMoldUp(certification.getMobile());
        return effectNum == 1 ? AjaxResult.success("新增藏品成功") : AjaxResult.error("新增藏品失败");
    }

    /**
     * 再次上架藏品
     *
     * @param collectionAgainParam 藏品
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult addMallCollectionAgain(CollectionAgainParam collectionAgainParam) {
        // 用户ID
        Long createId = collectionAgainParam.getCreateId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(createId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        // 藏品ID
        Long productId = collectionAgainParam.getProductId();
        // 查询藏品信息
        Product existProduct = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(existProduct)) {
            return AjaxResult.error("藏品不存在");
        }
        // 查询收藏夹信息
        Long collectDirId = collectionAgainParam.getCollectDirId();
        CollectDir collectDir = collectDirMapper.selectCollectDirById(collectDirId);
        if (ObjectUtil.isNull(collectDir)) {
            return AjaxResult.error("收藏夹不存在");
        }

        // 新增藏品
//        Product product = buildModelService.buildProduct(existProduct, collectionAgainParam, certification.getRealName(), CollectStatus.ONLINE.getCode());
//        productMapper.insertProduct(product);
//        Product product = buildModelService.buildProduct(existProduct, collectionAgainParam, certification.getRealName(), CollectStatus.ONLINE.getCode());
        existProduct.setStatus(CollectStatus.ONLINE.getCode());
        existProduct.setSourceProductId(createId);
        existProduct.setPrice(collectionAgainParam.getPrice());
        productMapper.updateProduct(existProduct);

        // 新增ProductToken信息
        collectionAgainParam.setProductId(existProduct.getProductId());
        ProductToken productToken = buildModelService.buildProductToken(collectionAgainParam);
        productTokenMapper.insertProductToken(productToken);

        // 新增藏品操作信息
        ProductOpe productOpe = buildModelService.buildProductOpe(createId, existProduct.getProductId(), CollectOpeType.ONLINE.getCode());
        productOpeMapper.insertProductOpe(productOpe);

        // 新增出账记录
        CertificationWithdraw withdraw = buildModelService.buildCertificationWithdraw(certification, collectionAgainParam.getFee(), WithdrawType.ONLINE.getCode(), WithdrawStatus.YES.getCode());
        int effectNum = withdrawMapper.insertCertificationWithdraw(withdraw);
        return effectNum == 1 ? AjaxResult.success("藏品上架成功") : AjaxResult.error("藏品上架失败");
    }

    /**
     * 获取藏品详情
     *
     * @param productId 藏品id
     * @return 结果
     */
    @Override
    public AjaxResult getMallCollectionById(Long certificationId, Long productId) {
        // 查询用户信息
        Certification certification = null;
        if (certificationId > 0) {
            certification = certificationMapper.selectCertificationById(certificationId);
            if (ObjectUtil.isNull(certification)) {
                return AjaxResult.error("用户不存在");
            }
        }
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        // 查询藏品创建者信息
        Certification owner = certificationMapper.selectCertificationById(product.getCreateId());
        if (ObjectUtil.isNull(owner)) {
            return AjaxResult.error("藏品不存在");
        }
        // 查询藏品Token信息
        List<ProductToken> tokenList = productTokenMapper.selectByProductId(productId);
        if (ObjectUtil.isNull(tokenList)) {
            return AjaxResult.error("藏品不存在");
        }
        // 查询藏品收藏夹信息
        CollectDir collectDir = collectDirMapper.selectCollectDirById(tokenList.get(tokenList.size() - 1).getCollectDirId());
        if (ObjectUtil.isNull(collectDir)) {
            return AjaxResult.error("藏品不存在");
        }
        // 查询用户附加信息
        List<CertificationAttach> attachList = Lists.newArrayList();
        if (ObjectUtil.isNotNull(certification)) {
            attachList = attachMapper.selectByCertificationIdSet(Sets.newHashSet(certificationId));
            if (CollectionUtil.isEmpty(attachList)) {
                return AjaxResult.error("藏品不存在");
            }
        }
        // 查询藏品分类信息
        ProductCategory category = productCategoryMapper.selectProductCategoryById(product.getProductCategoryId());
        ProductVO productVO = CopyUtils.copy(product, ProductVO.class);
        // 用户余额
//        productVO.setBalance(ObjectUtil.isNull(certification) ? BigDecimal.ZERO : attachList.get(tokenList.size() - 1 ).getBalance());
        // 藏品Token
        productVO.setTokenId(tokenList.get(tokenList.size() - 1).getTokenId());
        // 藏品上架状态
        productVO.setOpeType(tokenList.get(tokenList.size() - 1).getOpeType());
        // 收藏夹ID
        productVO.setCollectDirId(collectDir.getCollectDirId());
        // 收藏夹名称
        productVO.setCollectDirName(collectDir.getTokenName());
        // 藏品创建时间
        productVO.setTokenTime(DateUtil.formatDateTime(product.getCreateTime()));
        // 藏品合约地址
        productVO.setContractAddress(collectDir.getContractAddress());
        // 查询邀请人信息
        Certification invitor = ObjectUtil.isNull(certification) ? null : certificationMapper.selectByMyCode(certification.getOtherCode());
        productVO.setInvitorAddress(ObjectUtil.isNull(invitor) ? owner.getWalletAddress() : invitor.getWalletAddress());
        // 分类名称
        productVO.setProductCategoryName(ObjectUtil.isNull(category) ? "" : StringUtils.trimToEmpty(category.getCategoryName()));
        // 藏品创建者简介
        List<CertificationAttach> ownerAttachList = attachMapper.selectByCertificationIdSet(Sets.newHashSet(product.getCreateId()));
        productVO.setProductCreatorIntro(CollUtil.isEmpty(ownerAttachList) ? "" : StringUtils.trimToEmpty(ownerAttachList.get(0).getIntroduction()));
        // 藏品创建者昵称
        String creatorNickName = owner.getMobile();
        if (CollUtil.isNotEmpty(ownerAttachList)) {
            CertificationAttach ownerAttach = ownerAttachList.get(0);
            creatorNickName = StringUtils.isBlank(ownerAttach.getNickName()) ? creatorNickName : ownerAttach.getNickName();
            productVO.setProductCreatorAvatar(StringUtils.trimToEmpty(ownerAttach.getAvatarUrl()));
        }
        productVO.setProductCreatorNickName(creatorNickName);
        boolean isFrom = productVO.getCreateId().equals(certificationId);    // 判断创建者和持有者是否是同一个人
        int productProm = CollectFrom.CASTING.getCode();
//   1       无法售出      false      不是我的
//    2      可以售出   true    是我的
        if (!isFrom) {
            Product existProduct = productMapper.selectByParentProductId(productVO.getProductId());
            productProm = ObjectUtil.isNull(existProduct) ? CollectFrom.BUY.getCode() : productProm;
        }
        productVO.setProductFrom(productProm);

        productVO.setCoinType(tokenList.get(tokenList.size() - 1).getCoinType());
        productVO.setSignatureLeft(tokenList.get(tokenList.size() - 1).getSignatureLeft());
        productVO.setOrderLeft(tokenList.get(tokenList.size() - 1).getOrderLeft());
        productVO.setRemark(tokenList.get(tokenList.size() - 1).getRemark());

        // 查询藏品是否交易
        ProductTrade productTrade = productTradeMapper.selectByProductId(productId);
        if (ObjectUtil.isNotNull(productTrade) && ObjectUtil.isNotNull(certification)) {
            if (productTrade.getStatus().equals(CollectTradeStatus.FAIL.getCode())) {
                productVO.setTradeStatus(1);
            } else {
                productVO.setTradeStatus(certificationId.equals(productTrade.getToId()) ? 1 : 2);
            }
        }
        return AjaxResult.success("查询成功", productVO);
    }

    /**
     * 获取藏品作者信息
     *
     * @param authorName 藏品作者
     * @return 结果
     */
    @Override
    public AjaxResult getCollectAuthorByName(String authorName) {
        // 查询藏品信息
        Product product = productMapper.selectProductByAuthor(authorName);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        ProductAuthorVO authorVO = new ProductAuthorVO();
        authorVO.setAuthor(StringUtils.trimToEmpty(product.getProductAuthor()));
        authorVO.setAuthorDesc(StringUtils.trimToEmpty(product.getProductAuthorDesc()));
        return AjaxResult.success(authorVO);
    }

    /**
     * 获取藏品艺术家信息
     *
     * @param artName 艺术家名称
     * @return 结果
     */
    @Override
    public AjaxResult getCollectArtistByName(String artName) {
        List<ProductAuthorVO> resultList = Lists.newArrayList();
        List<String> artNameList = Lists.newArrayList();
        // 查询艺术家列表
        List<ProductAuthorVO> authorVOList = productMapper.selectProductListByArt(artName.trim());
        for (ProductAuthorVO art : authorVOList) {
            String artAuthor = StringUtils.trimToNull(art.getAuthor());
            if (StringUtils.isNotBlank(artAuthor) && !artNameList.contains(artAuthor)) {
                resultList.add(art);
                artNameList.add(artAuthor);
            }
        }
        return AjaxResult.success(resultList);
    }

    /**
     * 查询藏品是否点赞/收藏
     *
     * @param certificationId 用户id
     * @param productId       藏品id
     * @return 结果
     */
    @Override
    public AjaxResult checkIsFavoriteAndCollect(Long certificationId, Long productId) {
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        // 藏品封装信息
        ProductVO productVO = CopyUtils.copy(product, ProductVO.class);
        // 操作列表
        List<ProductOpe> productOpeList = productOpeMapper.selectByProductIdSet(Sets.newHashSet(productId));
        // 查询用户点赞信息
        ProductOpe productOpeByFavorite = productOpeMapper.selectByProductIdAndCertificationIdAndType(productId, certificationId, CollectOpeType.FAVORITE.getCode());
        productVO.setIsFavorite(ObjectUtil.isNull(productOpeByFavorite) ? 0 : 1);
        // 查询用户收藏信息
        ProductOpe productOpeByCollect = productOpeMapper.selectByProductIdAndCertificationIdAndType(productId, certificationId, CollectOpeType.COLLECT.getCode());
        productVO.setIsCollection(ObjectUtil.isNull(productOpeByCollect) ? 0 : 1);
        // 点赞数量
        long favoriteCount = countProductOpe(productOpeList, CollectOpeType.FAVORITE.getCode());
        productVO.setFavoriteCount(favoriteCount);
        long collectCount = countProductOpe(productOpeList, CollectOpeType.COLLECT.getCode());
        // 收藏数量
        productVO.setCollectionCount(collectCount);
        return AjaxResult.success("查询成功", productVO);
    }

    /**
     * 查询同类型或者同作者藏品列表
     *
     * @param collectionSearchParam 藏品
     * @return 结果
     */
    @Override
    public AjaxResult getSameAuthorOrCategoryCollectionList(CollectionSearchParam collectionSearchParam) {
        ProductVO productVO = new ProductVO();
        // 状态
        Integer status = collectionSearchParam.getStatus();
        // 用户ID
        Long certificationId = collectionSearchParam.getCertificationId();
        // 藏品ID
        Long productId = collectionSearchParam.getProductId();
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        // 查询同类别NFT
        startPage(collectionSearchParam);
        List<Product> sameCategoryList = productMapper.selectProductByIdAndCategoryIdAndCreateId(productId, product.getProductCategoryId(), certificationId, status);
        // 藏品ID列表
        Set<Long> productIdSet = sameCategoryList.stream().map(Product::getProductId).collect(Collectors.toSet());
        // 查询藏品操作列表
        List<ProductOpe> sameCategoryProductOpeList = productIdSet.size() == 0 ? Lists.newArrayList() : productOpeMapper.selectByProductIdSet(productIdSet);
        // 藏品操作映射关系
        Map<Long, List<ProductOpe>> productOpeListMap = sameCategoryProductOpeList.stream().collect(Collectors.groupingBy(ProductOpe::getProductId));
        List<ProductOpe> productOpeByUser = ObjectUtil.isNull(certificationId) ? Lists.newArrayList() : productOpeMapper.selectByCertificationIdSetAndTypeSet(Sets.newHashSet(certificationId), CollectOpeType.opeTypeSet());
        List<ProductBase> categoryBaseList = getProductBaseList(certificationId, sameCategoryList, productOpeListMap, productOpeByUser);
        productVO.setSameCategoryList(categoryBaseList);

        // 查询同作者NFT
        startPage(collectionSearchParam);
        List<Product> sameAuthorList = productMapper.selectProductByIdAndAuthor(productId, product.getCreateId(), status, 0L);
        productIdSet = sameAuthorList.stream().map(Product::getProductId).collect(Collectors.toSet());
        // 查询藏品操作列表
        List<ProductOpe> sameAuthorProductOpeList = productIdSet.size() == 0 ? Lists.newArrayList() : productOpeMapper.selectByProductIdSet(productIdSet);
        // 藏品操作映射关系
        productOpeListMap = sameAuthorProductOpeList.stream().collect(Collectors.groupingBy(ProductOpe::getProductId));
        List<ProductBase> authorBaseList = getProductBaseList(certificationId, sameAuthorList, productOpeListMap, productOpeByUser);
        productVO.setSameAuthorList(authorBaseList);
        return AjaxResult.success("查询成功", productVO);
    }

    /**
     * 藏品操作(点赞、收藏)
     *
     * @param collectionOpeParam 点赞信息
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult addProductOpe(CollectionOpeParam collectionOpeParam) {
        // 藏品ID
        Long productId = collectionOpeParam.getProductId();
        // 查询藏品是否存在
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        // 操作者ID
        Long certificationId = collectionOpeParam.getCertificationId();
        // 操作类型
        Integer opeType = collectionOpeParam.getOpeType();
        // 查询用户藏品操作信息
        ProductOpe productOpe = productOpeMapper.selectByProductIdAndCertificationIdAndType(productId, certificationId, opeType);
        int effectNum;
        Product updateProduct = new Product();
        updateProduct.setProductId(productId);
        updateProduct.setUpdateTime(new Date());
        if (ObjectUtil.isNull(productOpe)) {
            // 点赞、收藏操作
            ProductOpe ope = CopyUtils.copy(collectionOpeParam, ProductOpe.class);
            // 点赞
            if (opeType.equals(CollectOpeType.FAVORITE.getCode())) {
                updateProduct.setFavoriteCount(product.getFavoriteCount().intValue() + 1);
            }
            // 收藏
            if (opeType.equals(CollectOpeType.COLLECT.getCode())) {
                updateProduct.setCollectionCount(product.getCollectionCount().intValue() + 1);
            }
            productMapper.updateProduct(updateProduct);
            effectNum = productOpeMapper.insertProductOpe(ope);
            return effectNum == 1 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
        }
        // 取消点赞、收藏操作
        productOpeMapper.delByProductIdAndCertificationIdAndType(productId, certificationId, opeType);
        // 取消点赞
        if (opeType.equals(CollectOpeType.FAVORITE.getCode())) {
            updateProduct.setFavoriteCount(product.getFavoriteCount().intValue() - 1);
        }
        // 取消收藏
        if (opeType.equals(CollectOpeType.COLLECT.getCode())) {
            updateProduct.setCollectionCount(product.getCollectionCount().intValue() - 1);
        }
        effectNum = productMapper.updateProduct(updateProduct);
        return effectNum == 1 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }

    /**
     * 下单购买藏品
     *
     * @param collectionBuyParam 藏品信息
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult buyCollect(CollectionBuyParam collectionBuyParam) {
        // 用户ID
        Long certificationId = collectionBuyParam.getCertificationId();
        // 查询买家用户信息
        Certification buyer = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(buyer)) {
            return AjaxResult.error("用户不存在");
        }
        // 订单金额
        BigDecimal totalAmount = collectionBuyParam.getTotalAmount();
        // 藏品ID
        Long productId = collectionBuyParam.getProductId();
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        // 查询藏品状态
        if (!CollectStatus.validStatus(product.getStatus())) {
            CollectStatus statusEnum = CollectStatus.getByCode(product.getStatus());
            return AjaxResult.error(String.format("藏品%s,不能操作"), statusEnum.getMessage());
        }
        // 查询藏品拥有者信息
        Long ownerId = product.getSourceProductId();
        Certification owner = certificationMapper.selectCertificationById(ownerId);
        if (ObjectUtil.isNull(owner)) {
            return AjaxResult.error("藏品不存在");
        }
//        if (certificationId.equals(ownerId)) {
//            return AjaxResult.error("不能下单自己的藏品");
//        }
        // 查询藏品token信息
        List<ProductToken> tokenList = productTokenMapper.selectByProductId(productId);
        if (ObjectUtil.isNull(tokenList)) {
            return AjaxResult.error("藏品不存在");
        }

        // 构建藏品交易信息
        ProductTrade productTrade = buildModelService.buildProductTrade(product, collectionBuyParam, tokenList.get(tokenList.size() - 1).getTokenId(), owner, buyer, CollectTradeStatus.SUCCESS.getCode());
        productTradeMapper.insertProductTrade(productTrade);

        // 新增藏品交易记录(Buy)
        ProductOpe buyProductOpe = buildModelService.buildProductOpe(certificationId, productId, CollectOpeType.BUY.getCode());
        productOpeMapper.insertProductOpe(buyProductOpe);

        // 新增藏品交易记录(Sell)
        ProductOpe sellProductOpe = buildModelService.buildProductOpe(ownerId, productId, CollectOpeType.SELL.getCode());
        productOpeMapper.insertProductOpe(sellProductOpe);

        // 更新藏品状态
        Product updateProduct = new Product();
        updateProduct.setProductId(productId);
        updateProduct.setStatus(CollectStatus.TRADED.getCode());
        updateProduct.setUpdateTime(new Date());
        productMapper.updateProduct(updateProduct);

        // 新增买家出账记录(交易金额)
        CertificationWithdraw orderWithdraw = buildModelService.buildCertificationWithdraw(buyer, totalAmount, WithdrawType.BUY.getCode(), WithdrawStatus.YES.getCode());
        withdrawMapper.insertCertificationWithdraw(orderWithdraw);

        // 新增买家出账记录(交易手续费)
        CertificationWithdraw feeWithdraw = buildModelService.buildCertificationWithdraw(buyer, collectionBuyParam.getFee(), WithdrawType.FEE.getCode(), WithdrawStatus.YES.getCode());
        withdrawMapper.insertCertificationWithdraw(feeWithdraw);

        // 新增买家原石记录
//        Integer rewardFrom = RoughRewardFrom.TRADE.getCode();
//        String hash = Web3jUtils.mint(rewardFrom, totalAmount, buyer.getWalletAddress(), collectionBuyParam.getTokenAddress());
//        Integer rewardStatus = StringUtils.isBlank(hash) ? RoughStatus.FAIL.getCode() : RoughStatus.OK.getCode();
//        RoughRecord roughRecord = buildModelService.buildRoughRecord(buyer, totalAmount, hash, rewardFrom, rewardStatus);
//        roughRecordMapper.insertRoughRecord(roughRecord);

        // 单价
        BigDecimal price = product.getPrice();
        // 服务费率
        BigDecimal serviceRate = collectionBuyParam.getServiceRate();
        // 邀请人费率
        BigDecimal invitorRate = collectionBuyParam.getInvitorRate();
        // 版税
        BigDecimal copyrightRate = product.getCopyrightRate();

        // 查询版税获益者
//        Product sourceProduct = productMapper.selectProductById(product.getSourceProductId());
//        Certification sourceCreator = certificationMapper.selectCertificationById(ObjectUtil.isNull(sourceProduct) ? ownerId : sourceProduct.getCreateId());
//        BigDecimal copyrightAmount = price.multiply(copyrightRate).divide(new BigDecimal(100), 2);
//        Charge copyrightCharge = buildModelService.buildCharge(sourceCreator, copyrightAmount, ChargeType.COPYRIGHT.getCode(), ChargeStatus.SUCCESS.getCode());
//        chargeMapper.insertCharge(copyrightCharge);

        // 查询邀请人
//        Certification invitor = certificationMapper.selectByWalletAddress(collectionBuyParam.getInvitorAddress());
//        BigDecimal invitorAmount = price.multiply(invitorRate).divide(new BigDecimal(100), 2);
        // 邀请人入账记录
//        Charge invitorCharge = buildModelService.buildCharge(invitor, invitorAmount, ChargeType.INCOME.getCode(), ChargeStatus.SUCCESS.getCode());
//        chargeMapper.insertCharge(invitorCharge);

        // 卖家出账记录(交易手续费由卖家提供)
        BigDecimal serviceAmount = price.multiply(serviceRate).divide(new BigDecimal(100), 2);
        buildModelService.buildCertificationWithdraw(owner, serviceAmount, WithdrawType.FEE.getCode(), WithdrawStatus.YES.getCode());

        // 卖家入账记录
        BigDecimal income = price.subtract(price.multiply(serviceRate.add(invitorRate).add(copyrightRate)).divide(new BigDecimal(100), 2));
        Charge ownerCharge = buildModelService.buildCharge(owner, income, ChargeType.SALE.getCode(), ChargeStatus.SUCCESS.getCode());

        int effectNum = chargeMapper.insertCharge(ownerCharge);

        // 给买家发送邮件告知原文件下载地址
        thirdPartyService.sendHtmlMailForTradeBuyer(buyer.getMobile(), "https://artlinx.hk/ipfs/cat/" + product.getProductImage());
        return effectNum == 1 ? AjaxResult.success("Successful Transaction") : AjaxResult.error("交易失败");
    }

    /**
     * 更新藏品交易状态
     *
     * @param collectionTradeParam 交易信息
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult updateCollectTrade(CollectionTradeParam collectionTradeParam) {
        // 藏品交易ID
        Long productTradeId = collectionTradeParam.getProductTradeId();
        // 查询藏品交易信息
        ProductTrade productTrade = productTradeMapper.selectProductTradeById(productTradeId);
        if (ObjectUtil.isNull(productTrade) || !productTrade.getStatus().equals(CollectTradeStatus.TRADING.getCode())) {
            return AjaxResult.error("交易信息不存在");
        }
        // 藏品ID
        Long productId = productTrade.getProductId();
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        // 藏品交易状态
        Integer tradeStatus = collectionTradeParam.getStatus();
        // 更新藏品交易状态
        ProductTrade updateProductTrade = new ProductTrade();
        updateProductTrade.setProductTradeId(productTradeId);
        updateProductTrade.setStatus(tradeStatus);
        updateProductTrade.setUpdateTime(new Date());
        productTradeMapper.updateProductTrade(updateProductTrade);
        Product updateProduct = new Product();
        updateProduct.setProductId(productId);
        updateProduct.setUpdateTime(new Date());
        int productStatus = CollectStatus.OFFLINE.getCode();
        // 藏品交易成功
        if (tradeStatus.equals(CollectTradeStatus.SUCCESS.getCode())) {
            // 新增藏品操作记录
            ProductOpe productOpe = new ProductOpe();
            productOpe.setProductId(productId);
            productOpe.setCertificationId(productTrade.getCreateId());
            productOpe.setOpeType(CollectOpeType.BUY.getCode());
            productOpe.setCreateTime(new Date());
            productOpeMapper.insertProductOpe(productOpe);
        }
        // 藏品交易失败
        if (tradeStatus.equals(CollectTradeStatus.FAIL.getCode())) {
            // 修改藏品状态
            productStatus = CollectStatus.ONLINE.getCode();
        }
        updateProduct.setStatus(productStatus);
        int effectNum = productMapper.updateProduct(updateProduct);
        return effectNum == 1 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }

    /**
     * 查询藏品交易列表
     *
     * @param collectionTradeSearchParam 藏品
     * @return 结果
     */
    @Override
    public AjaxResult getCollectionTradeList(CollectionTradeSearchParam collectionTradeSearchParam) {
        // 用户ID
        Long certificationId = collectionTradeSearchParam.getCertificationId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("totalCount", 0);
        resultMap.put("list", Lists.newArrayList());
        CollectionSearchParam searchParam = CopyUtils.copy(collectionTradeSearchParam, CollectionSearchParam.class);
        startPage(searchParam);
        // 查询用户交易记录(Buy+Sell)
        List<ProductTrade> productTradeList = productTradeMapper.selectByAddress(certification.getWalletAddress());
        if (CollectionUtil.isEmpty(productTradeList)) {
            return AjaxResult.success("查询成功", resultMap);
        }
        PageInfo<ProductTrade> pageInfo = new PageInfo<>(productTradeList);
        // 查询藏品ID集合
        Set<Long> productIdSet = productTradeList.stream().map(ProductTrade::getProductId).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Long, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getProductId, Function.identity()));
        // 查询分类ID集合
        Set<Long> categoryIdSet = productTradeList.stream().map(ProductTrade::getCategoryId).collect(Collectors.toSet());
        List<ProductCategory> categoryList = productCategoryMapper.selectByCategoryIdSet(categoryIdSet);
        Map<Long, ProductCategory> categoryMap = categoryList.stream().collect(Collectors.toMap(ProductCategory::getProductCategoryId, Function.identity()));
        List<CollectionTradeInfo> tradeInfoList = CopyUtils.copyList(productTradeList, CollectionTradeInfo.class);
        tradeInfoList.stream().forEach(tradeInfo -> {
            CollectTradeStatus tradeStatus = CollectTradeStatus.getByCode(tradeInfo.getStatus());
            tradeInfo.setTradeStatusDesc(ObjectUtil.isNull(tradeStatus) ? "" : tradeStatus.getMessage());
            ProductCategory category = categoryMap.getOrDefault(tradeInfo.getCategoryId(), null);
            tradeInfo.setCategoryName(ObjectUtil.isNull(category) ? "" : category.getCategoryName());
            Product product = productMap.getOrDefault(tradeInfo.getProductId(), null);
            tradeInfo.setProductName(ObjectUtil.isNull(product) ? "" : product.getProductName());
            tradeInfo.setCoverImage(ObjectUtil.isNull(product) ? "" : product.getCoverImage());
            tradeInfo.setProductImage(ObjectUtil.isNull(product) ? "" : product.getProductImage());
            tradeInfo.setProductType(ObjectUtil.isNull(product) ? CollectType.NORMAL.getCode() : product.getProductType());
            boolean isFrom = tradeInfo.getFromId().equals(certificationId);
            tradeInfo.setTradeType(isFrom ? CollectTradeType.SELL.getCode() : CollectTradeType.BUY.getCode());
            tradeInfo.setTradeTypeDesc(isFrom ? CollectTradeType.SELL.getMessage() : CollectTradeType.BUY.getMessage());

            tradeInfo.setCoinType(ObjectUtil.isNull(product) ? "" : product.getCoinType());
            tradeInfo.setOrderLeft(ObjectUtil.isNull(product) ? "" : product.getOrderLeft());
            tradeInfo.setSignatureLeft(ObjectUtil.isNull(product) ? "" : product.getSignatureLeft());
            tradeInfo.setRemark(ObjectUtil.isNull(product) ? "" : product.getRemark());


        });
        resultMap.put("totalCount", pageInfo.getTotal());
        resultMap.put("list", tradeInfoList);
        return AjaxResult.success("查询成功", resultMap);
    }

    /**
     * 查询币种配置信息
     *
     * @param coinType 币种类型
     * @return 结果
     */
    @Override
    public AjaxResult getCoinConfig(Integer coinType) {
        if (ObjectUtil.isNull(coinType)) {
            return AjaxResult.error("参数错误");
        }
        ProductCoinConfig coinConfig = productCoinConfigMapper.selectByCoinType(coinType);
        return AjaxResult.success("查询成功", coinConfig);
    }

    /**
     * 查询藏品订单支付信息
     *
     * @param payCheckParam 查询参数
     * @return
     */
    @Override
    public AjaxResult checkProductPayInfo(PayCheckParam payCheckParam) {
        // 用户ID
        Long certificationId = payCheckParam.getCertificationId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("User does not exist.");
        }
        // 藏品ID
        Long productId = payCheckParam.getProductId();
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("Collection does not exist.");
        }
        // 查询藏品交易信息
        ProductTrade productTrade = productTradeMapper.selectByProductId(productId);
        if (ObjectUtil.isNull(productTrade)) {
            return AjaxResult.error("Information abnormal");
        }
        if (productTrade.getToId().equals(certificationId)) {
            if (productTrade.getStatus().equals(CollectTradeStatus.SUCCESS.getCode())) {
                return AjaxResult.success(1);
            }
            if (productTrade.getStatus().equals(CollectTradeStatus.WAIT_PAY.getCode()) || productTrade.getStatus().equals(CollectTradeStatus.TRADING.getCode())) {
                return AjaxResult.success(0);
            }
        }
        return AjaxResult.error();
    }

    /**
     * 更新藏品状态
     *
     * @param productId 藏品ID
     * @param status    状态
     * @return 结果
     */
    @Override
    public AjaxResult updateCollectionStatus(Long productId, Integer status) {
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        Product updateProduct = new Product();
        updateProduct.setProductId(productId);
        updateProduct.setStatus(status);
        updateProduct.setUpdateTime(new Date());
        int effectNum = productMapper.updateProduct(updateProduct);
        return effectNum == 1 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage(CollectionSearchParam collectionSearchParam) {
        Integer pageNum = collectionSearchParam.getPageNum();
        pageNum = (ObjectUtil.isNull(pageNum) || pageNum < 1) ? 1 : collectionSearchParam.getPageNum();
        Integer pageSize = collectionSearchParam.getPageSize();
        pageSize = (ObjectUtil.isNull(pageSize) || pageSize < 1) ? 10 : collectionSearchParam.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(collectionSearchParam.getOrderBy());
        orderBy = StringUtils.isEmpty(orderBy) ? "create_time desc" : orderBy;
        if (orderBy.startsWith("time")) {
            orderBy = orderBy.replace("time", "create_time");
        }
        if (orderBy.startsWith("favorite")) {
            orderBy = orderBy.replace("favorite", "favorite_count");
        }
        if (orderBy.startsWith("collection")) {
            orderBy = orderBy.replace("collection", "collection_count");
        }
        PageHelper.startPage(pageNum, pageSize, orderBy);
    }

    /**
     * 统计操作类型的数量
     */
    private long countProductOpe(List<ProductOpe> productOpeList, Integer opeType) {
        return productOpeList.stream().filter(productOpe -> productOpe.getOpeType().equals(opeType)).count();
    }

    private List<ProductBase> getProductBaseList(Long certificationId, List<Product> productList, Map<Long, List<ProductOpe>> productOpeListMap, List<ProductOpe> productOpeByUser) {
        Map<Long, List<ProductOpe>> productOpeUserMap = productOpeByUser.stream().collect(Collectors.groupingBy(ProductOpe::getProductId));
        List<ProductBase> productBaseList = CopyUtils.copyList(productList, ProductBase.class);
        // 藏品作者ID列表
        Set<Long> createIdSet = productList.stream().map(Product::getCreateId).collect(Collectors.toSet());
        // 藏品作者列表
        List<Certification> certificationList = createIdSet.size() == 0 ? Lists.newArrayList() : certificationMapper.selectByCertificateIdSet(createIdSet);
        Map<Long, Certification> certificationMap = CollUtil.isEmpty(certificationList) ? Maps.newHashMap() : certificationList.stream().collect(Collectors.toMap(Certification::getId, Function.identity()));
        // 藏品作者附属信息列表
        List<CertificationAttach> attachList = createIdSet.size() == 0 ? Lists.newArrayList() : attachMapper.selectByCertificationIdSet(createIdSet);
        Map<Long, CertificationAttach> attachMap = CollUtil.isEmpty(attachList) ? Maps.newHashMap() : attachList.stream().collect(Collectors.toMap(CertificationAttach::getCertificationId, Function.identity()));
        productBaseList.stream().forEach(productVO -> {
            List<ProductOpe> opeListMap = productOpeListMap.getOrDefault(productVO.getProductId(), null);
            if (CollectionUtil.isNotEmpty(opeListMap)) {
                // 点赞数量
                long favoriteCount = countProductOpe(opeListMap, CollectOpeType.FAVORITE.getCode());
                productVO.setFavoriteCount(favoriteCount);
                long collectCount = countProductOpe(opeListMap, CollectOpeType.COLLECT.getCode());
                // 收藏数量
                productVO.setCollectionCount(collectCount);
            }
            if (CollectionUtil.isNotEmpty(productOpeByUser)) {
                List<ProductOpe> productOpeUserMapList = productOpeUserMap.getOrDefault(productVO.getProductId(), Lists.newArrayList());
                List<ProductOpe> productOpeListByFavorite = productOpeUserMapList.stream()
                        .filter(productOpeUser -> ObjectUtil.isNotNull(productOpeUser) && productOpeUser.getOpeType().equals(CollectOpeType.FAVORITE.getCode()))
                        .collect(Collectors.toList());
                Set<Long> setByFavorite = productOpeListByFavorite.stream().map(ProductOpe::getCertificationId).collect(Collectors.toSet());
                productVO.setIsFavorite(setByFavorite.contains(certificationId) ? 1 : 0);
                List<ProductOpe> productOpeListByCollect = productOpeUserMapList.stream()
                        .filter(productOpeUser -> ObjectUtil.isNotNull(productOpeUser) && productOpeUser.getOpeType().equals(CollectOpeType.COLLECT.getCode()))
                        .collect(Collectors.toList());
                Set<Long> setByCollect = productOpeListByCollect.stream().map(ProductOpe::getCertificationId).collect(Collectors.toSet());
                productVO.setIsCollection(setByCollect.contains(certificationId) ? 1 : 0);
            }
            CertificationAttach attach = attachMap.getOrDefault(productVO.getCreateId(), null);
            String nickName = attach.getNickName();
            if (StringUtils.isBlank(nickName)) {
                Certification creator = certificationMap.get(productVO.getCreateId());
                nickName = creator.getMobile();
            }
            productVO.setProductCreatorNickName(nickName);
            productVO.setProductCreatorAvatar(ObjectUtil.isNull(attach) ? "" : StringUtils.trimToEmpty(attach.getAvatarUrl()));
        });
        return productBaseList;
    }

    /**
     * 新订单支付方式
     * 先待支付
     * 然后线上直接支付
     * 最后再收到支付成功后，就完成交易（别的方法）
     *
     * @param collectionBuyParam
     * @return
     */
    @Override
    @Transactional
    public AjaxResult buyForNewPayOrder(CollectionBuyParam collectionBuyParam) throws Exception {
        // 用户ID
        Long certificationId = collectionBuyParam.getCertificationId();
        // 查询买家用户信息
        Certification buyer = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(buyer)) {
            return AjaxResult.error("用户不存在");
        }

        // 藏品ID
        Long productId = collectionBuyParam.getProductId();
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
        // 查询藏品状态
        if (!CollectStatus.validStatus(product.getStatus())) {
            CollectStatus statusEnum = CollectStatus.getByCode(product.getStatus());
            return AjaxResult.error(String.format("藏品%s,不能操作"), statusEnum.getMessage());
        }
        // 查询藏品拥有者信息
        Long ownerId = product.getSourceProductId();
        Certification owner = certificationMapper.selectCertificationById(ownerId);
        if (ObjectUtil.isNull(owner)) {
            return AjaxResult.error("藏品不存在");
        }
//        if (certificationId.equals(ownerId)) {
//            return AjaxResult.error("不能下单自己的藏品");
//        }
        // 查询藏品token信息
        List<ProductToken> tokenList = productTokenMapper.selectByProductId(productId);
        if (ObjectUtil.isNull(tokenList)) {
            return AjaxResult.error("藏品不存在");
        }

        // 构建藏品交易信息
        ProductTrade productTrade = buildModelService.buildProductTrade(product, collectionBuyParam, tokenList.get(tokenList.size() - 1).getTokenId(), owner, buyer, CollectTradeStatus.WAIT_PAY.getCode());
        productTradeMapper.insertProductTrade(productTrade);

        log.info("待支付订单id：" + productTrade.getProductTradeId());
        // 更新藏品状态
        Product updateProduct = new Product();
        updateProduct.setProductId(productId);
        updateProduct.setSourceProductId(collectionBuyParam.getCertificationId());
        updateProduct.setStatus(CollectStatus.TRADING.getCode());
        updateProduct.setUpdateTime(new Date());
        productMapper.updateProduct(updateProduct);

        BigDecimal price = product.getPrice();
        //金钱出入记录，增加一条待支付
        MoneyHistory history = new MoneyHistory();
        history.setCertificationId(certificationId);
        history.setId(IdUtils.getTradeNo());
        history.setMoney(price);
        history.setType(MoneyHistoryType.BUY_PAY.getCode());
        history.setStatus(MoneyHistoryStatus.ING.getCode());
        history.setTradeId(productTrade.getProductTradeId());

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        history.setCreateTime(new Date());//创建时间

        Calendar newTime = Calendar.getInstance();
        newTime.setTime(now);
        // 日期加n分
        newTime.add(Calendar.MINUTE, Integer.parseInt(expire));

        Date newDate = newTime.getTime();
        // 支付到期时间
        history.setPayExpireTime(newDate);
        int effectEum = moneyHistoryMapper.insertCharge(history);

//        String result_str = HttpClient.doPostJson(signToParams(price.toPlainString(), productTrade.getProductTradeId().toString()), null, "https://api.hk.blueoceanpay.com/payment/pay");
//        log.info("支付结果：" + result_str);
//        JSONObject jsonObject = JSONObject.parseObject(result_str);
//        int code = jsonObject.getIntValue("code");
        if (effectEum > 0) {
//            JSONObject jsonObject_data = jsonObject.getJSONObject("data");
//            JSONObject jsonObject_data = new JSONObject(2);
//            String qrcode = jsonObject_data.getString("qrcode");
            AjaxResult ajaxResult = AjaxResult.success();
//            ajaxResult.put("qrcode", qrcode);
            ajaxResult.put("orderoId", productTrade.getProductTradeId());
            return ajaxResult;
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error("支付错误", 500);
        }
    }

    @Override
    @Transactional
    public AjaxResult cancelPay(Long tradeId) {

        //trade的订单状态修改
        ProductTrade productTrade = productTradeMapper.selectProductTradeById(tradeId);
        //待支付
        if (productTrade != null && productTrade.getStatus() == 0) {

            Product product = productMapper.selectProductById(productTrade.getProductId());
            MoneyHistory historySearch = new MoneyHistory();
            historySearch.setTradeId(productTrade.getProductTradeId());
            historySearch.setCertificationId(productTrade.getCreateId());
            historySearch.setType(MoneyHistoryType.BUY_PAY.getCode());
            historySearch.setStatus(MoneyHistoryStatus.ING.getCode());
            List<MoneyHistory> moneyHistories = moneyHistoryMapper.selectChargeList(historySearch);

            //更新订单状态
            ProductTrade productTradeUpdate = new ProductTrade();
            productTradeUpdate.setProductTradeId(productTrade.getProductTradeId());
            productTradeUpdate.setUpdateTime(new Date());
            productTradeUpdate.setStatus(CollectTradeStatus.FAIL.getCode());
            productTradeMapper.updateProductTrade(productTradeUpdate);

            // 更新藏品状态
            if (product != null && product.getStatus() == 1) {
                Product updateProduct = new Product();
                updateProduct.setProductId(productTrade.getProductId());
                updateProduct.setStatus(CollectStatus.ONLINE.getCode());
                updateProduct.setUpdateTime(new Date());
                productMapper.updateProduct(updateProduct);
            }

            //金钱出入记录
            if (moneyHistories != null && moneyHistories.size() != 0 && moneyHistories.get(0).getStatus() == 1) {

                MoneyHistory historyUpdate = new MoneyHistory();
                historyUpdate.setId(moneyHistories.get(0).getId());
                historyUpdate.setUpdateTime(new Date());
                historyUpdate.setStatus(MoneyHistoryStatus.CANCEL.getCode());
                moneyHistoryMapper.updateCharge(historyUpdate);
            }

        }

        return AjaxResult.success();
    }

    public String signToParams(String money, String orderId) {
        BigDecimal bigDecimal = new BigDecimal(money);
        BigDecimal total_for_pay = bigDecimal.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN);
        String h5BackUrl = h5_back_url + orderId;
        //除了key参数，其他的参数按首字母的ascii码顺序排列，最后再添加key的参数
        String params = "appid=1022401&h5_redirect_url=" + h5BackUrl + "&notify_url=" + notify_url + "&out_trade_no=" + orderId.trim() + "&payment=unionpay.link" + "&total_fee=" + total_for_pay.toString() + "&wallet=CN&key=7Fcmxbamf2Mgs5wkvPnWxiChDRgOqbTe";
        log.info("params：" + params);
        String sign = Md5Utils.hash(params).toUpperCase();

        ToPayPojo toPayPojo = new ToPayPojo(total_for_pay.toString(), notify_url, sign, orderId.trim(), h5BackUrl);

        Gson gson = new Gson();
        String toJson = gson.toJson(toPayPojo);
        log.info("toJson：" + toJson);
        return toJson;
    }

    @Override
    @Transactional
    public AjaxResult buyForPayedOrder(Long orderId, String artExchangeHash) {
        ProductTrade productTrade = productTradeMapper.selectProductTradeById(orderId);

        if (productTrade != null) {
            Long productId = productTrade.getProductId();
            Product product = productMapper.selectProductById(productId);
            if (product != null) {
                if (0 == productTrade.getStatus().intValue()) {
                    // 买家信息
                    Certification buyer = certificationMapper.selectCertificationById(productTrade.getCreateId());
                    //将藏品归属权转给买家
//                    String artExchangeHash = Web3jUtils.getArtExchangeHash(productTrade.getTokenId(), buyer.getWalletAddress());
                    if (StringUtils.isBlank(artExchangeHash)) {
                        return AjaxResult.error("将藏品归属权转给买家操作失败");
                    }
                    // 卖家信息
                    Certification seller = certificationMapper.selectCertificationById(productTrade.getFromId());
                    List<ProductToken> tokenList = productTokenMapper.selectByProductId(productId);
                    if (CollUtil.isEmpty(tokenList)) {
                        return AjaxResult.error("token信息异常, productId：" + productId);
                    }
                    CollectDir collectDir = collectDirMapper.selectCollectDirById(tokenList.get(tokenList.size() - 1).getCollectDirId());
                    // 单价
                    BigDecimal price = product.getPrice();
                    // 服务费率
                    BigDecimal serviceRate = product.getServiceRate();
                    // 服务费
                    BigDecimal serviceAmount = (price.multiply((serviceRate).divide(new BigDecimal(100)))).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //更新订单状态
                    ProductTrade productTrade_update = new ProductTrade();
                    productTrade_update.setProductTradeId(productTrade.getProductTradeId());
                    productTrade_update.setTradeFee(serviceAmount);
                    productTrade_update.setTradeHash(artExchangeHash);
                    productTrade_update.setContractAddress(collectDir.getContractAddress());
                    productTrade_update.setStatus(CollectStatus.TRADED.getCode());
                    productTradeMapper.updateProductTrade(productTrade_update);

                    // 新增藏品交易记录(Buy)
                    ProductOpe buyProductOpe = buildModelService.buildProductOpe(productTrade.getCreateId(), productId, CollectOpeType.BUY.getCode());
                    productOpeMapper.insertProductOpe(buyProductOpe);

                    // 新增藏品交易记录(Sell)
                    ProductOpe sellProductOpe = buildModelService.buildProductOpe(product.getCreateId(), productId, CollectOpeType.SELL.getCode());
                    productOpeMapper.insertProductOpe(sellProductOpe);

                    // 更新藏品状态
                    Product updateProduct = new Product();
                    updateProduct.setProductId(productId);
                    updateProduct.setStatus(CollectStatus.TRADED.getCode());
                    updateProduct.setUpdateTime(new Date());
                    productMapper.updateProduct(updateProduct);

                    /**
                     * 金钱出入记录
                     * 1.买家修改支出记录，状态为完成
                     * 2.卖家增加收入记录
                     * 3.卖家增加支付记录（手续费）
                     */
                    MoneyHistory historyParams = new MoneyHistory();
                    historyParams.setCertificationId(productTrade.getToId());
                    historyParams.setTradeId(productTrade.getProductTradeId());
                    historyParams.setStatus(MoneyHistoryStatus.ING.getCode());
                    historyParams.setType(MoneyHistoryType.BUY_PAY.getCode());
                    List<MoneyHistory> moneyHistories = moneyHistoryMapper.selectChargeList(historyParams);
                    if (moneyHistories != null && moneyHistories.size() != 0) {
                        //1.买家修改支出记录，状态为完成
                        MoneyHistory history = moneyHistories.get(0);
                        history.setStatus(MoneyHistoryStatus.COMPLETE.getCode());
                        history.setUpdateTime(new Date());
                        moneyHistoryMapper.updateCharge(history);

                        //2.卖家增加收入记录(订单金额-服务费用)
                        MoneyHistory historyIncome = new MoneyHistory();
                        historyIncome.setCertificationId(productTrade.getFromId());
                        historyIncome.setMoney(price.subtract(serviceAmount));
                        historyIncome.setId(IdUtils.getTradeNo());
                        historyIncome.setType(MoneyHistoryType.SOLD_INCOME.getCode());
                        historyIncome.setStatus(MoneyHistoryStatus.COMPLETE.getCode());
                        historyIncome.setTradeId(productTrade.getProductTradeId());
                        historyIncome.setCreateTime(new Date());

                        moneyHistoryMapper.insertCharge(historyIncome);

                        //3.卖家增加支付记录（手续费）
                        MoneyHistory historyFee = new MoneyHistory();
                        historyFee.setCertificationId(productTrade.getFromId());
                        historyFee.setMoney(serviceAmount);
                        historyFee.setId(IdUtils.getTradeNo());
                        historyFee.setType(MoneyHistoryType.SOLD_FEE.getCode());
                        historyFee.setStatus(MoneyHistoryStatus.COMPLETE.getCode());
                        historyFee.setTradeId(productTrade.getProductTradeId());
                        historyFee.setCreateTime(new Date());
                        moneyHistoryMapper.insertCharge(historyFee);

                        //余额修改：给卖家的余额上增加（价格-手续费）
                        BigDecimal finallyBalance = price.subtract(serviceAmount);
                        balanceService.updateBalance(productTrade.getFromId(), finallyBalance, 1);
                    }
                    // 给买家发送邮件告知原文件下载地址, 通知卖家艺术品已售出
                    if (buyer != null && (!"".equals(seller.getMobile()))) {
                        try {
//                            thirdPartyService.sendHtmlMailForTradeSeller(seller.getMobile(), product.getProductName());
                            thirdPartyService.sendHtmlMailForTradeBuyer(buyer.getMobile(), "https://artlinx.uk/ipfs/cat/" + product.getProductImage());
                        } catch (Exception e) {
                            log.error("发送邮件失败，用户id:" + buyer.getId() + "，订单id:" + orderId);
                        }

                    }
                    return AjaxResult.success("Successful Transaction");
                } else {
                    return AjaxResult.error("订单状态异常", productTrade.getStatus().intValue());
                }
            }
        }
        return AjaxResult.error("订单异常");
    }


    /**
     * 退款(实际在支付渠道那边是撤销订单，而且是8个小时内的订单才能有效)
     *
     * @param out_trade_no
     * @return
     */
    @Override
    public AjaxResult refund(String out_trade_no) {
        String result_str = HttpClient.doPostJson(signToRefund(out_trade_no), null, "https://api.hk.blueoceanpay.com/order/reverse");
        log.info("退款结果：" + result_str);
        JSONObject jsonObject = JSONObject.parseObject(result_str);
        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            AjaxResult ajaxResult = AjaxResult.success();
            ajaxResult.put("orderoId", out_trade_no);
            return ajaxResult;
        } else {
            return AjaxResult.error("退款错误", result_str);
        }
    }

    /**
     * 撤回藏品信息
     *
     * @param revokeParam 撤回参数
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult revokeMallCollection(RevokeParam revokeParam) {
        // 用户ID
        Long certificationId = revokeParam.getCertificationId();
        // 藏品ID
        Long productId = revokeParam.getProductId();

        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("User does not exist.");
        }
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("Collection does not exist.");
        }
        // 校验藏品状态
        if (!product.getStatus().equals(CollectStatus.ONLINE.getCode())) {
            return AjaxResult.error("Collection is not on sale.");
        }
        // 查询藏品token信息
        List<ProductToken> oldTokenList = productTokenMapper.selectByProductId(product.getProductId());
        if (CollUtil.isEmpty(oldTokenList)) {
            return AjaxResult.error("Collection does not exist.");
        }
        // 构建Token 信息
        revokeParam.setProductId(product.getProductId());
//        revokeParam.setCollectDirId(1L);
//        revokeParam.setRemark(product.getRemark());
        revokeParam.setTokenId(String.valueOf(oldTokenList.get(oldTokenList.size() - 1).getTokenId()));
        ProductToken productToken = buildModelService.buildProductToken(revokeParam);
        productToken.setOpeType(TokenOpeType.REVOKE.getCode());
        productToken.setCoinType(oldTokenList.get(oldTokenList.size() - 1).getCoinType());
//        productToken.setProductTokenId(oldTokenList.get(oldTokenList.size() - 1).getProductTokenId());
        productToken.setRemark(product.getRemark());
        productToken.setPrice(BigDecimal.ZERO);
        productTokenMapper.insertProductToken(productToken);
//        productTokenMapper.updateProductToken(productToken);

        int effectNum = productMapper.revokeProduct(certificationId, productId, CollectStatus.ONLINE.getCode(), CollectStatus.OFFLINE.getCode());
        return effectNum == 1 ? AjaxResult.success("Revoke success.") : AjaxResult.error("Revoke failed.");
    }

    /**
     * 导入藏品
     *
     * @param importParam 藏品
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importMallCollection(CollectionImportParam importParam) {
        // 用户ID
        Long createId = importParam.getCreateId();
        // 查询用户信息
        Certification certification = certificationMapper.selectCertificationById(createId);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("用户不存在");
        }
        if (!ReleaseStatus.checkRelease(certification.getReleaseStatus())) {
            return AjaxResult.error("没有发布权限");
        }

        // 查询收藏夹信息
        CollectDir collectDir = collectDirMapper.selectByContractAddress(importParam.getContractAddress());
        if (ObjectUtil.isNull(collectDir)) {
            // 收藏夹不存在, 新增收藏夹
            collectDir = buildModelService.buildCollectDir(certification, importParam);
            collectDirMapper.insertCollectDir(collectDir);
        }
        Long collectDirId = collectDir.getCollectDirId();
        importParam.setCollectDirId(collectDirId);

        // 新增藏品
        Product product = CopyUtils.copy(importParam, Product.class);
        product.setParentProductId(0L);
        product.setSourceProductId(createId);
        product.setProductFrom(CollectFrom.IMPORT.getCode());
        product.setCreateTime(new Date());
        productMapper.insertProduct(product);

        // 新增ProductToken信息
        ProductToken productToken = buildModelService.buildProductToken(product.getProductId(), importParam);
        productToken.setCoinType(importParam.getCoinType());
        productToken.setSignatureLeft(importParam.getSignatureLeft());
        productToken.setOrderLeft(importParam.getOrderLeft());
        productToken.setRemark(importParam.getRemark());
        productToken.setOpeType(TokenOpeType.IMPORT.getCode());

        productTokenMapper.insertProductToken(productToken);

        // 新增藏品操作信息
        ProductOpe productOpe = buildModelService.buildProductOpe(createId, product.getProductId(), CollectOpeType.IMPORT.getCode());
        productOpeMapper.insertProductOpe(productOpe);

        // 新增出账记录
        CertificationWithdraw withdraw = buildModelService.buildCertificationWithdraw(certification, importParam.getFee(), WithdrawType.IMPORT.getCode(), WithdrawStatus.YES.getCode());
        int effectNum = withdrawMapper.insertCertificationWithdraw(withdraw);
        // 发送邮件
//        thirdPartyService.sendHtmlMailForMoldUp(certification.getMobile());
//        mailTask.doSendHtmlMailForMoldUp(certification.getMobile());
        return effectNum == 1 ? AjaxResult.success("导入藏品成功") : AjaxResult.error("导入藏品失败");
    }

    /**
     * 校验藏品ID
     *
     * @param tokenId 藏品ID
     * @return 结果
     */
    @Override
    public AjaxResult checkTokenId(String tokenId) {
        // 查询藏品TOKEN信息
        List<ProductToken> tokenList = productTokenMapper.selectByTokenId(tokenId);
        return CollUtil.isEmpty(tokenList) ? AjaxResult.success("查询成功", 0) : AjaxResult.success("查询成功", 1);
    }

    public String signToRefund(String out_trade_no) {

        //除了key参数，其他的参数按首字母的ascii码顺序排列，最后再添加key的参数
        String params = "appid=1022401&out_trade_no=" + out_trade_no + "&key=7Fcmxbamf2Mgs5wkvPnWxiChDRgOqbTe";
        log.info("退款--params：" + params);
        String sign = Md5Utils.hash(params).toUpperCase();

        RefundVO refundVO = new RefundVO(sign, out_trade_no);
        Gson gson = new Gson();
        String toJson = gson.toJson(refundVO);
        log.info("退款--toJson：" + toJson);
        return toJson;
    }
}
