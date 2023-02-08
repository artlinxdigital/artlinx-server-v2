package com.nft.mall.mapper;

import java.util.List;
import com.nft.mall.domain.ReleaseApply;
import org.apache.ibatis.annotations.Param;

/**
 * 藏品发布申请信息Mapper接口
 *
 * @author nft
 * @date 2021-06-06
 */
public interface ReleaseApplyMapper
{
    /**
     * 查询藏品发布申请信息
     *
     * @param releaseApplyId 藏品发布申请信息ID
     * @return 藏品发布申请信息
     */
    public ReleaseApply selectReleaseApplyById(Long releaseApplyId);

    /**
     * 查询藏品发布申请信息列表
     *
     * @param releaseApply 藏品发布申请信息
     * @return 藏品发布申请信息集合
     */
    public List<ReleaseApply> selectReleaseApplyList(ReleaseApply releaseApply);

    /**
     * 新增藏品发布申请信息
     *
     * @param releaseApply 藏品发布申请信息
     * @return 结果
     */
    public int insertReleaseApply(ReleaseApply releaseApply);

    /**
     * 修改藏品发布申请信息
     *
     * @param releaseApply 藏品发布申请信息
     * @return 结果
     */
    public int updateReleaseApply(ReleaseApply releaseApply);

    /**
     * 删除藏品发布申请信息
     *
     * @param releaseApplyId 藏品发布申请信息ID
     * @return 结果
     */
    public int deleteReleaseApplyById(Long releaseApplyId);

    /**
     * 批量删除藏品发布申请信息
     *
     * @param releaseApplyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteReleaseApplyByIds(Long[] releaseApplyIds);

    /**
     * 查询藏品发布申请信息
     *
     * @param certificationId 用户ID
     * @return 藏品发布申请信息
     */
    public ReleaseApply selectByCertificationId(@Param("certificationId") Long certificationId);

    /**
     * 查询藏品发布申请信息
     *
     * @param certificationId 用户ID
     * @param applyStatus 申请状态
     * @return 藏品发布申请信息
     */
    public ReleaseApply selectByCertificationIdAndStatus(@Param("certificationId") Long certificationId, @Param("applyStatus") Integer applyStatus);
}