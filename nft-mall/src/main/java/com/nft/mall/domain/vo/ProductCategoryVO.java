package com.nft.mall.domain.vo;

import lombok.Data;

/**
 * 藏品分类前端展示对象
 *
 * @author nft
 * @date 2021-05-17
 */
@Data
public class ProductCategoryVO {

    /** 主键ID */
    private String productCategoryId;

    /** 分类名称 */
    private String categoryName;

}
