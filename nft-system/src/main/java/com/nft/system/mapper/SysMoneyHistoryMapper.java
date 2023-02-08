package com.nft.system.mapper;

import java.util.List;

import com.nft.system.domain.SysMoneyHistory;

/**
 * 钱支出收入记录Mapper接口
 *
 * @author nft
 * @date 2021-11-04
 */
public interface SysMoneyHistoryMapper {

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
     * 删除钱支出收入记录
     *
     * @param id 钱支出收入记录ID
     * @return 结果
     */
    public int deleteSysMoneyHistoryById(String id);

    /**
     * 批量删除钱支出收入记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysMoneyHistoryByIds(String[] ids);
}