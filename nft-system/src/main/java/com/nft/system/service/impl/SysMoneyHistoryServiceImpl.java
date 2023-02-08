package com.nft.system.service.impl;

import java.util.List;

import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.system.mapper.SysMoneyHistoryMapper;
import com.nft.system.domain.SysMoneyHistory;
import com.nft.system.service.ISysMoneyHistoryService;

/**
 * 钱支出收入记录Service业务层处理
 *
 * @author nft
 * @date 2021-11-04
 */
@Service
public class SysMoneyHistoryServiceImpl implements ISysMoneyHistoryService {

    @Autowired
    private SysMoneyHistoryMapper sysMoneyHistoryMapper;

    /**
     * 查询钱支出收入记录
     *
     * @param id 钱支出收入记录ID
     * @return 钱支出收入记录
     */
    @Override
    public SysMoneyHistory selectSysMoneyHistoryById(String id) {
        return sysMoneyHistoryMapper.selectSysMoneyHistoryById(id);
    }

    /**
     * 查询钱支出收入记录列表
     *
     * @param sysMoneyHistory 钱支出收入记录
     * @return 钱支出收入记录
     */
    @Override
    public List<SysMoneyHistory> selectSysMoneyHistoryList(SysMoneyHistory sysMoneyHistory) {
        return sysMoneyHistoryMapper.selectSysMoneyHistoryList(sysMoneyHistory);
    }

    /**
     * 新增钱支出收入记录
     *
     * @param sysMoneyHistory 钱支出收入记录
     * @return 结果
     */
    @Override
    public int insertSysMoneyHistory(SysMoneyHistory sysMoneyHistory) {
        sysMoneyHistory.setCreateTime(DateUtils.getNowDate());
        return sysMoneyHistoryMapper.insertSysMoneyHistory(sysMoneyHistory);
    }

    /**
     * 修改钱支出收入记录
     *
     * @param sysMoneyHistory 钱支出收入记录
     * @return 结果
     */
    @Override
    public int updateSysMoneyHistory(SysMoneyHistory sysMoneyHistory) {
        sysMoneyHistory.setUpdateTime(DateUtils.getNowDate());
        return sysMoneyHistoryMapper.updateSysMoneyHistory(sysMoneyHistory);
    }

    /**
     * 批量删除钱支出收入记录
     *
     * @param ids 需要删除的钱支出收入记录ID
     * @return 结果
     */
    @Override
    public int deleteSysMoneyHistoryByIds(String[] ids) {
        return sysMoneyHistoryMapper.deleteSysMoneyHistoryByIds(ids);
    }

    /**
     * 删除钱支出收入记录信息
     *
     * @param id 钱支出收入记录ID
     * @return 结果
     */
    @Override
    public int deleteSysMoneyHistoryById(String id) {
        return sysMoneyHistoryMapper.deleteSysMoneyHistoryById(id);
    }
}