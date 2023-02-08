package com.nft.common.enums;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * 允许藏品发布类型枚举类
 *
 * @author nft
 */
public enum ReleaseStatus {

    YES(0, "允许"),
    NO(1, "禁止");

    private final Integer code;
    private final String message;

    ReleaseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static boolean checkRelease(Integer code) {
        Set<Integer> allowSet = Sets.newHashSet(YES.code);
        return allowSet.contains(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return message;
    }
}
