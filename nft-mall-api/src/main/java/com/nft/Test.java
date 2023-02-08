package com.nft;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.nft.common.utils.HttpClient;
import com.nft.common.utils.sign.Md5Utils;
import com.nft.mall.domain.vo.RefundVO;
import com.nft.mall.domain.vo.ToPayPojo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
//        System.out.println(signToParams("2","-100001"));

//        signToRefund("83");

//        BigDecimal price=new BigDecimal("245");
//        BigDecimal serviceRate=new BigDecimal("0.1");
//        BigDecimal serviceAmount = (price.multiply((serviceRate).divide(new BigDecimal(100)))).setScale(2,BigDecimal.ROUND_HALF_UP);
//        System.out.println(serviceAmount.toPlainString());
        Map<String, Long> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("1", 5L);
        stringIntegerMap.put("1", 6L);
        stringIntegerMap.put("2", 6L);
        System.out.println(JSONObject.toJSONString(stringIntegerMap));
        System.out.println(stringIntegerMap.values());
    }

    static String notify_url = "http://baidu.com";
    static String back_url = "http://baidu.com?id=";

    public static String signToParams(String money, String orderId) {
        BigDecimal bigDecimal = new BigDecimal(money);
        String h5BackUrl = back_url + orderId;
        BigDecimal total_for_pay = bigDecimal.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN);
        String params = "appid=1022401&h5_redirect_url=" + h5BackUrl + "&notify_url=" + notify_url + "&out_trade_no=" + orderId.trim() + "&payment=unionpay.link" + "&total_fee=" + total_for_pay.toString() + "&wallet=CN&key=7Fcmxbamf2Mgs5wkvPnWxiChDRgOqbTe";
        String sign = Md5Utils.hash(params).toUpperCase();

        ToPayPojo toPayPojo = new ToPayPojo(total_for_pay.toString(), notify_url, sign, orderId.trim(), h5BackUrl);

        Gson gson = new Gson();
        String toJson = gson.toJson(toPayPojo);
        return toJson;
    }

    public static void signToRefund(String out_trade_no) {

        //除了key参数，其他的参数按首字母的ascii码顺序排列，最后再添加key的参数
        String params = "appid=1022401&out_trade_no=" + out_trade_no + "&key=7Fcmxbamf2Mgs5wkvPnWxiChDRgOqbTe";
        System.out.println("退款--params：" + params);
        String sign = Md5Utils.hash(params).toUpperCase();

        RefundVO refundVO = new RefundVO(sign, out_trade_no);
        Gson gson = new Gson();
        String toJson = gson.toJson(refundVO);
        System.out.println("退款--toJson：" + toJson);

        String result_str = HttpClient.doPostJson(toJson, null, "https://api.hk.blueoceanpay.com/order/reverse");
        System.out.println("支付结果：" + result_str);
        JSONObject jsonObject = JSONObject.parseObject(result_str);
        System.out.println(jsonObject.toJSONString());
    }
}
