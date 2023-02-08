package com.nft.mall.mapper;

import com.nft.mall.domain.Banner;

import java.util.List;

/**
 * 轮播图Mapper接口
 *
 * @author nft
 * @date 2021-10-20
 */
public interface BannerMapper {

    /**
     * 查询轮播图
     *
     * @param bannerId 轮播图ID
     * @return 轮播图
     */
    Banner selectBannerById(Long bannerId);

    /**
     * 查询轮播图列表
     *
     * @param banner 轮播图
     * @return 轮播图集合
     */
    List<Banner> selectBannerList(Banner banner);

    /**
     * 新增轮播图
     *
     * @param banner 轮播图
     * @return 结果
     */
     int insertBanner(Banner banner);

    /**
     * 修改轮播图
     *
     * @param banner 轮播图
     * @return 结果
     */
     int updateBanner(Banner banner);

    /**
     * 删除轮播图
     *
     * @param bannerId 轮播图ID
     * @return 结果
     */
     int deleteBannerById(Long bannerId);

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的数据ID
     * @return 结果
     */
     int deleteBannerByIds(Long[] bannerIds);

}
