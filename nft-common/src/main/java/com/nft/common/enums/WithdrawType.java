package com.nft.common.enums;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 提现申请类型枚举类
 *
 * @author nft
 */
public enum WithdrawType {

    WX(1, "微信"),

    ALI(2, "支付宝"),

    BANK(3, "银行卡"),

    BUY(4, "购买"),

    FAVORITE(5, "收藏夹"),

    COLLECTION(6, "铸造"),

    ONLINE(7, "上架"),

    FEE(8, "手续费"),

    IMPORT(9, "导入"),
    ;

    private int code;
    private String message;

    WithdrawType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static WithdrawType getByCode(int code) {
        for (WithdrawType anEnum : WithdrawType.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getTypeMessage(int code, int type) {
        Set<Integer> withDrawSet = Sets.newHashSet(WX.code, ALI.code, BANK.code);
        Set<Integer> feeSet = Sets.newHashSet(FAVORITE.code, COLLECTION.code);
        for (WithdrawType anEnum : WithdrawType.values()) {
            if (anEnum.code == code) {
                if (withDrawSet.contains(code)) {
                    return type == 0 ? anEnum.message : "提现";
                }
                if (feeSet.contains(code)) {
                    return type == 1 ? anEnum.message : "手续费";
                }
                return anEnum.message;
            }
        }
        return "";
    }

    /**
     * 提现类型列表
     */
    public static Set<Integer> getBillTypeList() {
        return Sets.newHashSet(WX.code, ALI.code, BANK.code);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
