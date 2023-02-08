package com.nft.mall.service.impl;

import java.util.List;

import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.CertificationBankMapper;
import com.nft.mall.domain.CertificationBank;
import com.nft.mall.service.ICertificationBankService;

/**
 * 用户银行配置信息Service业务层处理
 *
 * @author nft
 * @date 2021-11-02
 */
@Service
public class CertificationBankServiceImpl implements ICertificationBankService {

    @Autowired
    private CertificationBankMapper certificationBankMapper;

    /**
     * 查询用户银行配置信息
     *
     * @param certificationBankId 用户银行配置信息ID
     * @return 用户银行配置信息
     */
    @Override
    public CertificationBank selectCertificationBankById(Long certificationBankId) {
        return certificationBankMapper.selectCertificationBankById(certificationBankId);
    }

    /**
     * 查询用户银行配置信息列表
     *
     * @param certificationBank 用户银行配置信息
     * @return 用户银行配置信息
     */
    @Override
    public List<CertificationBank> selectCertificationBankList(CertificationBank certificationBank) {
        return certificationBankMapper.selectCertificationBankList(certificationBank);
    }

    /**
     * 新增用户银行配置信息
     *
     * @param certificationBank 用户银行配置信息
     * @return 结果
     */
    @Override
    public int insertCertificationBank(CertificationBank certificationBank) {
        certificationBank.setCreateTime(DateUtils.getNowDate());
        return certificationBankMapper.insertCertificationBank(certificationBank);
    }

    /**
     * 修改用户银行配置信息
     *
     * @param certificationBank 用户银行配置信息
     * @return 结果
     */
    @Override
    public int updateCertificationBank(CertificationBank certificationBank) {
        certificationBank.setUpdateTime(DateUtils.getNowDate());
        return certificationBankMapper.updateCertificationBank(certificationBank);
    }

    /**
     * 批量删除用户银行配置信息
     *
     * @param certificationBankIds 需要删除的用户银行配置信息ID
     * @return 结果
     */
    @Override
    public int deleteCertificationBankByIds(Long[] certificationBankIds) {
        return certificationBankMapper.deleteCertificationBankByIds(certificationBankIds);
    }

    /**
     * 删除用户银行配置信息信息
     *
     * @param certificationBankId 用户银行配置信息ID
     * @return 结果
     */
    @Override
    public int deleteCertificationBankById(Long certificationBankId) {
        return certificationBankMapper.deleteCertificationBankById(certificationBankId);
    }
}