package com.nft.mall.service.impl;

import java.util.List;
import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.NftTokenIdMapper;
import com.nft.mall.domain.NftTokenId;
import com.nft.mall.service.INftTokenIdService;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author nft
 * @date 2021-06-10
 */
@Service
public class NftTokenIdServiceImpl implements INftTokenIdService 
{
    @Autowired
    private NftTokenIdMapper nftTokenIdMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public NftTokenId selectNftTokenIdById(String id)
    {
        return nftTokenIdMapper.selectNftTokenIdById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param nftTokenId 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<NftTokenId> selectNftTokenIdList(NftTokenId nftTokenId)
    {
        return nftTokenIdMapper.selectNftTokenIdList(nftTokenId);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param nftTokenId 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertNftTokenId(NftTokenId nftTokenId)
    {
        nftTokenId.setCreateTime(DateUtils.getNowDate());
        return nftTokenIdMapper.insertNftTokenId(nftTokenId);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param nftTokenId 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateNftTokenId(NftTokenId nftTokenId)
    {
        nftTokenId.setUpdateTime(DateUtils.getNowDate());
        return nftTokenIdMapper.updateNftTokenId(nftTokenId);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteNftTokenIdByIds(String[] ids)
    {
        return nftTokenIdMapper.deleteNftTokenIdByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteNftTokenIdById(String id)
    {
        return nftTokenIdMapper.deleteNftTokenIdById(id);
    }
}
