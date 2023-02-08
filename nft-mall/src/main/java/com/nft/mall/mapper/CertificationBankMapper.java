package com.nft.mall.mapper;

import java.util.List;

import com.nft.mall.domain.CertificationBank;

/**
 * 用户银行配置信息Mapper接口
 *
 * @author nft
 * @date 2021-11-02
 */
public interface CertificationBankMapper {

    /**
     * 查询用户银行配置信息
     *
     * @param certificationBankId 用户银行配置信息ID
     * @return 用户银行配置信息
     */
    public CertificationBank selectCertificationBankById(Long certificationBankId);

    /**
     * 查询用户银行配置信息列表
     *
     * @param certificationBank 用户银行配置信息
     * @return 用户银行配置信息集合
     */
    public List<CertificationBank> selectCertificationBankList(CertificationBank certificationBank);

    /**
     * 新增用户银行配置信息
     *
     * @param certificationBank 用户银行配置信息
     * @return 结果
     */
    public int insertCertificationBank(CertificationBank certificationBank);

    /**
     * 修改用户银行配置信息
     *
     * @param certificationBank 用户银行配置信息
     * @return 结果
     */
    public int updateCertificationBank(CertificationBank certificationBank);

    /**
     * 删除用户银行配置信息
     *
     * @param certificationBankId 用户银行配置信息ID
     * @return 结果
     */
    public int deleteCertificationBankById(Long certificationBankId);

    /**
     * 批量删除用户银行配置信息
     *
     * @param certificationBankIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCertificationBankByIds(Long[] certificationBankIds);
}