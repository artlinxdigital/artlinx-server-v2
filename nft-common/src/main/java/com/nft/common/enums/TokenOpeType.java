package com.nft.common.enums;

/**
 * Token操作类型枚举类
 * @author nft
 */
public enum TokenOpeType {

    MOLD(1, "铸造"),
    INVOKE(2, "上架"),
    REVOKE(3, "下架"),
    TRADE(4, "交易"),
    IMPORT(5, "导入"),;

    private int code;
    private String message;

    TokenOpeType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static TokenOpeType getByCode(int code) {
        for (TokenOpeType anEnum : TokenOpeType.values()) {
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
