package com.nft.mall.service;

import java.util.List;
import com.nft.mall.domain.NftTokenId;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author nft
 * @date 2021-06-10
 */
public interface INftTokenIdService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public NftTokenId selectNftTokenIdById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param nftTokenId 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<NftTokenId> selectNftTokenIdList(NftTokenId nftTokenId);

    /**
     * 新增【请填写功能名称】
     * 
     * @param nftTokenId 【请填写功能名称】
     * @return 结果
     */
    public int insertNftTokenId(NftTokenId nftTokenId);

    /**
     * 修改【请填写功能名称】
     * 
     * @param nftTokenId 【请填写功能名称】
     * @return 结果
     */
    public int updateNftTokenId(NftTokenId nftTokenId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteNftTokenIdByIds(String[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteNftTokenIdById(String id);
}
