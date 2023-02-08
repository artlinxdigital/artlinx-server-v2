package com.nft.mall.service;

import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.collect.*;
import com.nft.mall.domain.ProductCategory;

/**
 * 商城藏品Service接口
 *
 * @author nft
 * @date 2021-05-17
 */
public interface ICollectionService {

    /**
     * 查询藏品分类列表
     *
     * @param productCategory 藏品分类
     * @return 结果
     */
    AjaxResult getProductCategoryList(ProductCategory productCategory);

    /**
     * 查询藏品列表
     *
     * @param collectionSearchParam 藏品
     * @return 结果
     */
    AjaxResult getMallCollectionList(CollectionSearchParam collectionSearchParam);

    /**
     * 查询藏品交易记录列表
     *
     * @param collectDealRecordParam 藏品
     * @return 结果
     */
    AjaxResult getCollectDealRecordList(CollectDealRecordParam collectDealRecordParam);

    /**
     * 新增收藏夹
     *
     * @param collectionDirParam 收藏夹
     * @return 结果
     */
    AjaxResult addCollectionDir(CollectionDirParam collectionDirParam);

    /**
     * 查询收藏夹列表
     *
     * @param collectionDirParam 收藏夹
     * @return 结果
     */
    AjaxResult getCollectionDirList(CollectionDirParam collectionDirParam);

    /**
     * 我的藏品操作列表
     *
     * @param collectionSearchParam 藏品信息
     * @return 结果
     */
    AjaxResult myCollectionOpeList(CollectionSearchParam collectionSearchParam);

    /**
     * 我的NFT列表
     *
     * @param collectionSearchParam 藏品信息
     * @return 结果
     */
    AjaxResult myNftList(CollectionSearchParam collectionSearchParam);

    /**
     * 新增藏品
     *
     * @param collectionParam 藏品
     * @return 结果
     */
    AjaxResult addMallCollection(CollectionParam collectionParam);

    /**
     * 再次上架藏品
     *
     * @param collectionAgainParam 藏品
     * @return 结果
     */
    AjaxResult addMallCollectionAgain(CollectionAgainParam collectionAgainParam);

    /**
     * 获取藏品详情
     *
     * @param certificationId 用户id
     * @param productId       藏品id
     * @return 结果
     */
    AjaxResult getMallCollectionById(Long certificationId, Long productId);

    /**
     * 获取藏品作者信息
     *
     * @param authorName 作者名称
     * @return 结果
     */
    AjaxResult getCollectAuthorByName(String authorName);

    /**
     * 获取藏品艺术家信息
     *
     * @param artName 艺术家名称
     * @return 结果
     */
    AjaxResult getCollectArtistByName(String artName);

    /**
     * 查询藏品是否点赞/收藏
     *
     * @param certificationId 用户id
     * @param productId       藏品id
     * @return 结果
     */
    AjaxResult checkIsFavoriteAndCollect(Long certificationId, Long productId);

    /**
     * 查询同类型或者同作者藏品列表
     *
     * @param collectionSearchParam 藏品
     * @return 结果
     */
    AjaxResult getSameAuthorOrCategoryCollectionList(CollectionSearchParam collectionSearchParam);

    /**
     * 藏品操作(点赞、收藏)
     *
     * @param collectionOpeParam 点赞信息
     * @return 结果
     */
    AjaxResult addProductOpe(CollectionOpeParam collectionOpeParam);

    /**
     * 下单购买藏品
     *
     * @param collectionBuyParam 藏品信息
     * @return 结果
     */
    AjaxResult buyCollect(CollectionBuyParam collectionBuyParam);

    /**
     * 更新藏品交易状态
     *
     * @param collectionTradeParam 交易信息
     * @return 结果
     */
    AjaxResult updateCollectTrade(CollectionTradeParam collectionTradeParam);

    /**
     * 查询藏品交易列表
     *
     * @param collectionTradeSearchParam 藏品
     * @return 结果
     */
    AjaxResult getCollectionTradeList(CollectionTradeSearchParam collectionTradeSearchParam);

    /**
     * 查询币种配置信息
     *
     * @param coinType 币种类型
     * @return 结果
     */
    AjaxResult getCoinConfig(Integer coinType);

    /**
     * 查询藏品订单支付信息
     *
     * @param payCheckParam 查询参数
     * @return
     */
    AjaxResult checkProductPayInfo(PayCheckParam payCheckParam);

    /**
     * 更新藏品状态
     *
     * @param productId 藏品ID
     * @param status    状态
     * @return 结果
     */
    AjaxResult updateCollectionStatus(Long productId, Integer status);

    /**
     * 在线支付的生成待支付订单
     *
     * @param collectionBuyParam
     * @return
     */
    AjaxResult buyForNewPayOrder(CollectionBuyParam collectionBuyParam) throws Exception;

    /**
     * 收到钱，订单修改
     *
     * @param orderId
     * @return
     */
    AjaxResult buyForPayedOrder(Long orderId, String trade_hash);

    /**
     * 取消订单
     *
     * @param tradeId
     * @return
     */
    AjaxResult cancelPay(Long tradeId);

    /**
     * 支付渠道-退款
     *
     * @param out_trade_no
     * @return
     */
    AjaxResult refund(String out_trade_no);

    /**
     * 撤回藏品
     *
     * @param revokeParam 撤回参数
     * @return 结果
     */
    AjaxResult revokeMallCollection(RevokeParam revokeParam);

    /**
     * 导入藏品
     *
     * @param collectionImportParam 藏品
     * @return 结果
     */
    AjaxResult importMallCollection(CollectionImportParam collectionImportParam);

    /**
     * 校验藏品ID
     *
     * @param tokenId 藏品ID
     * @return 结果
     */
    AjaxResult checkTokenId(String tokenId);

}
