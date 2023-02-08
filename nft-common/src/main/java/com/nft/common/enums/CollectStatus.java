package com.nft.common.enums;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 藏品状态枚举类
 * @author nft
 */
public enum CollectStatus {

    ONLINE(0, "Normal"),
    TRADING(1, "In Transaction"),
    TRADED(2, "Successful Transaction"),
    OFFLINE(3, "Has Been Removed"),
    RELAUNCH(4, "Relist"),
    FORGETOKENDOWN(5, "Forge Token Down"),;

    private int code;
    private String message;

    CollectStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CollectStatus getByCode(int code) {
        for (CollectStatus anEnum : CollectStatus.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    public static Set<Integer> onLineSet() {
        return Sets.newHashSet(ONLINE.code, OFFLINE.code);
    }

    public static Set<Integer> offLineSet() {
        return Sets.newHashSet(TRADING.code, TRADED.code);
    }

    public static boolean validStatus(int code) {
        Set<Integer> validSet = Sets.newHashSet(ONLINE.code, TRADING.code);
        return validSet.contains(code);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
