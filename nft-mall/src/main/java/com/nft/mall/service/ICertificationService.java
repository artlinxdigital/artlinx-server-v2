package com.nft.mall.service;

import java.util.List;

import com.nft.mall.domain.Certification;

/**
 * 商城用户Service接口
 *
 * @author nft
 * @date 2021-05-12
 */
public interface ICertificationService {
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
     * 批量删除商城用户
     *
     * @param ids 需要删除的商城用户ID
     * @return 结果
     */
    public int deleteCertificationByIds(Long[] ids);

    /**
     * 删除商城用户信息
     *
     * @param id 商城用户ID
     * @return 结果
     */
    public int deleteCertificationById(Long id);

}
