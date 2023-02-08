/**
* @mbg.generated
* generator on Fri Mar 04 17:01:47 CST 2022
*/
package com.nft.mall.service.impl;

import com.nft.mall.domain.TInquiryRecord;
import com.nft.mall.mapper.TInquiryRecordMapper;
import com.nft.mall.service.TInquiryRecordService;
import org.springframework.stereotype.Service;

@Service
public class TInquiryRecordServiceImpl implements TInquiryRecordService {
    private final TInquiryRecordMapper tInquiryRecordMapper;

    public TInquiryRecordServiceImpl(TInquiryRecordMapper tInquiryRecordMapper) {
        this.tInquiryRecordMapper=tInquiryRecordMapper;
    }

    /**
    * deleteByPrimaryKey
    * @param inquiryRecordId inquiryRecordId
    * @return int int
    */
    @Override
    public int deleteByPrimaryKey(Long inquiryRecordId) {
        return tInquiryRecordMapper.deleteByPrimaryKey(inquiryRecordId);
    }

    /**
    * insert
    * @param record record
    * @return int int
    */
    @Override
    public int insert(TInquiryRecord record) {
        return tInquiryRecordMapper.insert(record);
    }

    /**
    * insertSelective
    * @param record record
    * @return int int
    */
    @Override
    public int insertSelective(TInquiryRecord record) {
        return tInquiryRecordMapper.insertSelective(record);
    }

    /**
    * selectByPrimaryKey
    * @param inquiryRecordId inquiryRecordId
    * @return TInquiryRecord TInquiryRecord
    */
    @Override
    public TInquiryRecord selectByPrimaryKey(Long inquiryRecordId) {
        return tInquiryRecordMapper.selectByPrimaryKey(inquiryRecordId);
    }

    /**
    * updateByPrimaryKeySelective
    * @param record record
    * @return int int
    */
    @Override
    public int updateByPrimaryKeySelective(TInquiryRecord record) {
        return tInquiryRecordMapper.updateByPrimaryKeySelective(record);
    }

    /**
    * updateByPrimaryKey
    * @param record record
    * @return int int
    */
    @Override
    public int updateByPrimaryKey(TInquiryRecord record) {
        return tInquiryRecordMapper.updateByPrimaryKey(record);
    }
}