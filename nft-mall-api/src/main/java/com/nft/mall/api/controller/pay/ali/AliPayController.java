package com.nft.mall.api.controller.pay.ali;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.nft.common.ali.pay.AliConfig;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.collect.CollectionBuyParam;
import com.nft.common.core.redis.RedisCache;
import com.nft.common.enums.BusinessFrom;
import com.nft.common.enums.ChargeStatus;
import com.nft.common.enums.CollectStatus;
import com.nft.common.enums.PayType;
import com.nft.common.tencent.pay.Json;
import com.nft.common.utils.DigitalUtils;
import com.nft.common.utils.web3.Web3jUtils;
import com.nft.mall.domain.Certification;
import com.nft.mall.domain.Charge;
import com.nft.mall.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 充值、支付Controller
 *
 * @author nft
 * @date 2021-05-13
 */
@Slf4j
@RestController
@RequestMapping("/mall/aliPay")
public class AliPayController {

    @Autowired
    private IChargeService chargeService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IThirdPartyService thirdPartyService;

    @Autowired
    private IBuildModelService buildModelService;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private RedisCache redisCache;

    @GetMapping("/toPay")
    public AjaxResult toPay(HttpServletRequest request, HttpServletResponse httpResponse) {

        int type = new Integer(request.getParameter("type"));
        String address = request.getParameter("address");
        String money = request.getParameter("money");
        int from = new Integer(request.getParameter("from"));

        if (!address.toUpperCase().startsWith("0X") || address.length() != 42) {
            return AjaxResult.error("非法充值地址");
        }

        if (new BigDecimal(money).compareTo(new BigDecimal("0.01")) == -1) {
            return AjaxResult.error("非法充值金额");
        }

        Certification certification = userService.getByWalletAddress(address);
        if (ObjectUtil.isNull(certification)) {
            return AjaxResult.error("充值地址不存在");
        }

        // 添加充值记录
        Charge charge = buildModelService.buildCharge(certification, new BigDecimal(money), PayType.ALI_PAY.getCode(), ChargeStatus.ING.getCode());
        // 订单编号
        String orderId = charge.getId();
        int effectNum = chargeService.insertCharge(charge);
        if (effectNum != 1) {
            return AjaxResult.success("支付错误");
        }
        if (from == BusinessFrom.ORDER.getCode()) {
            CollectionBuyParam param = getCollectionBuyParam(request, PayType.ALI_PAY.getCode());
            // 更新藏品状态
//            collectionService.updateCollectionStatus(param.getProductId(), CollectStatus.TRADING.getCode());
            // 下单信息放入redis(有效期为24小时)
            redisCache.setCacheObject(orderId.trim(), param, 24, TimeUnit.HOURS);
        }
        String returnUrl = from == BusinessFrom.ORDER.getCode() ? AliConfig.return_url_order : AliConfig.return_url_charge;
        if (type == AliConfig.MOBILE_PAY_TYPE) {
            return thirdPartyService.aliMobilePay(returnUrl, orderId, money);
        }
        if (type == AliConfig.QR_CODE_PAY_TYPE) {
            return thirdPartyService.aliQrCodePay(returnUrl, orderId, money);
        }
        if (type == AliConfig.PC_PAY_TYPE) {
            return thirdPartyService.aliPcPay(returnUrl, orderId, money);
        }
        if (type == AliConfig.APP_PAY_TYPE) {
            return thirdPartyService.aliAppPay(returnUrl, orderId, money);
        }
        return AjaxResult.error("支付错误", orderId);
    }

