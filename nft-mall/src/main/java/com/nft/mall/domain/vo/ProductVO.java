package com.nft.mall.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 藏品前端展示对象
 *
 * @author nft
 * @date 2021-05-17
 */
@Data
public class ProductVO extends ProductBase {

    /** 同类别藏品列表 */
    private List<ProductBase> sameCategoryList;

    /** 同作者藏品列表 */
    private List<ProductBase> sameAuthorList;

}
