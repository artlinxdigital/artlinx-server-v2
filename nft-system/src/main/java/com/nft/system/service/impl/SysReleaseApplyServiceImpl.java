package com.nft.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nft.common.enums.ApplyStatus;
import com.nft.common.enums.ReleaseStatus;
import com.nft.common.utils.DateUtils;
import com.nft.system.domain.SysCertification;
import com.nft.system.mapper.SysCertificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.system.mapper.SysReleaseApplyMapper;
import com.nft.system.domain.SysReleaseApply;
import com.nft.system.service.ISysReleaseApplyService;

/**
 * 铸造权限申请信息Service业务层处理
 *
 * @author nft
 * @date 2021-07-24
 */
@Service
public class SysReleaseApplyServiceImpl implements ISysReleaseApplyService {

    @Autowired
    private SysReleaseApplyMapper sysReleaseApplyMapper;

    @Autowired
    private SysCertificationMapper sysCertificationMapper;

    /**
     * 查询铸造权限申请信息
     *
     * @param releaseApplyId 铸造权限申请信息ID
     * @return 铸造权限申请信息
     */
    @Override
    public SysReleaseApply selectSysReleaseApplyById(Long releaseApplyId) {
        return sysReleaseApplyMapper.selectSysReleaseApplyById(releaseApplyId);
    }

    /**
     * 查询铸造权限申请信息列表
     *
     * @param sysReleaseApply 铸造权限申请信息
     * @return 铸造权限申请信息
     */
    @Override
    public List<SysReleaseApply> selectSysReleaseApplyList(SysReleaseApply sysReleaseApply) {
        List<SysReleaseApply> applyList = sysReleaseApplyMapper.selectSysReleaseApplyList(sysReleaseApply);
        if (CollectionUtil.isNotEmpty(applyList)) {
            Set<Long> certificationIdSet = applyList.stream().map(SysReleaseApply::getCertificationId).collect(Collectors.toSet());
            List<SysCertification> certificationList = sysCertificationMapper.selectByCertificationIdSet(certificationIdSet);
            Map<Long, SysCertification> certificationMap = certificationList.stream().collect(Collectors.toMap(SysCertification::getId, Function.identity()));
            applyList.stream().forEach(apply -> {
                SysCertification certification = certificationMap.getOrDefault(apply.getCertificationId(), null);
                apply.setRealName(ObjectUtil.isNull(certification) ? "" : certification.getRealName());
                apply.setMobile(ObjectUtil.isNull(certification) ? "" : certification.getMobile());
            });
        }
        return applyList;
    }

    /**
     * 新增铸造权限申请信息
     *
     * @param sysReleaseApply 铸造权限申请信息
     * @return 结果
     */
    @Override
    public int insertSysReleaseApply(SysReleaseApply sysReleaseApply) {
        sysReleaseApply.setCreateTime(DateUtils.getNowDate());
        return sysReleaseApplyMapper.insertSysReleaseApply(sysReleaseApply);
    }

    /**
     * 修改铸造权限申请信息
     *
     * @param sysReleaseApply 铸造权限申请信息
     * @return 结果
     */
    @Override
    public int updateSysReleaseApply(SysReleaseApply sysReleaseApply) {
        sysReleaseApply.setUpdateTime(DateUtils.getNowDate());
        return sysReleaseApplyMapper.updateSysReleaseApply(sysReleaseApply);
    }

    /**
     * 批量删除铸造权限申请信息
     *
     * @param releaseApplyIds 需要删除的铸造权限申请信息ID
     * @return 结果
     */
    @Override
    public int deleteSysReleaseApplyByIds(Long[] releaseApplyIds) {
        return sysReleaseApplyMapper.deleteSysReleaseApplyByIds(releaseApplyIds);
    }

    /**
     * 删除铸造权限申请信息信息
     *
     * @param releaseApplyId 铸造权限申请信息ID
     * @return 结果
     */
    @Override
    public int deleteSysReleaseApplyById(Long releaseApplyId) {
        return sysReleaseApplyMapper.deleteSysReleaseApplyById(releaseApplyId);
    }

    /**
     * 批量审核铸造权限申请信息
     *
     * @param releaseApplyIds 需要审核的铸造权限申请信息ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int auditSysReleaseApplyByIds(Long[] releaseApplyIds, Integer status) {
        if (status.equals(ApplyStatus.YES.getCode())) {
            // 修改用户铸造状态
            List<SysReleaseApply> releaseApplyList = sysReleaseApplyMapper.selectSysReleaseApplyByIds(releaseApplyIds);
            if (CollUtil.isNotEmpty(releaseApplyList)) {
                Set<Long> certificationIdSet = releaseApplyList.stream().map(SysReleaseApply::getCertificationId).collect(Collectors.toSet());
                sysCertificationMapper.updateReleaseStatusByCertificationIdSet(certificationIdSet, ReleaseStatus.YES.getCode());
            }
        }
        return sysReleaseApplyMapper.auditSysReleaseApplyByIds(releaseApplyIds, status);
    }
}