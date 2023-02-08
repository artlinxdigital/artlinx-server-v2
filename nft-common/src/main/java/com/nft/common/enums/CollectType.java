package com.nft.common.enums;

/**
 * 藏品类型枚举类
 * @author nft
 */
public enum CollectType {

    NORMAL(1, "普通文件"),
    AUDIO(2, "音频"),
    VIDEO(3, "视频"),;

    private int code;
    private String message;

    CollectType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CollectType getByCode(int code) {
        for (CollectType anEnum : CollectType.values()) {
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
