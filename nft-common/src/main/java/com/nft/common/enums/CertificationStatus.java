package com.nft.common.enums;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * 用户认证状态枚举类
 *
 * @author nft
 */
public enum CertificationStatus {

    NOT_REAL(0, "未实名"),

    SUCCESS(1, "已实名"),

    WAIT_CHECK(2, "待审核"),

    CHECK_REJECT(3, "已拒绝"),

    ;

    private int code;
    private String message;

    CertificationStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CertificationStatus getByCode(int code) {
        for (CertificationStatus anEnum : CertificationStatus.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 校验认证状态
     */
    public static boolean checkStatus(int status) {
        if (Objects.isNull(status)) {
            return false;
        }
        Set<Integer> statusSet = Sets.newHashSet(SUCCESS.code);
        return statusSet.contains(status);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
