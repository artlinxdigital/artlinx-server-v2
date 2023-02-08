package com.nft.common.tencent.pay;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 微信支付工具类
 *
 * @author nft
 * @date 2021-05-14
 */
public class WeixinPayUtil {

    public static final String SUBMIT_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static DefaultHttpClient httpclient;

    static {
        httpclient = new DefaultHttpClient();
        httpclient = (DefaultHttpClient) HttpClientConnectionManager
                .getSSLInstance(httpclient);
    }

    public static String getPayNo(String url, String xmlParam) {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,
                true);
        HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
        String prepay_id = "";
        try {
            httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
            HttpResponse response = httpclient.execute(httpost);
            String jsonStr = EntityUtils
                    .toString(response.getEntity(), "UTF-8");
            System.out.println("prepay_id:" + jsonStr);
            Map<String, Object> dataMap = new HashMap<String, Object>();

            if (jsonStr.indexOf("FAIL") != -1) {
                return prepay_id;
            }
            Map map = doXMLParse(jsonStr);

            String return_code = (String) map.get("return_code");
            prepay_id = (String) map.get("prepay_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prepay_id;
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static Map doXMLParse(String strxml) throws Exception {
        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }
            m.put(k, v);
        }
        // 关闭流
        in.close();
        return m;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * 获取统一订单提交返回字符串值
     *
     * @param url
     * @param xmlParam
     * @return
     */
    public static String getTradeOrder(String url, String xmlParam) {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        HttpPost httPost = HttpClientConnectionManager.getPostMethod(url);
        String jsonStr = "";
        try {
            httPost.setEntity(new StringEntity(xmlParam, "UTF-8"));
            HttpResponse response = httpclient.execute(httPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 生成二维码地址
     *
     * @param weixinInfoDTO
     * @return
     */
    public static String generateCodeUrl(WeixinInfoDTO weixinInfoDTO) {
        String codeUrl = "";
        String submitXml = buildWeixinXml(weixinInfoDTO);
        String resultStr = getTradeOrder(SUBMIT_URL, submitXml);
        if (StringUtils.isNotBlank(resultStr)) {
            try {
                Map map = doXMLParse(resultStr);
                codeUrl = (String) map.get("code_url");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return codeUrl;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(SortedMap<String, String> packageParams) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=pxw20150801xrlaty15m01d01foralla");
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;

    }

    public static String getSign(Map<String, String> paramMap, String key) {
        List list = new ArrayList(paramMap.keySet());
        Object[] ary = list.toArray();
        Arrays.sort(ary);
        list = Arrays.asList(ary);
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str += list.get(i) + "=" + paramMap.get(list.get(i) + "") + "&";
        }
        str += "key=" + key;
        str = MD5Util.MD5Encode(str, "UTF-8").toUpperCase();

        return str;
    }

    /**
     * 创建提交统一订单的xml
     *
     * @return
     */
    public static String buildWeixinXml(WeixinInfoDTO weixinInfoDTO) {
        String xml = "<xml>" + "<appid>" + weixinInfoDTO.getAppid() + "</appid>"
                + "<body>" + weixinInfoDTO.getBody() + "</body>" + "<mch_id>" + weixinInfoDTO.getMch_id() + "</mch_id>"
                + "<nonce_str>" + weixinInfoDTO.getNonce_str() + "</nonce_str>"
                + "<notify_url>" + weixinInfoDTO.getNotify_url() + "</notify_url>"
                + "<out_trade_no>" + weixinInfoDTO.getOut_trade_no() + "</out_trade_no>"
                + "<spbill_create_ip>" + weixinInfoDTO.getSpbill_create_ip() + "</spbill_create_ip>"
                + "<total_fee>" + weixinInfoDTO.getTotal_fee() + "</total_fee>"
                + "<trade_type>" + weixinInfoDTO.getTrade_type() + "</trade_type>"
                + "<sign>" + weixinInfoDTO.getSign() + "</sign>" + "</xml>";
        System.out.println(xml);
        return xml;
    }

    /**
     * 处理xml请求信息
     *
     * @param request
     * @return
     */
    public static String getXmlRequest(HttpServletRequest request) {
        java.io.BufferedReader bis = null;
        String result = "";
        try {
            bis = new java.io.BufferedReader(new java.io.InputStreamReader(request.getInputStream()));
            String line = null;
            while ((line = bis.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 查询订单
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    public static Map checkWxOrderPay(String orderId) throws Exception {
        String nonce_str = UUID.randomUUID().toString().replaceAll("-", "");
        Boolean payFlag = false;
        String sign = "";
        SortedMap<String, String> storeMap = new TreeMap<String, String>();
        // 商户 后台的贸易单号
        storeMap.put("out_trade_no", orderId);
        // appid
        storeMap.put("appid", "wx5e45586116813f60");
        // 商户号
        storeMap.put("mch_id", "1251135401");
        // 随机数
        storeMap.put("nonce_str", nonce_str);
        sign = createSign(storeMap);

        String xml = "<xml><appid>wx5e45586116813f60</appid><mch_id>1251135401</mch_id>" +
                "<nonce_str>" + nonce_str + "</nonce_str>" +
                "<out_trade_no>" + orderId + "</out_trade_no>" +
                "<sign>" + sign + "</sign></xml>";
        String resultMsg = getTradeOrder("https://api.mch.weixin.qq.com/pay/orderquery", xml);
        System.out.println("orderquery,result:" + resultMsg);
        if (StringUtils.isNotBlank(resultMsg)) {
            Map resultMap = WeixinPayUtil.doXMLParse(resultMsg);
            if (resultMap != null && resultMap.size() > 0) {
                return resultMap;
            }
        }
        return null;
    }

}
