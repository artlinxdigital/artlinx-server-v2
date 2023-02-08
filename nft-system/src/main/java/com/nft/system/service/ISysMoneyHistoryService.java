package com.nft.system.service;

import java.util.List;

import com.nft.system.domain.SysMoneyHistory;

/**
 * 钱支出收入记录Service接口
 *
 * @author nft
 * @date 2021-11-04
 */
public interface ISysMoneyHistoryService {

    /**
     * 查询钱支出收入记录
     *
     * @param id 钱支出收入记录ID
     * @return 钱支出收入记录
     */
    public SysMoneyHistory selectSysMoneyHistoryById(String id);

    /**
     * 查询钱支出收入记录列表
     *
     * @param sysMoneyHistory 钱支出收入记录
     * @return 钱支出收入记录集合
     */
    public List<SysMoneyHistory> selectSysMoneyHistoryList(SysMoneyHistory sysMoneyHistory);

    /**
     * 新增钱支出收入记录
     *
     * @param sysMoneyHistory 钱支出收入记录
     * @return 结果
     */
    public int insertSysMoneyHistory(SysMoneyHistory sysMoneyHistory);

    /**
     * 修改钱支出收入记录
     *
     * @param sysMoneyHistory 钱支出收入记录
     * @return 结果
     */
    public int updateSysMoneyHistory(SysMoneyHistory sysMoneyHistory);

    /**
     * 批量删除钱支出收入记录
     *
     * @param ids 需要删除的钱支出收入记录ID
     * @return 结果
     */
    public int deleteSysMoneyHistoryByIds(String[] ids);

    /**
     * 删除钱支出收入记录信息
     *
     * @param id 钱支出收入记录ID
     * @return 结果
     */
    public int deleteSysMoneyHistoryById(String id);
}