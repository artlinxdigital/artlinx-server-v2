package com.nft.common.enums;

/**
 * 收藏夹状态枚举类
 *
 * @author nft
 */
public enum CollectDirStatus {

    ING(0, "创建中"),
    YES(1, "创建成功"),
    NO(2, "创建失败");

    private final Integer code;
    private final String message;

    CollectDirStatus(Integer code, String message) {
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
