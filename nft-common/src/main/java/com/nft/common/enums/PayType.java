package com.nft.common.enums;

/**
 * 支付类型枚举类
 * @author nft
 */
public enum PayType {

    WE_CHAT(1, "微信"),

    ALI_PAY(2, "支付宝"),

    BALANCE(3, "余额"),
    BOP(4,"BOP银联支付");;

    private int code;
    private String message;

    PayType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static PayType getByCode(int code) {
        for (PayType anEnum : PayType.values()) {
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
