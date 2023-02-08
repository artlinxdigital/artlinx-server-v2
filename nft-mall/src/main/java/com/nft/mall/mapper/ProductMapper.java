package com.nft.mall.mapper;

import com.nft.common.core.domain.pojo.collect.CollectionSearchParam;
import com.nft.mall.domain.Product;
import com.nft.mall.domain.vo.ProductAuthorVO;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 藏品Mapper接口
 *
 * @author nft
 * @date 2021-05-17
 */
public interface ProductMapper {
    /**
     * 查询藏品
     *
     * @param productId 藏品ID
     * @return 藏品
     */
    public Product selectProductById(Long productId);

    /**
     * 查询藏品列表
     *
     * @param product 藏品
     * @return 藏品集合
     */
    public List<Product> selectProductList(Product product);

    /**
     * 新增藏品
     *
     * @param product 藏品
     * @return 结果
     */
    public int insertProduct(Product product);

    /**
     * 修改藏品
     *
     * @param product 藏品
     * @return 结果
     */
    public int updateProduct(Product product);

    /**
     * 删除藏品
     *
     * @param productId 藏品ID
     * @return 结果
     */
    public int deleteProductById(Long productId);

    /**
     * 批量删除藏品
     *
     * @param productIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductByIds(Long[] productIds);

    /**
     * 查询藏品列表
     *
     * @param collectionSearchParam 藏品
     * @return 藏品集合
     */
    public List<Product> selectCollectionList(CollectionSearchParam collectionSearchParam);

    public ArrayList<Product> selectCollectionListCON(CollectionSearchParam collectionSearchParam);

    /**
     * 查询藏品列表
     *
     * @param productCategoryId 藏品分类
     * @return 藏品集合
     */
    public List<Product> selectProductByIdAndCategoryIdAndCreateId(@Param("productId") Long productId, @Param("productCategoryId") Long productCategoryId, @Param("createId") Long createId, @Param("status") Integer status);

    /**
     * 查询藏品列表
     *
     * @param createId 藏品作者
     * @return 藏品集合
     */
    public List<Product> selectProductByIdAndAuthor(@Param("productId") Long productId, @Param("createId") Long createId, @Param("status") Integer status, @Param("sourceProductId") Long sourceProductId);

    /**
     * 批量查询藏品操作
     *
     * @param productIdSet 需要查询的数据ID集合
     * @return 结果
     */
    List<Product> selectByProductIdSet(@Param("productIdSet") Set<Long> productIdSet);

    /**
     * 查询藏品操作
     *
     * @param parentProductId 需要查询的数据ID
     * @return 结果
     */
    Product selectByParentProductId(Long parentProductId);

    /**
     * 批量查询藏品操作
     *
     * @param parentProductIdSet 需要查询的数据ID集合
     * @param statusSet          状态集合
     * @return 结果
     */
    List<Product> selectByParentProductIdSetAndStatusSet(@Param("parentProductIdSet") Set<Long> parentProductIdSet, @Param("statusSet") Set<Integer> statusSet);

    /**
     * 批量查询藏品操作
     *
     * @param createIdSet 需要查询的数据ID集合
     * @param statusSet   状态集合
     * @return 结果
     */
    List<Product> selectByCreateIdSetAndStatusSet(@Param("createIdSet") Set<Long> createIdSet, @Param("statusSet") Set<Integer> statusSet);

    List<Product> selectByCreateIdSetAndsourceProductIdSet(@Param("createIdSet") Set<Long> createIdSet, @Param("sourceProductId") Integer sourceProductId);
    List<Product> selectByCreateIdSetAndsourceProductIdSet1(@Param("createIdSet") Set<Long> createIdSet, @Param("sourceProductId") Integer sourceProductId);

    List<Product> selectByCreateIdSetAndStatusSet1(@Param("createIdSet") Set<Long> createIdSet, @Param("statusSet") Set<Integer> statusSet, @Param("idSet") Collection<Long> idSet);

    /**
     * 查询藏品操作
     *
     * @param productAuthor 需要查询的藏品作者
     * @return 结果
     */
    Product selectProductByAuthor(String productAuthor);

    /**
     * 查询藏品艺术家
     *
     * @param artName 需要查询的藏品艺术家
     * @return 结果
     */
    List<ProductAuthorVO> selectProductListByArt(String artName);

    /**
     * 撤回/上架藏品
     *
     * @param certificationId 用户ID
     * @param productId       藏品ID
     * @param fromStatus      修改前的状态
     * @param toStatus        修改后的状态
     * @return 结果
     */
    int revokeProduct(@Param("certificationId") Long certificationId, @Param("productId") Long productId, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus);

    /**
     * 藏品总数量
     *
     * @return 结果
     */
    String countAllProduct();

    /**
     * 当天藏品数量
     *
     * @return 结果
     */
    String countToadyProduct();

    /**
     * 藏品标价总和
     *
     * @return 结果
     */
    String sumAllProduct();

}
