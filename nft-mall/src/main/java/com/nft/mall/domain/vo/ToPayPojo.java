package com.nft.mall.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 传给bop银联支付的参数
 */
@Data
public class ToPayPojo implements Serializable {

    private String appid;

    private String payment;

    private String out_trade_no;

    private String total_fee;

    private String wallet;

    private String notify_url;

    private String sign;

    private String h5_redirect_url;
    public  ToPayPojo()
    {

    }

    public ToPayPojo(String total_fee,String notify_url,String sign,String out_trade_no,String h5_redirect_url){
        this.appid="1022401";
        this.total_fee=total_fee;
        this.notify_url=notify_url;
        this.sign=sign;
        this.payment="unionpay.link";
        this.wallet="CN";
        this.out_trade_no=out_trade_no;
        this.h5_redirect_url=h5_redirect_url;
    }
}