    /**
     * 支付完成后的回调
     *
     * @param request
     * @param httpResponse
     * @throws IOException
     */
    @PostMapping("/payFinish")
    public void payFinish(HttpServletRequest request, HttpServletResponse httpResponse) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = getParams(request);
        try {
            if (!isSuccess(params)) {
                fail(httpResponse.getWriter());
                return;
            }
            String orderId = getOrderId(params);
            log.info("payFinish-orderId:" + orderId);
            Charge charge = chargeService.selectChargeById(orderId);
            if (charge.getStatus() != 0) {
                fail(httpResponse.getWriter());
                return;
            }
            log.info("charge:" + JSONObject.toJSONString(charge));
            // 链上充值
            BigDecimal money = charge.getMoney();
            money = DigitalUtils.clearNoUseZeroForBigDecimal(money);
            Json chargeJson = Web3jUtils.charge(money.multiply(BigDecimal.TEN.pow(18)).toBigInteger(), charge.getAddress());
            if (!chargeJson.isFlag()) {
                charge.setStatus(ChargeStatus.FAIL.getCode());
                chargeService.updateCharge(charge);
                AlipayClient alipayClient = AliConfig.getAppPay();
                // 创建API对应的request类
                AlipayTradeRefundRequest request1 = new AlipayTradeRefundRequest();
                // 设置业务参数
                request1.setBizContent("{" +
                        " \"out_trade_no\":\"" + orderId + "\"," +
                        " \"refund_amount\":\"" + charge.getMoney() + "\"" +
                        " }");
                // 通过alipayClient调用API，获得对应的response类
                AlipayTradeRefundResponse response = alipayClient.execute(request1);
                log.info(response.getBody());
                fail(httpResponse.getWriter());
                return;
            } else {
                charge.setStatus(ChargeStatus.SUCCESS.getCode());
                charge.setUpdateTime(new Date());
                chargeService.updateCharge(charge);
                success(httpResponse.getWriter());
            }
        } catch (Exception e) {
            log.error("error", e);
            e.printStackTrace();
        }
    }

    /**
     * 获取订单id
     *
     * @param params 请求参数
     * @return 订单id
     */
    private String getOrderId(Map<String, String> params) {
        String order_no = params.get("out_trade_no");
        return order_no;
    }

    /**
     * 获取总金额
     *
     * @param params 请求参数
     * @return 总金额
     */
    private String getTotalFee(Map<String, String> params) {
        String total_fee = params.get("total_amount");
        return total_fee;
    }

    /**
     * 校验支付宝支付是否成功
     *
     * @param params http请求
     * @return 成功即为真
     * @throws AlipayApiException
     */
    private boolean isSuccess(final Map<String, String> params) throws AlipayApiException {
        // 调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AliConfig.alipay_public_key, AliConfig.charset,
                AliConfig.sign_type);
        log.info("signVerified:" + signVerified);
        if (!signVerified) {
            return false;
        }
        // 交易状态
        String trade_status = params.get("trade_status");
        log.info("trade_status:" + trade_status);
        if (!trade_status.equals("TRADE_FINISHED") && !trade_status.equals("TRADE_SUCCESS")) {
            return false;
        }
        return true;
    }

    /**
     * 成功
     * <p>
     * TODO 重构方法名
     *
     * @param out 输出流
     */
    private void success(PrintWriter out) {
        log.info("pay success");
    }

    /**
     * 失败
     * <p>
     * TODO 重构方法名
     *
     * @param out 输出流
     */
    private void fail(PrintWriter out) {
        log.info("pay fail");
    }

    /**
     * 获取参数
     *
     * @param request HttpServletRequest对象
     * @return 返回支付宝携带的参数
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

    /**
     * 获取参数
     *
     * @param request HttpServletRequest对象
     * @return 返回支付宝携带的参数
     */
    private CollectionBuyParam getCollectionBuyParam(HttpServletRequest request, Integer payType) {
        CollectionBuyParam param = new CollectionBuyParam();
        String productId = request.getParameter("productId");
        String totalAmount = request.getParameter("totalAmount");
        String fee = request.getParameter("fee");
        String invitorAddress = request.getParameter("invitorAddress");
        String contractAddress = request.getParameter("contractAddress");
        param.setProductId(Long.valueOf(productId));
        param.setTotalAmount(new BigDecimal(totalAmount));
        param.setFee(new BigDecimal(fee));
        param.setInvitorAddress(invitorAddress);
        param.setContractAddress(contractAddress);
        param.setPayType(payType);
        return param;
    }

}
