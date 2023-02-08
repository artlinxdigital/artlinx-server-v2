package com.nft.common.enums;

/**
 * 历史交易状态枚举类
 * @author nft
 */
public enum MoneyHistoryStatus {

    ING(1, "In progress"),

    COMPLETE(2, "Completed"),

    CANCEL(3, "Cancelled"),;;

    private int code;
    private String message;

    MoneyHistoryStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static MoneyHistoryStatus getByCode(int code) {
        for (MoneyHistoryStatus anEnum : MoneyHistoryStatus.values()) {
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
