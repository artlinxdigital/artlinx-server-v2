package com.nft.mall.mapper;

import java.util.List;
import com.nft.mall.domain.NftContractConfig;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author nft
 * @date 2021-06-10
 */
public interface NftContractConfigMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public NftContractConfig selectNftContractConfigById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param nftContractConfig 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<NftContractConfig> selectNftContractConfigList(NftContractConfig nftContractConfig);

    /**
     * 新增【请填写功能名称】
     * 
     * @param nftContractConfig 【请填写功能名称】
     * @return 结果
     */
    public int insertNftContractConfig(NftContractConfig nftContractConfig);

    /**
     * 修改【请填写功能名称】
     * 
     * @param nftContractConfig 【请填写功能名称】
     * @return 结果
     */
    public int updateNftContractConfig(NftContractConfig nftContractConfig);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteNftContractConfigById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNftContractConfigByIds(Long[] ids);
}
