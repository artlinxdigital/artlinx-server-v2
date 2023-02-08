package com.nft.mall.mapper;

import com.nft.mall.domain.TInquiryRecord;

public interface TInquiryRecordMapper {
    /**
    * deleteByPrimaryKey
    * @param inquiryRecordId inquiryRecordId
    * @return int int
    */
    int deleteByPrimaryKey(Long inquiryRecordId);

    /**
    * insert
    * @param record record
    * @return int int
    */
    int insert(TInquiryRecord record);

    /**
    * insertSelective
    * @param record record
    * @return int int
    */
    int insertSelective(TInquiryRecord record);

    /**
    * selectByPrimaryKey
    * @param inquiryRecordId inquiryRecordId
    * @return TInquiryRecord TInquiryRecord
    */
    TInquiryRecord selectByPrimaryKey(Long inquiryRecordId);

    /**
    * updateByPrimaryKeySelective
    * @param record record
    * @return int int
    */
    int updateByPrimaryKeySelective(TInquiryRecord record);

    /**
    * updateByPrimaryKey
    * @param record record
    * @return int int
    */
    int updateByPrimaryKey(TInquiryRecord record);
}