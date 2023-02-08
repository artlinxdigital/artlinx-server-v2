package com.nft.mall.domain;

import java.util.Date;

/**
* 藏品询价记录表
*/
public class TInquiryRecord {
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

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    public Long getInquiryRecordId() {
        return inquiryRecordId;
    }

    public void setInquiryRecordId(Long inquiryRecordId) {
        this.inquiryRecordId = inquiryRecordId;
    }

    public Long getInquiryCertificationId() {
        return inquiryCertificationId;
    }

    public void setInquiryCertificationId(Long inquiryCertificationId) {
        this.inquiryCertificationId = inquiryCertificationId;
    }

    public String getInquiryAccount() {
        return inquiryAccount;
    }

    public void setInquiryAccount(String inquiryAccount) {
        this.inquiryAccount = inquiryAccount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}