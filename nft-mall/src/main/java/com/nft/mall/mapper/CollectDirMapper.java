package com.nft.mall.mapper;

import java.util.List;

import com.nft.mall.domain.CollectDir;
import org.apache.ibatis.annotations.Param;

/**
 * 用户收藏夹Mapper接口
 *
 * @author nft
 * @date 2021-06-06
 */
public interface CollectDirMapper
{
    /**
     * 查询用户收藏夹
     *
     * @param collectDirId 用户收藏夹ID
     * @return 用户收藏夹
     */
    public CollectDir selectCollectDirById(Long collectDirId);

    /**
     * 查询用户收藏夹列表
     *
     * @param collectDir 用户收藏夹
     * @return 用户收藏夹集合
     */
    public List<CollectDir> selectCollectDirList(CollectDir collectDir);

    /**
     * 新增用户收藏夹
     *
     * @param collectDir 用户收藏夹
     * @return 结果
     */
    public int insertCollectDir(CollectDir collectDir);

    /**
     * 修改用户收藏夹
     *
     * @param collectDir 用户收藏夹
     * @return 结果
     */
    public int updateCollectDir(CollectDir collectDir);

    /**
     * 删除用户收藏夹
     *
     * @param collectDirId 用户收藏夹ID
     * @return 结果
     */
    public int deleteCollectDirById(Long collectDirId);

    /**
     * 批量删除用户收藏夹
     *
     * @param collectDirIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCollectDirByIds(Long[] collectDirIds);

    /**
     * 查询用户收藏夹
     *
     * @param certificationId 用户ID
     * @param tokenName 文件夹名称
     * @param tokenType 文件夹类型
     * @return 用户收藏夹
     */
    CollectDir selectByCertificationIdAndNameAndType(@Param("certificationId") Long certificationId, @Param("tokenName") String tokenName, @Param("tokenType") Integer tokenType);

    /**
     * 查询用户收藏夹
     *
     * @param contractAddress 收藏夹地址
     * @return 用户收藏夹
     */
    CollectDir selectByContractAddress(String contractAddress);
}