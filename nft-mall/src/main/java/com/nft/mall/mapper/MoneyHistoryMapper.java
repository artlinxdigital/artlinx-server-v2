package com.nft.mall.mapper;

import com.nft.mall.domain.Charge;
import com.nft.mall.domain.MoneyHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 充值Mapper接口
 *
 * @author nft
 * @date 2021-05-12
 */
public interface MoneyHistoryMapper
{
    /**
     * 查询
     *
     * @param id 充值ID
     * @return 充值
     */
    public Charge selectChargeById(String id);

    /**
     * 查询列表
     *
     * @param
     * @return 充值集合
     */
    public List<MoneyHistory> selectChargeList(MoneyHistory moneyHistory);

    /**
     * 新增
     *
     * @param
     * @return 结果
     */
    public int insertCharge(MoneyHistory moneyHistory);

    /**
     * 修改
     *
     * @param
     * @return 结果
     */
    public int updateCharge(MoneyHistory moneyHistory);

    /**
     * 删除
     *
     * @param id 充值ID
     * @return 结果
     */
    public int deleteChargeById(String id);

    /**
     * 批量删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteChargeByIds(String[] ids);

    /**
     * 用户状态是完成的金额历史记录
     * @param moneyHistory
     * @return
     */
    public List<MoneyHistory> selectMyHistoryOfUseful(MoneyHistory moneyHistory);
    /**
     * 查询用户记录列表
     *
     * @param certificationId 用户ID
     * @param status
     * @return 结果
     */
    List<MoneyHistory> listByCertificationIdAndStatus(@Param("certificationId") Long certificationId, @Param("status") Integer status);
}
