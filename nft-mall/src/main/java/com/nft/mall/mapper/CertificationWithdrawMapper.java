package com.nft.mall.mapper;

import java.util.List;
import com.nft.mall.domain.CertificationWithdraw;

/**
 * 用户提现Mapper接口
 *
 * @author nft
 * @date 2021-06-09
 */
public interface CertificationWithdrawMapper
{
    /**
     * 查询用户提现
     *
     * @param certificationWithdrawId 用户提现ID
     * @return 用户提现
     */
    public CertificationWithdraw selectCertificationWithdrawById(Long certificationWithdrawId);

    /**
     * 查询用户提现列表
     *
     * @param certificationWithdraw 用户提现
     * @return 用户提现集合
     */
    public List<CertificationWithdraw> selectCertificationWithdrawList(CertificationWithdraw certificationWithdraw);

    /**
     * 新增用户提现
     *
     * @param certificationWithdraw 用户提现
     * @return 结果
     */
    public int insertCertificationWithdraw(CertificationWithdraw certificationWithdraw);

    /**
     * 修改用户提现
     *
     * @param certificationWithdraw 用户提现
     * @return 结果
     */
    public int updateCertificationWithdraw(CertificationWithdraw certificationWithdraw);

    /**
     * 删除用户提现
     *
     * @param certificationWithdrawId 用户提现ID
     * @return 结果
     */
    public int deleteCertificationWithdrawById(Long certificationWithdrawId);

    /**
     * 批量删除用户提现
     *
     * @param certificationWithdrawIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCertificationWithdrawByIds(Long[] certificationWithdrawIds);

    /**
     * 查询用户提现列表
     *
     * @param certificationId 用户ID
     * @return 用户提现集合
     */
    public List<CertificationWithdraw> selectByCertificationId(Long certificationId);
}