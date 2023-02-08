package com.nft.common.enums;

/**
 * 原石激励来源枚举类
 *
 * @author nft
 */
public enum RoughRewardFrom {

    RECOMMEND(1, "推荐"),
    TRADE(2, "交易");

    private final Integer code;
    private final String message;

    RoughRewardFrom(Integer code, String message) {
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
