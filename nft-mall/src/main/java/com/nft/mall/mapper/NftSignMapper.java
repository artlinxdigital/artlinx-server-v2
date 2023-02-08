package com.nft.mall.mapper;

import com.nft.mall.domain.NftSign;
import com.nft.mall.domain.NftSignExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NftSignMapper {
    long countByExample(NftSignExample example);

    int deleteByExample(NftSignExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NftSign record);

    int insertSelective(NftSign record);

    List<NftSign> selectByExample(NftSignExample example);

    NftSign selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NftSign record, @Param("example") NftSignExample example);

    int updateByExample(@Param("record") NftSign record, @Param("example") NftSignExample example);

    int updateByPrimaryKeySelective(NftSign record);

    int updateByPrimaryKey(NftSign record);
}