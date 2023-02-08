package com.nft.mall.domain;

import com.nft.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 询价记录对象 t_inquiry_record
 *
 * @author nft
 * @date 2021-06-06
 */
@Data
public class InquiryRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long inquiryRecordId;

    /**
     * 询价用户ID
     */
    private Long inquiryCertificationId;

    /**
     * 询价用户账号
     */
    private String inquiryAccount;

    /**
     * 藏品ID
     */
    private Long productId;

    /**
     * 处理状态：0.未处理 1.已处理
     */
    private Integer dealStatus;

    /**
     * 记录状态：0.有效 1.失效
     */
    private Integer recordStatus;

}
