package com.nft.mall.domain.vo;

import lombok.Data;

/**
 * 轮播图前端展示对象
 *
 * @author nft
 * @date 2021-05-17
 */
@Data
public class BannerVO {

    /**
     * 主键ID
     */
    private Long bannerId;

    /**
     * 轮播图标题
     */
    private String bannerTitle;

    /**
     * 轮播图路径
     */
    private String imageUrl;

    /**
     * 轮播图跳转超链接
     */
    private String hyperLink;

}
