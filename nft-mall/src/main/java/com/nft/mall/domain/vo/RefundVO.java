package com.nft.mall.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 退款包装类
 */
@Data
public class RefundVO implements Serializable {
    private String appid;

    private String sign;

    private String out_trade_no;

    public RefundVO(String sign, String out_trade_no) {
        this.appid="1022401";
        this.sign = sign;
        this.out_trade_no = out_trade_no;
    }
}
