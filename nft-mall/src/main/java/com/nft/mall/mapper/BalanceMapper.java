package com.nft.mall.mapper;

import com.nft.mall.domain.Balance;

import java.util.List;

public interface BalanceMapper {

    List<Balance> selectBalanceList(Balance balance);

    Balance selectBalanceByUserId(Long userId);

    public int insertBalance(Balance balance);

    public int updateBalance(Balance balance);
}
