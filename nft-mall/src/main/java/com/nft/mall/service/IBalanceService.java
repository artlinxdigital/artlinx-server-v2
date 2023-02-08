package com.nft.mall.service;

import com.nft.common.core.domain.AjaxResult;
import com.nft.mall.domain.Balance;

import java.math.BigDecimal;

/**
 * 余额Service接口
 *
 * @author nft
 * @date 2021-10-20
 */
public interface IBalanceService {

    public Balance getBalanceIfNullCreate(Long userId);

    /**
     * @param userId
     * @param changeMoney
     * @param type        1增加 2减掉
     * @return
     */
    public void updateBalance(Long userId, BigDecimal changeMoney, Integer type);

    /**
     * 管理员给用户手工提现
     *
     * @param userId
     * @param money
     * @return 结果
     */
    public AjaxResult withDrawFromAdmin(Long userId, BigDecimal money);

}
