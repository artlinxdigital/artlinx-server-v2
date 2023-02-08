package com.nft.common.enums;

/**
 * 原石状态枚举类
 *
 * @author nft
 */
public enum RoughStatus {

    ING(0, "In Transaction"),
    OK(1, "Successful Transaction"),
    FAIL(2, "交易失败"),;

    private final Integer code;
    private final String message;

    RoughStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return message;
    }

}
