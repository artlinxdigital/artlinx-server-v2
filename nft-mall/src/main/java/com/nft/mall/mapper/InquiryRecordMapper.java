package com.nft.mall.mapper;

import com.nft.mall.domain.InquiryRecord;

/**
 * 询价记录Mapper接口
 *
 * @author nft
 * @date 2021-06-06
 */
public interface InquiryRecordMapper {

    /**
     * 删除用户询价记录
     *
     * @param inquiryRecordId 询价ID
     * @return 结果
     */
    int deleteByPrimaryKey(Long inquiryRecordId);

    /**
     * 插入用户询价记录
     *
     * @param record 询价记录
     * @return 结果
     */
    int insertInquiryRecord(InquiryRecord record);

    /**
     * 查询用户询价记录
     *
     * @param inquiryRecordId 询价ID
     * @return 结果
     */
    InquiryRecord selectByPrimaryKey(Long inquiryRecordId);

    int updateByPrimaryKey(InquiryRecord record);

}
