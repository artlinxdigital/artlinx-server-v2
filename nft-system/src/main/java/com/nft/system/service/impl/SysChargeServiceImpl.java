package com.nft.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nft.common.utils.DateUtils;
import com.nft.system.domain.SysCertification;
import com.nft.system.mapper.SysCertificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.system.mapper.SysChargeMapper;
import com.nft.system.domain.SysCharge;
import com.nft.system.service.ISysChargeService;

/**
 * 入账记录Service业务层处理
 *
 * @author nft
 * @date 2021-07-25
 */
@Service
public class SysChargeServiceImpl implements ISysChargeService {

    @Autowired
    private SysChargeMapper sysChargeMapper;

    @Autowired
    private SysCertificationMapper sysCertificationMapper;

    /**
     * 查询入账记录
     *
     * @param id 入账记录ID
     * @return 入账记录
     */
    @Override
    public SysCharge selectSysChargeById(String id) {
        return sysChargeMapper.selectSysChargeById(id);
    }

    /**
     * 查询入账记录列表
     *
     * @param sysCharge 入账记录
     * @return 入账记录
     */
    @Override
    public List<SysCharge> selectSysChargeList(SysCharge sysCharge) {
        List<SysCharge> chargeList = sysChargeMapper.selectSysChargeList(sysCharge);
        if (CollectionUtil.isNotEmpty(chargeList)) {
            Set<Long> certificationIdSet = chargeList.stream().map(SysCharge::getCertificationId).collect(Collectors.toSet());
            List<SysCertification> certificationList = sysCertificationMapper.selectByCertificationIdSet(certificationIdSet);
            Map<Long, SysCertification> certificationMap = certificationList.stream().collect(Collectors.toMap(SysCertification::getId, Function.identity()));
            chargeList.stream().forEach(charge -> {
                SysCertification certification = certificationMap.getOrDefault(charge.getCertificationId(), null);
                charge.setRealName(ObjectUtil.isNull(certification) ? "" : certification.getRealName());
            });
        }
        return chargeList;
    }

    /**
     * 新增入账记录
     *
     * @param sysCharge 入账记录
     * @return 结果
     */
    @Override
    public int insertSysCharge(SysCharge sysCharge) {
        sysCharge.setCreateTime(DateUtils.getNowDate());
        return sysChargeMapper.insertSysCharge(sysCharge);
    }

    /**
     * 修改入账记录
     *
     * @param sysCharge 入账记录
     * @return 结果
     */
    @Override
    public int updateSysCharge(SysCharge sysCharge) {
        sysCharge.setUpdateTime(DateUtils.getNowDate());
        return sysChargeMapper.updateSysCharge(sysCharge);
    }

    /**
     * 批量删除入账记录
     *
     * @param ids 需要删除的入账记录ID
     * @return 结果
     */
    @Override
    public int deleteSysChargeByIds(String[] ids) {
        return sysChargeMapper.deleteSysChargeByIds(ids);
    }

    /**
     * 删除入账记录信息
     *
     * @param id 入账记录ID
     * @return 结果
     */
    @Override
    public int deleteSysChargeById(String id) {
        return sysChargeMapper.deleteSysChargeById(id);
    }
}