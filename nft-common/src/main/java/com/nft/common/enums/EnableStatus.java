package com.nft.common.enums;

/**
 * 允许状态
 *
 * @author nft
 */
public enum EnableStatus {

    ENABLE(0, "Normal"),
    DISABLE(1, "停用");

    private final Integer code;
    private final String message;

    EnableStatus(Integer code, String message) {
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
