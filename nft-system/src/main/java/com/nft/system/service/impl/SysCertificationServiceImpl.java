package com.nft.system.service.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.EmailInfo;
import com.nft.common.enums.CertificationStatus;
import com.nft.common.enums.ReleaseStatus;
import com.nft.common.utils.DateUtils;
import com.nft.common.utils.StringUtils;
import com.nft.common.utils.sms.EmailUtils;
import com.nft.system.domain.param.SysRealAuditParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.system.mapper.SysCertificationMapper;
import com.nft.system.domain.SysCertification;
import com.nft.system.service.ISysCertificationService;

/**
 * 会员信息Service业务层处理
 *
 * @author nft
 * @date 2021-07-24
 */
@Slf4j
@Service
public class SysCertificationServiceImpl implements ISysCertificationService {

    @Autowired
    private SysCertificationMapper sysCertificationMapper;

    /**
     * 查询会员信息
     *
     * @param id 会员信息ID
     * @return 会员信息
     */
    @Override
    public SysCertification selectSysCertificationById(Long id) {
        return sysCertificationMapper.selectSysCertificationById(id);
    }

    /**
     * 查询会员信息列表
     *
     * @param sysCertification 会员信息
     * @return 会员信息
     */
    @Override
    public List<SysCertification> selectSysCertificationList(SysCertification sysCertification) {
        List<SysCertification> list = sysCertificationMapper.selectSysCertificationList(sysCertification);
        list.stream().forEach(cert -> {
            cert.setNumber(cert.getNumber());
            String realName = cert.getRealName();
            cert.setRealName(StringUtils.isBlank(realName) ? "-" : realName);
            cert.setMobile(cert.getMobile());
        });
        return list;
    }

    /**
     * 新增会员信息
     *
     * @param sysCertification 会员信息
     * @return 结果
     */
    @Override
    public int insertSysCertification(SysCertification sysCertification) {
        sysCertification.setCreateTime(DateUtils.getNowDate());
        return sysCertificationMapper.insertSysCertification(sysCertification);
    }

    /**
     * 修改会员信息
     *
     * @param sysCertification 会员信息
     * @return 结果
     */
    @Override
    public int updateSysCertification(SysCertification sysCertification) {
        sysCertification.setUpdateTime(DateUtils.getNowDate());
        return sysCertificationMapper.updateSysCertification(sysCertification);
    }

    /**
     * 批量删除会员信息
     *
     * @param ids 需要删除的会员信息ID
     * @return 结果
     */
    @Override
    public int deleteSysCertificationByIds(Long[] ids) {
        return sysCertificationMapper.deleteSysCertificationByIds(ids);
    }

    /**
     * 删除会员信息信息
     *
     * @param id 会员信息ID
     * @return 结果
     */
    @Override
    public int deleteSysCertificationById(Long id) {
        return sysCertificationMapper.deleteSysCertificationById(id);
    }

    /**
     * 实名审核
     *
     * @param auditParam 审核参数
     * @return 结果
     */
    @Override
    public AjaxResult verifyRealName(SysRealAuditParam auditParam) {
        // 用户ID
        Long id = auditParam.getId();
        SysCertification certification = sysCertificationMapper.selectSysCertificationById(id);
        if (ObjectUtil.isNull(certification)) {
            AjaxResult.error("参数错误,请联系管理员");
        }
        // 审核状态
        Integer status = auditParam.getStatus();
        SysCertification updateCert = new SysCertification();
        updateCert.setId(id);
        updateCert.setStatus(status);
        if (status.equals(CertificationStatus.CHECK_REJECT.getCode())) {
            updateCert.setRealAuditTime(new Date());
            updateCert.setAuditContent(StringUtils.trimToEmpty(auditParam.getAuditContent()));
        }
        // 实名审核通过发送邮件
        String mobile = certification.getMobile();
        if (status.equals(CertificationStatus.SUCCESS.getCode())) {
            updateCert.setReleaseStatus(ReleaseStatus.YES.getCode());
            EmailInfo info = new EmailInfo(mobile, EmailUtils.SUBJECT_KYC, EmailUtils.contentForKyc());
            if (EmailUtils.sendHtmlMail(info)) {
                log.info("实名成功, 邮箱：【" + mobile + "】发送邮件结束");
            }
        }
        updateCert.setRzTime(new Date());
        updateCert.setUpdateTime(new Date());
        int effectNum = sysCertificationMapper.updateSysCertification(updateCert);
        return effectNum == 1 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }
}