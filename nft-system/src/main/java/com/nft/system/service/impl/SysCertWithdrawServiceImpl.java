package com.nft.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nft.common.utils.DateUtils;
import com.nft.system.domain.SysCertification;
import com.nft.system.mapper.SysCertificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.system.mapper.SysCertWithdrawMapper;
import com.nft.system.domain.SysCertWithdraw;
import com.nft.system.service.ISysCertWithdrawService;

/**
 * 出账记录Service业务层处理
 *
 * @author nft
 * @date 2021-07-26
 */
@Service
public class SysCertWithdrawServiceImpl implements ISysCertWithdrawService {

    @Autowired
    private SysCertWithdrawMapper sysCertWithdrawMapper;

    @Autowired
    private SysCertificationMapper sysCertificationMapper;

    /**
     * 查询出账记录
     *
     * @param certificationWithdrawId 出账记录ID
     * @return 出账记录
     */
    @Override
    public SysCertWithdraw selectSysCertWithdrawById(Long certificationWithdrawId) {
        return sysCertWithdrawMapper.selectSysCertWithdrawById(certificationWithdrawId);
    }

    /**
     * 查询出账记录列表
     *
     * @param sysCertWithdraw 出账记录
     * @return 出账记录
     */
    @Override
    public List<SysCertWithdraw> selectSysCertWithdrawList(SysCertWithdraw sysCertWithdraw) {
        List<SysCertWithdraw> withdrawList = sysCertWithdrawMapper.selectSysCertWithdrawList(sysCertWithdraw);
        if (CollectionUtil.isNotEmpty(withdrawList)) {
            Set<Long> certificationIdSet = withdrawList.stream().map(SysCertWithdraw::getCertificationId).collect(Collectors.toSet());
            List<SysCertification> certificationList = sysCertificationMapper.selectByCertificationIdSet(certificationIdSet);
            Map<Long, SysCertification> certificationMap = certificationList.stream().collect(Collectors.toMap(SysCertification::getId, Function.identity()));
            withdrawList.stream().forEach(withdraw -> {
                SysCertification certification = certificationMap.getOrDefault(withdraw.getCertificationId(), null);
                withdraw.setRealName(ObjectUtil.isNull(certification) ? "" : certification.getRealName());
            });
        }
        return withdrawList;
    }

    /**
     * 查询提现记录列表
     *
     * @param sysCertWithdraw 提现记录
     * @return 提现记录集合
     */
    @Override
    public List<SysCertWithdraw> selectSysCertBillList(SysCertWithdraw sysCertWithdraw) {
        List<SysCertWithdraw> withdrawList = sysCertWithdrawMapper.selectSysCertBillList(sysCertWithdraw);
        if (CollUtil.isNotEmpty(withdrawList)) {
            Set<Long> certificationIdSet = withdrawList.stream().map(SysCertWithdraw::getCertificationId).collect(Collectors.toSet());
            List<SysCertification> certificationList = sysCertificationMapper.selectByCertificationIdSet(certificationIdSet);
            Map<Long, SysCertification> certificationMap = certificationList.stream().collect(Collectors.toMap(SysCertification::getId, Function.identity()));
            withdrawList.stream().forEach(withdraw -> {
                SysCertification certification = certificationMap.getOrDefault(withdraw.getCertificationId(), null);
                withdraw.setRealName(ObjectUtil.isNull(certification) ? "" : certification.getRealName());
            });
        }
        return withdrawList;
    }

    /**
     * 新增出账记录
     *
     * @param sysCertWithdraw 出账记录
     * @return 结果
     */
    @Override
    public int insertSysCertWithdraw(SysCertWithdraw sysCertWithdraw) {
        sysCertWithdraw.setCreateTime(DateUtils.getNowDate());
        return sysCertWithdrawMapper.insertSysCertWithdraw(sysCertWithdraw);
    }

    /**
     * 修改出账记录
     *
     * @param sysCertWithdraw 出账记录
     * @return 结果
     */
    @Override
    public int updateSysCertWithdraw(SysCertWithdraw sysCertWithdraw) {
        sysCertWithdraw.setUpdateTime(DateUtils.getNowDate());
        return sysCertWithdrawMapper.updateSysCertWithdraw(sysCertWithdraw);
    }

    /**
     * 修改提现状态
     *
     * @param sysCertWithdraw 提现记录
     * @return 结果
     */
    @Override
    public int updateSysCertBillStatus(SysCertWithdraw sysCertWithdraw) {
        sysCertWithdraw.setUpdateTime(DateUtils.getNowDate());
        return sysCertWithdrawMapper.updateSysCertWithdraw(sysCertWithdraw);
    }

    /**
     * 批量删除出账记录
     *
     * @param certificationWithdrawIds 需要删除的出账记录ID
     * @return 结果
     */
    @Override
    public int deleteSysCertWithdrawByIds(Long[] certificationWithdrawIds) {
        return sysCertWithdrawMapper.deleteSysCertWithdrawByIds(certificationWithdrawIds);
    }

    /**
     * 删除出账记录信息
     *
     * @param certificationWithdrawId 出账记录ID
     * @return 结果
     */
    @Override
    public int deleteSysCertWithdrawById(Long certificationWithdrawId) {
        return sysCertWithdrawMapper.deleteSysCertWithdrawById(certificationWithdrawId);
    }
}