package com.nft.common.tencent.pay;

import lombok.Data;

/**
 * 微信相关信息DTO
 *
 * @author nft
 * @date 2021-05-14
 */
@Data
public class WeixinInfoDTO {

    private String appid;

    private String body;

    private String mch_id;

    private String nonce_str;

    private String notify_url;

    private String out_trade_no;

    private String spbill_create_ip;

    private String trade_type;

    private Integer total_fee;

    private String sign;

}
