package com.nft.mall.domain;

import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 轮播图对象
 *
 * @author nft
 * @date 2021-10-20
 */
@Data
public class Banner extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long bannerId;

    /**
     * 轮播图标题
     */
    @Excel(name = "轮播图标题")
    private String bannerTitle;

    /**
     * 轮播图路径
     */
    @Excel(name = "轮播图路径")
    private String imageUrl;

    /**
     * 轮播图跳转超链接
     */
    @Excel(name = "轮播图跳转超链接")
    private String hyperLink;

    /**
     * 轮播图状态  0.有效 1.不可用
     */
    private Integer status;

    /**
     * 轮播图录入人员
     */
    private Long createId;

    /**
     * 轮播图修改人员
     */
    private Long updateId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("bannerId", getBannerId())
                .append("bannerTitle", getBannerTitle())
                .append("imageUrl", getImageUrl())
                .append("hyperLink", getHyperLink())
                .append("status", getStatus())
                .append("createId", getCreateId())
                .append("updateId", getUpdateId())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
