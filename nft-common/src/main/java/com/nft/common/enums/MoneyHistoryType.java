package com.nft.common.enums;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 历史交易类型枚举类
 * @author nft
 */
public enum MoneyHistoryType {

    BUY_PAY(1, "Buy-in"),

    SOLD_INCOME(2, "Sales income"),

    SOLD_FEE(3, "Selling fee"),

    CASH(4,"Settlement");;

    private int code;
    private String message;

    MoneyHistoryType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static MoneyHistoryType getByCode(int code) {
        for (MoneyHistoryType anEnum : MoneyHistoryType.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getTradeType(int code) {
        Set<Integer> outSet = Sets.newHashSet(BUY_PAY.code, SOLD_FEE.code);
        return outSet.contains(code) ? "Out" : "In";
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
