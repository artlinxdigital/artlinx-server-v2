package com.nft.mall.api.controller.pay.boppay;

import cn.hutool.core.util.ObjectUtil;
import com.alipay.api.AlipayApiException;
import com.google.common.collect.Maps;
import com.nft.common.constant.HttpStatus;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.collect.CollectionBuyParam;
import com.nft.common.core.redis.RedisCache;
import com.nft.common.enums.CollectStatus;
import com.nft.common.enums.CollectTradeStatus;
import com.nft.config.RedisKeyExpirationListener;
import com.nft.mall.domain.*;
import com.nft.mall.mapper.*;
import com.nft.mall.service.ICollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/mall/bopPay")
public class BopPayController {

    @Autowired
    private RedisCache redisCache;

    @Value("${pay.bop.notifyurl}")
    private String notify_url;

    @Autowired
    private CertificationMapper certificationMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTokenMapper productTokenMapper;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private ProductTradeMapper productTradeMapper;

    @Autowired
    private MoneyHistoryMapper moneyHistoryMapper;

    @PostMapping("/toPay")
    public AjaxResult toPay(@RequestBody ToPayOrderPojo toPayOrderPojo) {

        // 用户ID
        Long certificationId = toPayOrderPojo.getCertificationId();
        // 查询买家用户信息
        Certification buyer = certificationMapper.selectCertificationById(certificationId);
        if (ObjectUtil.isNull(buyer)) {
            return AjaxResult.error("用户不存在");
        }

        // 藏品ID
        Long productId = toPayOrderPojo.getProductId();
        // 查询藏品信息
        Product product = productMapper.selectProductById(productId);
        if (ObjectUtil.isNull(product)) {
            return AjaxResult.error("藏品不存在");
        }
//        // 查询藏品状态
//        if (!CollectStatus.validStatus(product.getStatus())) {
//            CollectStatus statusEnum = CollectStatus.getByCode(product.getStatus());
//            return AjaxResult.error("藏品"+statusEnum.getMessage()+",不能操作" );
//        }
        if (product.getStatus().intValue() != CollectStatus.ONLINE.getCode()) {
            return AjaxResult.error("此藏品不能下单");
        }

        // 查询藏品拥有者信息
        Long ownerId = product.getCreateId();
        Certification owner = certificationMapper.selectCertificationById(ownerId);
        if (ObjectUtil.isNull(owner)) {
            return AjaxResult.error("藏品不存在");
        }
//        if (certificationId.equals(ownerId)) {
//            return AjaxResult.error("不能下单自己的藏品");
//        }
        // 查询藏品token信息
        List<ProductToken> tokenList = productTokenMapper.selectByProductId(productId);
        if (ObjectUtil.isNull(tokenList)) {
            return AjaxResult.error("藏品不存在");
        }

//        if (product.getPrice().compareTo(new BigDecimal("0.01")) == -1) {
//            return AjaxResult.error("非法金额");
//        }

        // 订单金额
        BigDecimal tradefee = product.getPrice().multiply(product.getServiceRate()).divide(new BigDecimal(100), 2);

        CollectionBuyParam collectionBuyParam = new CollectionBuyParam();
        collectionBuyParam.setCertificationId(certificationId);
        collectionBuyParam.setFee(tradefee);
        collectionBuyParam.setServiceRate(product.getServiceRate());
        collectionBuyParam.setProductId(productId);
        try {
            AjaxResult ajaxResult1 = collectionService.buyForNewPayOrder(collectionBuyParam);
            if ((int) ajaxResult1.get(AjaxResult.CODE_TAG) == HttpStatus.SUCCESS) {
                redisCache.setCacheObject(RedisKeyExpirationListener.EX_PAY + ajaxResult1.get("orderoId"), ajaxResult1.get("qrcode"), 90, TimeUnit.MINUTES);
                return ajaxResult1;
            }
        } catch (Exception e) {
            return AjaxResult.error("支付错误");
        }
        return AjaxResult.error("支付错误");
    }

