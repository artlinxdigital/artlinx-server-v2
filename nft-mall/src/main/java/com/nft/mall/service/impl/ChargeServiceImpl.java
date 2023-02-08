package com.nft.mall.service.impl;

import java.util.List;
import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.ChargeMapper;
import com.nft.mall.domain.Charge;
import com.nft.mall.service.IChargeService;

/**
 * 充值Service业务层处理
 *
 * @author nft
 * @date 2021-05-12
 */
@Service
public class ChargeServiceImpl implements IChargeService
{
    @Autowired
    private ChargeMapper chargeMapper;

    /**
     * 查询充值
     *
     * @param id 充值ID
     * @return 充值
     */
    @Override
    public Charge selectChargeById(String id)
    {
        return chargeMapper.selectChargeById(id);
    }

    /**
     * 查询充值列表
     *
     * @param charge 充值
     * @return 充值
     */
    @Override
    public List<Charge> selectChargeList(Charge charge)
    {
        return chargeMapper.selectChargeList(charge);
    }

    /**
     * 新增充值
     *
     * @param charge 充值
     * @return 结果
     */
    @Override
    public int insertCharge(Charge charge)
    {
        charge.setCreateTime(DateUtils.getNowDate());
        return chargeMapper.insertCharge(charge);
    }

    /**
     * 修改充值
     *
     * @param charge 充值
     * @return 结果
     */
    @Override
    public int updateCharge(Charge charge)
    {
        charge.setUpdateTime(DateUtils.getNowDate());
        return chargeMapper.updateCharge(charge);
    }

    /**
     * 批量删除充值
     *
     * @param ids 需要删除的充值ID
     * @return 结果
     */
    @Override
    public int deleteChargeByIds(String[] ids)
    {
        return chargeMapper.deleteChargeByIds(ids);
    }

    /**
     * 删除充值信息
     *
     * @param id 充值ID
     * @return 结果
     */
    @Override
    public int deleteChargeById(String id)
    {
        return chargeMapper.deleteChargeById(id);
    }
}
