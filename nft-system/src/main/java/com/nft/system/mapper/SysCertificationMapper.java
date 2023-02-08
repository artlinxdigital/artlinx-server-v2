package com.nft.system.mapper;

import java.util.List;
import java.util.Set;

import com.nft.system.domain.SysCertification;
import org.apache.ibatis.annotations.Param;

/**
 * 会员信息Mapper接口
 *
 * @author nft
 * @date 2021-07-24
 */
public interface SysCertificationMapper {

    /**
     * 查询会员信息
     *
     * @param id 会员信息ID
     * @return 会员信息
     */
    public SysCertification selectSysCertificationById(Long id);

    /**
     * 查询会员信息列表
     *
     * @param sysCertification 会员信息
     * @return 会员信息集合
     */
    public List<SysCertification> selectSysCertificationList(SysCertification sysCertification);

    /**
     * 新增会员信息
     *
     * @param sysCertification 会员信息
     * @return 结果
     */
    public int insertSysCertification(SysCertification sysCertification);

    /**
     * 修改会员信息
     *
     * @param sysCertification 会员信息
     * @return 结果
     */
    public int updateSysCertification(SysCertification sysCertification);

    /**
     * 删除会员信息
     *
     * @param id 会员信息ID
     * @return 结果
     */
    public int deleteSysCertificationById(Long id);

    /**
     * 批量删除会员信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysCertificationByIds(Long[] ids);

    /**
     * 查询会员信息列表
     *
     * @param certificationIdSet 会员信息
     * @return 会员信息集合
     */
    public List<SysCertification> selectByCertificationIdSet(@Param("certificationIdSet") Set<Long> certificationIdSet);

    /**
     * 修改会员信铸造状态
     *
     * @param certificationIdSet 会员ID
     * @param releaseStatus      铸造状态
     * @return 结果
     */
    public int updateReleaseStatusByCertificationIdSet(@Param("certificationIdSet") Set<Long> certificationIdSet, @Param("releaseStatus") Integer releaseStatus);
}