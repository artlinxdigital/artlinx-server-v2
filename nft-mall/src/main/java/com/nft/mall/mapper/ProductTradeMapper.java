package com.nft.mall.mapper;

import com.nft.mall.domain.ProductTrade;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 藏品交易Mapper接口
 *
 * @author nft
 * @date 2021-05-18
 */
public interface ProductTradeMapper {
    /**
     * 查询藏品交易
     *
     * @param productTradeId 藏品交易ID
     * @return 藏品交易
     */
    public ProductTrade selectProductTradeById(Long productTradeId);

    /**
     * 查询藏品交易列表
     *
     * @param productTrade 藏品交易
     * @return 藏品交易集合
     */
    public List<ProductTrade> selectProductTradeList(ProductTrade productTrade);

    /**
     * 新增藏品交易
     *
     * @param productTrade 藏品交易
     * @return 结果
     */
    public int insertProductTrade(ProductTrade productTrade);

    /**
     * 修改藏品交易
     *
     * @param productTrade 藏品交易
     * @return 结果
     */
    public int updateProductTrade(ProductTrade productTrade);

    /**
     * 删除藏品交易
     *
     * @param productTradeId 藏品交易ID
     * @return 结果
     */
    public int deleteProductTradeById(Long productTradeId);

    /**
     * 批量删除藏品交易
     *
     * @param productTradeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductTradeByIds(Long[] productTradeIds);

    /**
     * 批量查询藏品交易
     *
     * @param certificationIdSet 需要查询的数据ID
     * @return 结果
     */
    List<ProductTrade> selectByCertificationIdSet(@Param("certificationIdSet") Set<Long> certificationIdSet);

    /**
     * 批量查询藏品交易(根据状态查询)
     *
     * @param certificationIdSet 需要查询的数据ID
     * @param statusSet          状态集合
     * @return 结果
     */
    List<ProductTrade> selectByCertificationIdSetAndStatus(@Param("certificationIdSet") Set<Long> certificationIdSet, @Param("statusSet") Set<Integer> statusSet);

    /**
     * 查询藏品交易记录列表
     *
     * @param contractAddress 合约地址
     * @param tokenId         Token ID
     * @return 藏品交易集合
     */
    public List<ProductTrade> selectByContractAddressAndTokenId(@Param("contractAddress") String contractAddress, @Param("tokenId") String tokenId);
    public List<ProductTrade> selectByContractAddressAndTokenIdCON(@Param("contractAddress") String contractAddress, @Param("tokenId") String tokenId);

    /**
     * 批量查询藏品交易
     *
     * @param address 需要查询的地址
     * @return 结果
     */
    List<ProductTrade> selectByAddress(String address);

    /**
     * 查询藏品交易信息
     *
     * @param productId 藏品主键ID
     * @return 结果
     */
    ProductTrade selectByProductId(Long productId);

}
