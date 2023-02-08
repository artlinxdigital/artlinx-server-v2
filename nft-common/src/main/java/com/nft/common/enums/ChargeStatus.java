package com.nft.common.enums;

/**
 * 充值状态枚举类
 * @author nft
 */
public enum ChargeStatus {

    ING(0, "充值中"),

    SUCCESS(1, "充值成功"),

    FAIL(2, "充值失败"),;

    private int code;
    private String message;

    ChargeStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ChargeStatus getByCode(int code) {
        for (ChargeStatus anEnum : ChargeStatus.values()) {
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
