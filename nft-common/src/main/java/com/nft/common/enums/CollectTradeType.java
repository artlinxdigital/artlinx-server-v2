package com.nft.common.enums;

/**
 * 藏品交易类型枚举类
 * @author nft
 */
public enum CollectTradeType {

    BUY(1, "Buy"),
    SELL(2, "Sell"),;

    private int code;
    private String message;

    CollectTradeType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CollectTradeType getByCode(int code) {
        for (CollectTradeType anEnum : CollectTradeType.values()) {
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
