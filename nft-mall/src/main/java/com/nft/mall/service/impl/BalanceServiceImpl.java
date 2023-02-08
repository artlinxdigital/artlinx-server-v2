package com.nft.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.enums.MoneyHistoryStatus;
import com.nft.common.enums.MoneyHistoryType;
import com.nft.common.utils.uuid.IdUtils;
import com.nft.mall.domain.Balance;
import com.nft.mall.domain.Certification;
import com.nft.mall.domain.MoneyHistory;
import com.nft.mall.mapper.BalanceMapper;
import com.nft.mall.mapper.CertificationMapper;
import com.nft.mall.mapper.MoneyHistoryMapper;
import com.nft.mall.service.IBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 余额Service业务层处理
 *
 * @author nft
 * @date 2021-10-25
 */
@Slf4j
@Service
public class BalanceServiceImpl implements IBalanceService {

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private MoneyHistoryMapper moneyHistoryMapper;

    @Autowired
    private CertificationMapper certificationMapper;

    @Override
    public synchronized Balance getBalanceIfNullCreate(Long userId) {
        Balance balance = balanceMapper.selectBalanceByUserId(userId);
        if (balance == null) {
            balance = new Balance();
            balance.setBalance(BigDecimal.ZERO);
            balance.setCertificationId(userId);
            balance.setCreateTime(new Date());
            balance.setUpdateTime(new Date());
            balanceMapper.insertBalance(balance);
        }
        return balance;
    }

    @Override
    public synchronized void updateBalance(Long userId, BigDecimal changeMoney, @NotNull Integer type) {
        Balance balance = getBalanceIfNullCreate(userId);
        log.info("用户userId："+userId+",金额变化："+changeMoney.toPlainString()+"，变化类型："+type);
        log.info("钱包id:"+balance.getId());
        if (type == 1) {
            //增加余额
            balance.setBalance(balance.getBalance().add(changeMoney));
            balance.setUpdateTime(new Date());
            balanceMapper.updateBalance(balance);
        } else if (type == 2) {
            //减掉余额
            balance.setBalance(balance.getBalance().subtract(changeMoney));
            balance.setUpdateTime(new Date());
            balanceMapper.updateBalance(balance);
        }
    }

    @Override
    @Transactional
    public AjaxResult withDrawFromAdmin(@NotNull Long userId, @NotNull BigDecimal money) {
        Certification buyer = certificationMapper.selectCertificationById(userId);
        if (ObjectUtil.isNull(buyer)) {
            return AjaxResult.error("用户不存在");
        }
        Balance balance = getBalanceIfNullCreate(userId);
        if (balance.getBalance().compareTo(money) < 0) {
            return AjaxResult.error("余额不够提现");
        }

        MoneyHistory historyFee = new MoneyHistory();
        historyFee.setCertificationId(userId);
        historyFee.setMoney(money);
        historyFee.setId(IdUtils.getTradeNo());
        historyFee.setType(MoneyHistoryType.CASH.getCode());
        historyFee.setAddress(buyer.getWalletAddress());
        historyFee.setStatus(MoneyHistoryStatus.COMPLETE.getCode());
        historyFee.setTradeId(null);
        historyFee.setCreateTime(new Date());
        moneyHistoryMapper.insertCharge(historyFee);

        updateBalance(userId, money, 2);
        return AjaxResult.success();
    }
}
