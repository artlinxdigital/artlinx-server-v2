package com.nft.common.enums;

/**
 * 代币类型枚举类
 *
 * @author nft
 */
public enum TokenType {

    ERC721(1, "ERC721"),
    ERC1155(2, "ERC1155"),;

    private final Integer code;
    private final String message;

    TokenType(Integer code, String message) {
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
