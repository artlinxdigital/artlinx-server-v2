package com.nft.mall.mapper;

import java.util.List;
import com.nft.mall.domain.BankConfig;

/**
 * 银行配置信息Mapper接口
 *
 * @author nft
 * @date 2021-06-12
 */
public interface BankConfigMapper
{
    /**
     * 查询银行配置信息
     *
     * @param bankConfigId 银行配置信息ID
     * @return 银行配置信息
     */
    public BankConfig selectBankConfigById(Long bankConfigId);

    /**
     * 查询银行配置信息列表
     *
     * @param bankConfig 银行配置信息
     * @return 银行配置信息集合
     */
    public List<BankConfig> selectBankConfigList(BankConfig bankConfig);

    /**
     * 新增银行配置信息
     *
     * @param bankConfig 银行配置信息
     * @return 结果
     */
    public int insertBankConfig(BankConfig bankConfig);

    /**
     * 修改银行配置信息
     *
     * @param bankConfig 银行配置信息
     * @return 结果
     */
    public int updateBankConfig(BankConfig bankConfig);

    /**
     * 删除银行配置信息
     *
     * @param bankConfigId 银行配置信息ID
     * @return 结果
     */
    public int deleteBankConfigById(Long bankConfigId);

    /**
     * 批量删除银行配置信息
     *
     * @param bankConfigIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBankConfigByIds(Long[] bankConfigIds);
}