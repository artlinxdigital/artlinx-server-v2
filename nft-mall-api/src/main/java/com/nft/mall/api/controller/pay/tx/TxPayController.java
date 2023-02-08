package com.nft.mall.api.controller.pay.tx;

import com.alibaba.fastjson.JSONObject;
import com.nft.common.config.TxConfig;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.collect.CollectionBuyParam;
import com.nft.common.enums.BusinessFrom;
import com.nft.common.enums.ChargeStatus;
import com.nft.common.enums.PayType;
import com.nft.common.tencent.XMLUtils;
import com.nft.common.tencent.pay.Json;
import com.nft.common.utils.DigitalUtils;
import com.nft.common.utils.ip.IpUtils;
import com.nft.common.utils.web3.Web3jUtils;
import com.nft.mall.domain.Charge;
import com.nft.mall.service.IChargeService;
import com.nft.mall.service.IThirdPartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 充值、支付Controller
 *
 * @author nft
 * @date 2021-05-13
 */
@Slf4j
@RestController
@RequestMapping("/mall/txPay")
public class TxPayController {

    @Autowired
    private IThirdPartyService thirdPartyService;

    @Autowired
    private IChargeService chargeService;

    @GetMapping("/toPay")
    public AjaxResult toPay(HttpServletRequest request, HttpServletResponse httpResponse) {
        // 支付IP
        String ipAddr = IpUtils.getIpAddr(request);
        // 支付主题
        String subject = request.getParameter("subject");
        // 充值的地址
        String address = request.getParameter("address");
        // 支付金额
        BigDecimal getMoney = new BigDecimal(String.valueOf(request.getParameter("money")));
        String code = request.getParameter("code");
        // 0电脑支付
        int type = new Integer(request.getParameter("type"));

        String money = getMoney.toString();

        if (!address.toUpperCase().startsWith("0X") || address.length() != 42) {
            return AjaxResult.error("非法充值地址");
        }

        if (new BigDecimal(money).compareTo(new BigDecimal("0.01")) == -1) {
            return AjaxResult.error("非法充值金额");
        }

        try {
            // H5
            if (type == TxConfig.h5) {
                return thirdPartyService.wxH5(address, money, ipAddr, subject, getCollectionBuyParam(request, PayType.WE_CHAT.getCode()));
            }
            // PC
            if (type == TxConfig.pc) {
                return thirdPartyService.wxNative(address, money, ipAddr, subject, getCollectionBuyParam(request, PayType.WE_CHAT.getCode()));
            }
            // 微信公众号
            if(type == TxConfig.jsapi) {
                return thirdPartyService.wxJsapi(address, money, ipAddr, subject, code, request, httpResponse);
            }
            // APP
            if (type == TxConfig.app) {
                return thirdPartyService.wxApp(address, money, ipAddr, subject, request, httpResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("支付错误");
        }
        return AjaxResult.success("支付成功");
    }

    /**
     * 支付完成回调
     *
     * @param request
     * @param httpResponse
     * @throws IOException
     */
    @PostMapping("/weixinPayNotify")
    public void weixinPayNotify(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception {
        String line = "";
        BufferedReader reader = request.getReader();
        Map map = new HashMap();
        String xml = "<xml><return_code><![CDATA[FAIL]]></xml>";
        StringBuffer inputString = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        request.getReader().close();
        log.info("----接收到的报文---" + inputString.toString());
        if (inputString.toString().length() > 0) {
            map = XMLUtils.parseXmlToList(inputString.toString());
        } else {
            log.info("接受微信报文为空");
        }
        log.info("map=" + map);
        Charge charge = chargeService.selectChargeById(map.get("out_trade_no").toString());
        if (map != null && "SUCCESS".equals(map.get("result_code"))) {
            log.info("charge:" + JSONObject.toJSONString(charge));
            if (charge.getStatus() != ChargeStatus.ING.getCode()) {
                return;
            }
            // 链上充值
            BigDecimal money = charge.getMoney();
            money = DigitalUtils.clearNoUseZeroForBigDecimal(money);
            Json chargeJson = Web3jUtils.charge(money.multiply(BigDecimal.TEN.pow(18)).toBigInteger(), charge.getAddress());
            if (!chargeJson.isFlag()) {
                // 退款
                if (thirdPartyService.wxPayRefund(charge)) {
                    charge.setStatus(ChargeStatus.FAIL.getCode());
                    chargeService.updateCharge(charge);
                }
            } else {
                charge.setStatus(ChargeStatus.SUCCESS.getCode());
                chargeService.updateCharge(charge);
                xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            }
        }
        // 告诉微信端已经确认支付成功
        httpResponse.getWriter().write(xml);
    }

    /**
     * 获取参数
     *
     * @param request HttpServletRequest对象
     * @return 返回支付宝携带的参数
     */
    private CollectionBuyParam getCollectionBuyParam(HttpServletRequest request, Integer payType) {
        CollectionBuyParam param = new CollectionBuyParam();
        // 支付来源
        int payFrom = new Integer(request.getParameter("from"));
        if (payFrom == BusinessFrom.ORDER.getCode()) {
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
        }
        param.setPayFrom(payFrom);
        return param;
    }

}
