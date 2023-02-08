package com.nft.common.enums;

/**
 * 认证用户类型枚举类
 * @author nft
 */
public enum CertificationType {

    PERSON(1, "个人"),

    ORG(2, "企业");

    private int code;
    private String message;

    CertificationType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CertificationType getByCode(int code) {
        for (CertificationType anEnum : CertificationType.values()) {
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
