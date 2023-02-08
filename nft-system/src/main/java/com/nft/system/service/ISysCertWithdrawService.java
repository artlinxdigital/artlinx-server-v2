package com.nft.system.service;

import java.util.List;

import com.nft.system.domain.SysCertWithdraw;

/**
 * 出账记录Service接口
 *
 * @author nft
 * @date 2021-07-26
 */
public interface ISysCertWithdrawService {

    /**
     * 查询出账记录
     *
     * @param certificationWithdrawId 出账记录ID
     * @return 出账记录
     */
    public SysCertWithdraw selectSysCertWithdrawById(Long certificationWithdrawId);

    /**
     * 查询出账记录列表
     *
     * @param sysCertWithdraw 出账记录
     * @return 出账记录集合
     */
    public List<SysCertWithdraw> selectSysCertWithdrawList(SysCertWithdraw sysCertWithdraw);

    /**
     * 查询提现记录列表
     *
     * @param sysCertWithdraw 提现记录
     * @return 提现记录集合
     */
    public List<SysCertWithdraw> selectSysCertBillList(SysCertWithdraw sysCertWithdraw);

    /**
     * 新增出账记录
     *
     * @param sysCertWithdraw 出账记录
     * @return 结果
     */
    public int insertSysCertWithdraw(SysCertWithdraw sysCertWithdraw);

    /**
     * 修改出账记录
     *
     * @param sysCertWithdraw 出账记录
     * @return 结果
     */
    public int updateSysCertWithdraw(SysCertWithdraw sysCertWithdraw);

    /**
     * 修改提现状态
     *
     * @param sysCertWithdraw 提现记录
     * @return 结果
     */
    public int updateSysCertBillStatus(SysCertWithdraw sysCertWithdraw);

    /**
     * 批量删除出账记录
     *
     * @param certificationWithdrawIds 需要删除的出账记录ID
     * @return 结果
     */
    public int deleteSysCertWithdrawByIds(Long[] certificationWithdrawIds);

    /**
     * 删除出账记录信息
     *
     * @param certificationWithdrawId 出账记录ID
     * @return 结果
     */
    public int deleteSysCertWithdrawById(Long certificationWithdrawId);
}