package com.nft.system.service;

import java.util.List;

import com.nft.system.domain.SysBanner;

/**
 * 轮播图Service接口
 *
 * @author nft
 * @date 2021-10-20
 */
public interface ISysBannerService {

    /**
     * 查询轮播图
     *
     * @param bannerId 轮播图ID
     * @return 轮播图
     */
    public SysBanner selectSysBannerById(Long bannerId);

    /**
     * 查询轮播图列表
     *
     * @param sysBanner 轮播图
     * @return 轮播图集合
     */
    public List<SysBanner> selectSysBannerList(SysBanner sysBanner);

    /**
     * 新增轮播图
     *
     * @param sysBanner 轮播图
     * @return 结果
     */
    public int insertSysBanner(SysBanner sysBanner);

    /**
     * 修改轮播图
     *
     * @param sysBanner 轮播图
     * @return 结果
     */
    public int updateSysBanner(SysBanner sysBanner);

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的轮播图ID
     * @return 结果
     */
    public int deleteSysBannerByIds(Long[] bannerIds);

    /**
     * 删除轮播图信息
     *
     * @param bannerId 轮播图ID
     * @return 结果
     */
    public int deleteSysBannerById(Long bannerId);

}
