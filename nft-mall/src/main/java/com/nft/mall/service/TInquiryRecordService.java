/**
* @mbg.generated
* generator on Fri Mar 04 17:01:47 CST 2022
*/
package com.nft.mall.service;

import com.nft.mall.domain.TInquiryRecord;

public interface TInquiryRecordService {
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