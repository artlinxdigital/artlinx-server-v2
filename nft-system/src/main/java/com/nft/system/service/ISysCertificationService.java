package com.nft.system.service;

import java.util.List;

import com.nft.common.core.domain.AjaxResult;
import com.nft.system.domain.SysCertification;
import com.nft.system.domain.param.SysRealAuditParam;

/**
 * 会员信息Service接口
 *
 * @author nft
 * @date 2021-07-24
 */
public interface ISysCertificationService {

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
     * 批量删除会员信息
     *
     * @param ids 需要删除的会员信息ID
     * @return 结果
     */
    public int deleteSysCertificationByIds(Long[] ids);

    /**
     * 删除会员信息信息
     *
     * @param id 会员信息ID
     * @return 结果
     */
    public int deleteSysCertificationById(Long id);

    /**
     * 实名审核
     *
     * @param auditParam 审核参数
     * @return 结果
     */
    AjaxResult verifyRealName(SysRealAuditParam auditParam);
}