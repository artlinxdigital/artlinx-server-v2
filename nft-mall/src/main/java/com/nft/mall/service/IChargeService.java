package com.nft.mall.service;

import java.util.List;
import com.nft.mall.domain.Charge;

/**
 * 充值Service接口
 *
 * @author nft
 * @date 2021-05-12
 */
public interface IChargeService
{
    /**
     * 查询充值
     *
     * @param id 充值ID
     * @return 充值
     */
    public Charge selectChargeById(String id);

    /**
     * 查询充值列表
     *
     * @param charge 充值
     * @return 充值集合
     */
    public List<Charge> selectChargeList(Charge charge);

    /**
     * 新增充值
     *
     * @param charge 充值
     * @return 结果
     */
    public int insertCharge(Charge charge);

    /**
     * 修改充值
     *
     * @param charge 充值
     * @return 结果
     */
    public int updateCharge(Charge charge);

    /**
     * 批量删除充值
     *
     * @param ids 需要删除的充值ID
     * @return 结果
     */
    public int deleteChargeByIds(String[] ids);

    /**
     * 删除充值信息
     *
     * @param id 充值ID
     * @return 结果
     */
    public int deleteChargeById(String id);
}