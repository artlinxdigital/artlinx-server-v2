package com.nft.system.mapper;

import java.util.List;

import com.nft.system.domain.SysReleaseApply;
import org.apache.ibatis.annotations.Param;

/**
 * 铸造权限申请信息Mapper接口
 *
 * @author nft
 * @date 2021-07-24
 */
public interface SysReleaseApplyMapper {

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
     * 删除铸造权限申请信息
     *
     * @param releaseApplyId 铸造权限申请信息ID
     * @return 结果
     */
    public int deleteSysReleaseApplyById(Long releaseApplyId);

    /**
     * 批量删除铸造权限申请信息
     *
     * @param releaseApplyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysReleaseApplyByIds(Long[] releaseApplyIds);

    /**
     * 批量查询铸造权限申请信息
     *
     * @param releaseApplyIds 需要查询的数据ID
     * @return 结果
     */
    public List<SysReleaseApply> selectSysReleaseApplyByIds(Long[] releaseApplyIds);

    /**
     * 批量删除铸造权限申请信息
     *
     * @param releaseApplyIds 需要删除的数据ID
     * @param applyStatus 状态
     * @return 结果
     */
    public int auditSysReleaseApplyByIds(@Param("releaseApplyIds") Long[] releaseApplyIds, @Param("applyStatus") Integer applyStatus);
}