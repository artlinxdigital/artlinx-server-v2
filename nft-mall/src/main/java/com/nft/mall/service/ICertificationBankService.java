package com.nft.mall.service;

import java.util.List;

import com.nft.mall.domain.CertificationBank;

/**
 * 用户银行配置信息Service接口
 *
 * @author nft
 * @date 2021-11-02
 */
public interface ICertificationBankService {

    /**
     * 查询用户银行配置信息
     *
     * @param certificationBankId 用户银行配置信息ID
     * @return 用户银行配置信息
     */
    CertificationBank selectCertificationBankById(Long certificationBankId);

    /**
     * 查询用户银行配置信息列表
     *
     * @param certificationBank 用户银行配置信息
     * @return 用户银行配置信息集合
     */
    List<CertificationBank> selectCertificationBankList(CertificationBank certificationBank);

    /**
     * 新增用户银行配置信息
     *
     * @param certificationBank 用户银行配置信息
     * @return 结果
     */
    int insertCertificationBank(CertificationBank certificationBank);

    /**
     * 修改用户银行配置信息
     *
     * @param certificationBank 用户银行配置信息
     * @return 结果
     */
    int updateCertificationBank(CertificationBank certificationBank);

    /**
     * 批量删除用户银行配置信息
     *
     * @param certificationBankIds 需要删除的用户银行配置信息ID
     * @return 结果
     */
    int deleteCertificationBankByIds(Long[] certificationBankIds);

    /**
     * 删除用户银行配置信息信息
     *
     * @param certificationBankId 用户银行配置信息ID
     * @return 结果
     */
    int deleteCertificationBankById(Long certificationBankId);
}