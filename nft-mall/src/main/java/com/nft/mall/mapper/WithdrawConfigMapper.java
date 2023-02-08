package com.nft.mall.mapper;

import java.util.List;
import java.util.Set;

import com.nft.mall.domain.WithdrawConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 用户提现配置信息Mapper接口
 *
 * @author nft
 * @date 2021-06-09
 */
public interface WithdrawConfigMapper
{
    /**
     * 查询用户提现配置信息
     *
     * @param withdrawConfigId 用户提现配置信息ID
     * @return 用户提现配置信息
     */
    public WithdrawConfig selectWithdrawConfigById(Long withdrawConfigId);

    /**
     * 查询用户提现配置信息列表
     *
     * @param withdrawConfig 用户提现配置信息
     * @return 用户提现配置信息集合
     */
    public List<WithdrawConfig> selectWithdrawConfigList(WithdrawConfig withdrawConfig);

    /**
     * 新增用户提现配置信息
     *
     * @param withdrawConfig 用户提现配置信息
     * @return 结果
     */
    public int insertWithdrawConfig(WithdrawConfig withdrawConfig);

    /**
     * 修改用户提现配置信息
     *
     * @param withdrawConfig 用户提现配置信息
     * @return 结果
     */
    public int updateWithdrawConfig(WithdrawConfig withdrawConfig);

    /**
     * 删除用户提现配置信息
     *
     * @param withdrawConfigId 用户提现配置信息ID
     * @return 结果
     */
    public int deleteWithdrawConfigById(Long withdrawConfigId);

    /**
     * 批量删除用户提现配置信息
     *
     * @param withdrawConfigIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteWithdrawConfigByIds(Long[] withdrawConfigIds);

    /**
     * 查询用户提现配置信息
     *
     * @param certificationId 用户ID
     * @param configType 提现类型
     * @return 结果
     */
    List<WithdrawConfig> selectByCertificationIdAndType(@Param("certificationId") Long certificationId, @Param("configType") Integer configType);

    /**
     * 查询用户提现配置信息
     *
     * @param bankCardSet 银行卡号
     * @return 结果
     */
    List<WithdrawConfig> selectByBankCardSet(@Param("bankCardSet") Set<String> bankCardSet);
}