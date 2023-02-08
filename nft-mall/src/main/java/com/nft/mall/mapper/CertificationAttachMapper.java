package com.nft.mall.mapper;

import java.util.List;
import java.util.Set;

import com.nft.mall.domain.CertificationAttach;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息补充Mapper接口
 *
 * @author nft
 * @date 2021-05-13
 */
public interface CertificationAttachMapper
{
    /**
     * 查询用户信息补充
     *
     * @param certificationAttachId 用户信息补充ID
     * @return 用户信息补充
     */
    public CertificationAttach selectCertificationAttachById(Long certificationAttachId);

    /**
     * 查询用户信息补充列表
     *
     * @param certificationAttach 用户信息补充
     * @return 用户信息补充集合
     */
    public List<CertificationAttach> selectCertificationAttachList(CertificationAttach certificationAttach);

    /**
     * 新增用户信息补充
     *
     * @param certificationAttach 用户信息补充
     * @return 结果
     */
    public int insertCertificationAttach(CertificationAttach certificationAttach);

    /**
     * 修改用户信息补充
     *
     * @param certificationAttach 用户信息补充
     * @return 结果
     */
    public int updateCertificationAttach(CertificationAttach certificationAttach);

    /**
     * 删除用户信息补充
     *
     * @param certificationAttachId 用户信息补充ID
     * @return 结果
     */
    public int deleteCertificationAttachById(Long certificationAttachId);

    /**
     * 批量删除用户信息补充
     *
     * @param certificationAttachIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCertificationAttachByIds(Long[] certificationAttachIds);

    /**
     * 批量查询用户信息补充
     *
     * @param certificationIdSet 需要删除的数据ID
     * @return 结果
     */
    List<CertificationAttach> selectByCertificationIdSet(@Param("certificationIdSet") Set<Long> certificationIdSet);
}