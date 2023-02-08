package com.nft.mall.domain.vo;

import lombok.Data;

/**
 * 藏品作者前端展示对象
 *
 * @author nft
 * @date 2021-05-17
 */
@Data
public class ProductAuthorVO {

    /** 藏品作者 */
    private String author;

    /** 藏品作者简介 */
    private String authorDesc;

}
