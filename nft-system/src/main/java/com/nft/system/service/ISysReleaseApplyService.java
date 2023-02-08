package com.nft.system.service;

import java.util.List;

import com.nft.system.domain.SysReleaseApply;

/**
 * 铸造权限申请信息Service接口
 *
 * @author nft
 * @date 2021-07-24
 */
public interface ISysReleaseApplyService {

    /**
     * 查询铸造权限申请信息
     *
     * @param releaseApplyId 铸造权限申请信息ID
     * @return 铸造权限申请信息
     */
    public SysReleaseApply selectSysReleaseApplyById(Long releaseApplyId);

    /**
     * 查询铸造权限申请信息列表
     *
     * @param sysReleaseApply 铸造权限申请信息
     * @return 铸造权限申请信息集合
     */
    public List<SysReleaseApply> selectSysReleaseApplyList(SysReleaseApply sysReleaseApply);

    /**
     * 新增铸造权限申请信息
     *
     * @param sysReleaseApply 铸造权限申请信息
     * @return 结果
     */
    public int insertSysReleaseApply(SysReleaseApply sysReleaseApply);

    /**
     * 修改铸造权限申请信息
     *
     * @param sysReleaseApply 铸造权限申请信息
     * @return 结果
     */
    public int updateSysReleaseApply(SysReleaseApply sysReleaseApply);

    /**
     * 批量删除铸造权限申请信息
     *
     * @param releaseApplyIds 需要删除的铸造权限申请信息ID
     * @return 结果
     */
    public int deleteSysReleaseApplyByIds(Long[] releaseApplyIds);

    /**
     * 删除铸造权限申请信息信息
     *
     * @param releaseApplyId 铸造权限申请信息ID
     * @return 结果
     */
    public int deleteSysReleaseApplyById(Long releaseApplyId);

    /**
     * 批量审核铸造权限申请信息
     *
     * @param releaseApplyIds 需要审核的铸造权限申请信息ID
     * @param status 状态
     * @return 结果
     */
    public int auditSysReleaseApplyByIds(Long[] releaseApplyIds, Integer status);
}