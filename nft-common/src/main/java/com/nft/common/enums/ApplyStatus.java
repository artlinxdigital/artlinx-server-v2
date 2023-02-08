package com.nft.common.enums;

/**
 * 藏品发布申请枚举类
 *
 * @author nft
 */
public enum ApplyStatus {

    WAIT(-1, "待申请"),
    ING(0, "申请中"),
    YES(1, "申请成功"),
    NO(2, "申请失败");

    private final Integer code;
    private final String message;

    ApplyStatus(Integer code, String message) {
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
