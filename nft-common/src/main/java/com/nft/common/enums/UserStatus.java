package com.nft.common.enums;

/**
 * 用户状态
 * 
 * @author nft
 */
public enum UserStatus
{
    OK("0", "Normal"), DISABLE("1", "停用"), DELETED("2", "删除");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
