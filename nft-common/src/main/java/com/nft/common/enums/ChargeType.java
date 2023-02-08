package com.nft.common.enums;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 充值类型枚举类
 * @author nft
 */
public enum ChargeType {

    WX(1, "微信"),

    ALI(2, "支付宝"),

    SALE(3, "交易"),

    INCOME(4, "提成"),

    COPYRIGHT(5, "版税"),;

    private int code;
    private String message;

    ChargeType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ChargeType getByCode(int code) {
        for (ChargeType anEnum : ChargeType.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getTypeMessage(int code) {
        Set<Integer> chargeSet = Sets.newHashSet(WX.code, ALI.code);
        for (ChargeType anEnum : ChargeType.values()) {
            if (anEnum.code == code) {
                return chargeSet.contains(code) ? "充值" : anEnum.message;
            }
        }
        return "";
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
