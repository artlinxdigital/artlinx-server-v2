package com.nft.common.enums;

/**
 * 藏品来源类型枚举类
 * @author nft
 */
public enum CollectFrom {

    CASTING(1, "铸造"),
    BUY(2, "Buy"),
    IMPORT(3, "导入"),;

    private int code;
    private String message;

    CollectFrom(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CollectFrom getByCode(int code) {
        for (CollectFrom anEnum : CollectFrom.values()) {
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
