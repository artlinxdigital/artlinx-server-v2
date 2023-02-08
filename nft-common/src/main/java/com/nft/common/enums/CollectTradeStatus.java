package com.nft.common.enums;

/**
 * 藏品交易状态枚举类
 * @author nft
 */
public enum CollectTradeStatus {

    WAIT_PAY(0, "待支付"),
    TRADING(1, "In Transaction"),
    SUCCESS(2, "Successful Transaction"),
    FAIL(3, "交易失败"),;

    private int code;
    private String message;

    CollectTradeStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CollectTradeStatus getByCode(int code) {
        for (CollectTradeStatus anEnum : CollectTradeStatus.values()) {
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
