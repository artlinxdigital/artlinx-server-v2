package com.nft.common.enums;

/**
 * 业务来源枚举类
 * @author nft
 */
public enum BusinessFrom {

    CHARGE(1, "充值"),

    ORDER(2, "订单"),;

    private int code;
    private String message;

    BusinessFrom(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static BusinessFrom getByCode(int code) {
        for (BusinessFrom anEnum : BusinessFrom.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
