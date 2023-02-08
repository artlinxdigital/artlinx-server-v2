package com.nft.mall.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nft.mall.domain.Certification;
import org.apache.ibatis.annotations.Param;

/**
 * 商城用户Mapper接口
 *
 * @author nft
 * @date 2021-05-12
 */
public interface CertificationMapper
{
    /**
     * 查询商城用户
     *
     * @param id 商城用户ID
     * @return 商城用户
     */
    public Certification selectCertificationById(Long id);

    /**
     * 查询商城用户列表
     *
     * @param certification 商城用户
     * @return 商城用户集合
     */
    public List<Certification> selectCertificationList(Certification certification);

    /**
     * 新增商城用户
     *
     * @param certification 商城用户
     * @return 结果
     */
    public int insertCertification(Certification certification);

    /**
     * 修改商城用户
     *
     * @param certification 商城用户
     * @return 结果
     */
    public int updateCertification(Certification certification);

    /**
     * 删除商城用户
     *
     * @param id 商城用户ID
     * @return 结果
     */
    public int deleteCertificationById(Long id);

    /**
     * 批量删除商城用户
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCertificationByIds(Long[] ids);

    /**
     * 查询注册信息
     * @param account
     * @param type
     * @return 结果
     */
    Certification selectByAccountAndType(@Param("account") String account, @Param("type") Integer type);

    /**
     * 查询账号信息
     * @param account
     * @return 结果
     */
    List<Certification> listByAccount(@Param("account") String account);

    /**
     * 查询账号信息
     * @param account
     * @param type
     * @return 结果
     */
    List<Certification> listByAccountAndType(@Param("account") String account, @Param("type") Integer type);

    /**
     * 查询账号信息
     * @param walletAddress
     * @return 结果
     */
    Certification selectByWalletAddress(String walletAddress);

    /**
     * 查询账号信息
     * @param id 主键ID
     * @return 结果
     */
    Certification selectByIdAndType(@Param("id") Long id, @Param("type") int type);

    /**
     * 删除账号信息
     * @param walletAddress
     * @return 结果
     */
    int deleteByWalletAddress(String walletAddress);

    /**
     * 根据地址 名称 证件号 验证是否实名
     * @param map
     * @return 结果
     */
    Certification selectInfoByMap(Map<String, String> map);

    /**
     * 用户推荐人列表
     * @param myCode
     * @return 结果
     */
    List<Certification> selectByReferralCode(String myCode);

    /**
     * 查询用户
     * @param myCode
     * @return 结果
     */
    Certification selectByMyCode(String myCode);

    /**
     * 用户列表
     * @param certificationIdSet
     * @return 结果
     */
    List<Certification> selectByCertificateIdSet(@Param("certificationIdSet") Set<Long> certificationIdSet);
}