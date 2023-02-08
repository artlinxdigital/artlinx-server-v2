package com.nft.common.enums;

/**
 * 提现申请状态枚举类
 *
 * @author nft
 */
public enum WithdrawStatus {

    ING(0, "审核中"),
    YES(1, "已出账"),
    NO(2, "出账失败"),
    REFUSE(3, "已拒绝"),;

    private final Integer code;
    private final String message;

    WithdrawStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static WithdrawStatus getByCode(int code) {
        for (WithdrawStatus anEnum : WithdrawStatus.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return message;
    }

}
