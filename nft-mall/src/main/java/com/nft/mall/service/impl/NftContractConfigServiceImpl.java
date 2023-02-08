package com.nft.mall.service.impl;

import java.util.List;

import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.NftContractConfigMapper;
import com.nft.mall.domain.NftContractConfig;
import com.nft.mall.service.INftContractConfigService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author nft
 * @date 2021-06-10
 */
@Service
public class NftContractConfigServiceImpl implements INftContractConfigService {
    @Autowired
    private NftContractConfigMapper nftContractConfigMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public NftContractConfig selectNftContractConfigById(Long id) {
        return nftContractConfigMapper.selectNftContractConfigById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param nftContractConfig 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<NftContractConfig> selectNftContractConfigList(NftContractConfig nftContractConfig) {
        return nftContractConfigMapper.selectNftContractConfigList(nftContractConfig);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param nftContractConfig 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertNftContractConfig(NftContractConfig nftContractConfig) {
        nftContractConfig.setCreateTime(DateUtils.getNowDate());
        return nftContractConfigMapper.insertNftContractConfig(nftContractConfig);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param nftContractConfig 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateNftContractConfig(NftContractConfig nftContractConfig) {
        nftContractConfig.setUpdateTime(DateUtils.getNowDate());
        return nftContractConfigMapper.updateNftContractConfig(nftContractConfig);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteNftContractConfigByIds(Long[] ids) {
        return nftContractConfigMapper.deleteNftContractConfigByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteNftContractConfigById(Long id) {
        return nftContractConfigMapper.deleteNftContractConfigById(id);
    }
}
