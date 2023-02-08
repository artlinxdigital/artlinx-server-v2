package com.nft.common.enums;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 藏品操作类型枚举类
 *
 * @author nft
 */
public enum CollectOpeType {

    FAVORITE(1, "点赞"),
    COLLECT(2, "收藏"),
    CREATE(3, "创建"),
    IMPORT(4, "导入"),
    BUY(5, "Buy"),
    SELL(6, "Sell"),
    ONLINE(7, "上架"),
    ;

    private int code;
    private String message;

    CollectOpeType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CollectOpeType getByCode(int code) {
        for (CollectOpeType anEnum : CollectOpeType.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    public static Set<Integer> opeTypeSet() {
        return Sets.newHashSet(FAVORITE.code, COLLECT.code);
    }

    public static Set<Integer> collectTypeSet() {
        return Sets.newHashSet(CREATE.code, IMPORT.code, BUY.code);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
