package com.nft.mall.service.impl;

import java.util.*;

import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.CertificationMapper;
import com.nft.mall.domain.Certification;
import com.nft.mall.service.ICertificationService;

/**
 * 商城用户Service业务层处理
 *
 * @author nft
 * @date 2021-05-12
 */
@Service
public class CertificationServiceImpl implements ICertificationService {

    @Autowired
    private CertificationMapper certificationMapper;

    /**
     * 查询商城用户
     *
     * @param id 商城用户ID
     * @return 商城用户
     */
    @Override
    public Certification selectCertificationById(Long id) {
        return certificationMapper.selectCertificationById(id);
    }

    /**
     * 查询商城用户列表
     *
     * @param certification 商城用户
     * @return 商城用户
     */
    @Override
    public List<Certification> selectCertificationList(Certification certification) {
        return certificationMapper.selectCertificationList(certification);
    }

    /**
     * 新增商城用户
     *
     * @param certification 商城用户
     * @return 结果
     */
    @Override
    public int insertCertification(Certification certification) {
        certification.setCreateTime(DateUtils.getNowDate());
        return certificationMapper.insertCertification(certification);
    }

    /**
     * 修改商城用户
     *
     * @param certification 商城用户
     * @return 结果
     */
    @Override
    public int updateCertification(Certification certification) {
        certification.setUpdateTime(DateUtils.getNowDate());
        return certificationMapper.updateCertification(certification);
    }

    /**
     * 批量删除商城用户
     *
     * @param ids 需要删除的商城用户ID
     * @return 结果
     */
    @Override
    public int deleteCertificationByIds(Long[] ids) {
        return certificationMapper.deleteCertificationByIds(ids);
    }

    /**
     * 删除商城用户信息
     *
     * @param id 商城用户ID
     * @return 结果
     */
    @Override
    public int deleteCertificationById(Long id) {
        return certificationMapper.deleteCertificationById(id);
    }

}