    /**
     * 获取订单支付状态
     *
     * @return
     */
    @GetMapping("/getTradePayStatus")
    public AjaxResult getTradePayStatus(@RequestParam Long tradeId, @RequestParam String tradeHash) {
        ProductTrade productTrade = productTradeMapper.selectProductTradeById(tradeId);
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payAmount", productTrade.getTradeAmount());

        Long orderId = tradeId;
        try {

            log.info("payFinish-orderId:" + orderId);
            log.info("payFinish-tradeHash:" + tradeHash);
            collectionService.buyForPayedOrder(orderId, tradeHash);
        } catch (Exception e) {
            log.error("支付成功接口--成功订单流程：", e);
            e.printStackTrace();
            //如果报错了，说明支付失败，可能是区块链调用错误，可能是数据库异常，也可能是邮件发送失败
            try {
                collectionService.cancelPay(orderId);
            } catch (Exception e1) {
                log.error("支付成功接口--退款订单流程：", e);
                e.printStackTrace();
            }
        }


        if (productTrade != null) {
            if (productTrade.getStatus().intValue() == CollectTradeStatus.WAIT_PAY.getCode()) {
                resultMap.put("payStatus", "paying");
                return AjaxResult.success("查询成功", resultMap);
            } else {
                MoneyHistory history_search = new MoneyHistory();
                history_search.setTradeId(productTrade.getProductTradeId());
                history_search.setCertificationId(productTrade.getCreateId());
                history_search.setType(1);
                List<MoneyHistory> moneyHistories = moneyHistoryMapper.selectChargeList(history_search);
                if (moneyHistories != null && moneyHistories.size() != 0) {
                    Integer status = moneyHistories.get(0).getStatus();

                    //1.进行中，2已完成 3.已取消
                    if (status == 1) {
                        resultMap.put("payStatus", "paying");
                        return AjaxResult.success("查询成功", resultMap);
                    } else if (status == 2) {
                        resultMap.put("payStatus", "payed");
                        return AjaxResult.success("查询成功", resultMap);
                    } else if (status == 3) {
                        resultMap.put("payStatus", "cancelPay");
                        return AjaxResult.success("查询成功", resultMap);
                    }
                }
            }
        }
        return AjaxResult.error("查询失败");
    }

    /**
     * 支付完成后的回调
     *
     * @param request
     * @param httpResponse
     * @throws IOException
     */
    @PostMapping("/payFinish")
    public synchronized void payFinish(HttpServletRequest request, HttpServletResponse httpResponse) throws InterruptedException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = getParams(request);
        String orderId = params.get("out_trade_no");
        try {
            if (isSuccess(params)) {

                log.info("payFinish-orderId:" + orderId);
                // TODO 暂时用不上
//                collectionService.buyForPayedOrder(Long.parseLong(orderId));
            }
        } catch (Exception e) {
            log.error("支付成功接口--成功订单流程：", e);
            e.printStackTrace();
            //如果报错了，说明支付失败，可能是区块链调用错误，可能是数据库异常，也可能是邮件发送失败
            try {
                AjaxResult ajaxResult = collectionService.refund(orderId);
                if ((int) ajaxResult.get("code") == 200) {
                    collectionService.cancelPay(Long.parseLong(orderId));
                } else {
                    log.error("支付成功接口--退款接口调用失败", ajaxResult.get("data"));
                }

            } catch (Exception e1) {
                log.error("支付成功接口--退款订单流程：", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 校验支付是否成功
     *
     * @param params http请求
     * @return 成功即为真
     * @throws AlipayApiException
     */
    private boolean isSuccess(final Map<String, String> params) throws AlipayApiException {
        // 调用SDK验证签名

        // 交易状态
        String trade_status = params.get("trade_state");
        log.info("trade_status:" + trade_status);
        if ("SUCCESS".equals(trade_status)) {
            return true;
        }
        return false;
    }

    /**
     * 获取参数
     */
    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            @SuppressWarnings("cast")
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

}
