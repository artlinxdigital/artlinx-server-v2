package com.nft.system.service.impl;

import java.util.List;

import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.system.mapper.SysBannerMapper;
import com.nft.system.domain.SysBanner;
import com.nft.system.service.ISysBannerService;

/**
 * 轮播图Service业务层处理
 *
 * @author nft
 * @date 2021-10-20
 */
@Service
public class SysBannerServiceImpl implements ISysBannerService {

    @Autowired
    private SysBannerMapper sysBannerMapper;

    /**
     * 查询轮播图
     *
     * @param bannerId 轮播图ID
     * @return 轮播图
     */
    @Override
    public SysBanner selectSysBannerById(Long bannerId) {
        return sysBannerMapper.selectSysBannerById(bannerId);
    }

    /**
     * 查询轮播图列表
     *
     * @param sysBanner 轮播图
     * @return 轮播图
     */
    @Override
    public List<SysBanner> selectSysBannerList(SysBanner sysBanner) {
        return sysBannerMapper.selectSysBannerList(sysBanner);
    }

    /**
     * 新增轮播图
     *
     * @param sysBanner 轮播图
     * @return 结果
     */
    @Override
    public int insertSysBanner(SysBanner sysBanner) {
        sysBanner.setCreateTime(DateUtils.getNowDate());
        return sysBannerMapper.insertSysBanner(sysBanner);
    }

    /**
     * 修改轮播图
     *
     * @param sysBanner 轮播图
     * @return 结果
     */
    @Override
    public int updateSysBanner(SysBanner sysBanner) {
        sysBanner.setUpdateTime(DateUtils.getNowDate());
        return sysBannerMapper.updateSysBanner(sysBanner);
    }

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的轮播图ID
     * @return 结果
     */
    @Override
    public int deleteSysBannerByIds(Long[] bannerIds) {
        return sysBannerMapper.deleteSysBannerByIds(bannerIds);
    }

    /**
     * 删除轮播图信息
     *
     * @param bannerId 轮播图ID
     * @return 结果
     */
    @Override
    public int deleteSysBannerById(Long bannerId) {
        return sysBannerMapper.deleteSysBannerById(bannerId);
    }

}
