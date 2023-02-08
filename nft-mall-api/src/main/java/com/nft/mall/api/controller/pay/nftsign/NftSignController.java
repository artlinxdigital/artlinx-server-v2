package com.nft.mall.api.controller.pay.nftsign;

import com.alibaba.fastjson.JSONObject;
import com.nft.common.annotation.Log;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.enums.BusinessType;
import com.nft.mall.domain.NftContractConfig;
import com.nft.mall.domain.NftTokenId;
import com.nft.mall.service.INftContractConfigService;
import com.nft.mall.service.INftTokenIdService;
import com.nft.mall.service.IToeknService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NFt签名模块
 *
 * @Data 2021.06.10
 */
@Slf4j
@RestController
@RequestMapping("/mall/token")
public class NftSignController {

    @Autowired
    private IToeknService iToeknService;

    @Autowired
    private INftContractConfigService iNftContractConfigService;

    @Autowired
    private INftTokenIdService iNftTokenIdService;

    /**
     * toUrl Nft获取nonce
     *
     * @throws IOException
     */
    @RequestMapping(value = "/{contractAddress}/nonce", method = {RequestMethod.GET})
    public AjaxResult nonceGen(@PathVariable("contractAddress") String contractAddress) {
        if (contractAddress == null) {
            return AjaxResult.error("参数错误");
        }
        AjaxResult ajaxResult = iToeknService.insertsignNftByTokenId(contractAddress);
        return AjaxResult.success("处理成功", ajaxResult);
    }

    /**
     * nftbuyer 签名获取盐,  和手续费之类的挂钩
     * !!!!!!!!!!!!!!!!!!!!!!!!!此接口只做计算,不做数据正确性验证
     * 可能错误数据会导致最后成交失败
     * nftbuyer 签名 并且返回交易的最后订单
     *
     * @return
     */
    @Log(title = "获取合约最新的一条config配置", businessType = BusinessType.GET)
    @RequestMapping(value = "/contract/config", method = {RequestMethod.GET})
    public AjaxResult cconfig() {
        List<NftContractConfig> nftContractConfigs = iNftContractConfigService.selectNftContractConfigList(new NftContractConfig());
        return AjaxResult.success(nftContractConfigs.get(nftContractConfigs.size() - 1));
    }

    /**
     * nftbuyer 签名获取盐,  和手续费之类的挂钩
     * !!!!!!!!!!!!!!!!!!!!!!!!!此接口只做计算,不做数据正确性验证
     * 可能错误数据会导致最后成交失败
     * nftbuyer 签名 并且返回交易的最后订单
     *
     * @param request
     * @param httpResponse
     * @param jsonParam
     * @return
     */
    @Log(title = "签名 并且返回交易的最后订单", businessType = BusinessType.INSERT)
    @RequestMapping(value = "/items/bestOffers", method = {RequestMethod.POST})
    public AjaxResult bestOffers(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody JSONObject jsonParam) {
        String param = jsonParam.getString("param");

        NftTokenId nftTokenId = new NftTokenId();
        nftTokenId.setToken(param.split(":")[0]);
        nftTokenId.setTokenId(Long.valueOf(param.split(":")[1]));
        boolean isQuery = param.split(":").length == 2;
        if (!isQuery) {
            String walletAddress = param.split(":")[2];
            nftTokenId.setOwner(walletAddress);
        }

        nftTokenId.setCanceld(1L);  // 1  售卖中
        List<NftTokenId> nftTokenIds = iNftTokenIdService.selectNftTokenIdList(nftTokenId);
        if (nftTokenIds.size() < 1 && !isQuery) {
            String walletAddress = param.split(":")[2];
            nftTokenId.setOwner(walletAddress);
            //插入订单记录
            nftTokenId.setSaltType("SALT");
            nftTokenId.setSaltValue(RandomStringUtils.randomNumeric(77));
            nftTokenId.setAssettype("ERC721");
            nftTokenId.setBuyToken("0x0000000000000000000000000000000000000000");   //ETH为0
//            nftTokenId.setBuyTokenId(param.split(":")[1]);
            nftTokenId.setBuyTokenId("0");
            nftTokenId.setBuyAssetType("ETH");
            //token + tokenid + owner + buyToken + buyTokenId +  salt',
            nftTokenId.setId(nftTokenId.getToken() + ":"
                    + nftTokenId.getTokenId() + ":"
                    + nftTokenId.getOwner() + ":"
                    + nftTokenId.getBuyToken() + ":" + nftTokenId.getBuyTokenId() + ":"
                    + nftTokenId.getSaltValue());
            iNftTokenIdService.insertNftTokenId(nftTokenId);
            return AjaxResult.success(nftTokenId);
        } else {
            //返回值:
            // salt  随机数字
            //selling  tokenid数量
            //buying 售卖金额
            //sellerFee 手续费
            return AjaxResult.success(nftTokenIds.get(nftTokenIds.size() - 1));
        }
    }

    /**
     * nft卖家签名价格信息
     *
     * @param request
     * @param httpResponse
     * @param jsonParam
     * @return
     */
    @Log(title = "卖方对藏品价格进行签名", businessType = BusinessType.INSERT)
    @RequestMapping(value = "/fixprice", method = {RequestMethod.POST})
    public AjaxResult sellFixedPrice(HttpServletRequest request, HttpServletResponse httpResponse, @RequestBody JSONObject jsonParam) {
        String r = jsonParam.getString("r");
        String s = jsonParam.getString("s");
        String v = jsonParam.getString("v");
        String fee = jsonParam.getString("fee");
        String sellPrice = jsonParam.getString("sellPrice");
        String value = jsonParam.getString("value");
        String id = jsonParam.getString("id");
        NftTokenId nftTokenId = new NftTokenId();
        nftTokenId.setId(id);
        nftTokenId.setSignatureR(r);
        nftTokenId.setSignatureS(s);
        nftTokenId.setSignatureV(v);
        nftTokenId.setValue((value));
        nftTokenId.setFee((fee));
        nftTokenId.setSellPrice(sellPrice);
        int raw = iNftTokenIdService.updateNftTokenId(nftTokenId);

        //修改历史成交数据
        NftTokenId nftTokenId1 = new NftTokenId();
        nftTokenId1.setToken(id.split(":")[0]);
        nftTokenId1.setTokenId(Long.valueOf(id.split(":")[1]));
        nftTokenId1.setCanceld(1L);
        List<NftTokenId> nftTokenIds = iNftTokenIdService.selectNftTokenIdList(nftTokenId1);
        for (NftTokenId tokenId : nftTokenIds) {
            if (tokenId.getId().equals(id)) {
                continue;
            } else {
                tokenId.setCanceld(2L);
                iNftTokenIdService.updateNftTokenId(tokenId);
            }
        }

        //存储数据库
        return AjaxResult.success(raw);
    }

    public static String encodePacked(String... param) {
        String zeroStr64 = "0000000000000000000000000000000000000000000000000000000000000000";
        String resultHash = "0x";
        for (String s : param) {

//            if (s.length() == 42) {
//                s = toChecksumAddress(s);
//            }

            String param16Str = s;
            if (param16Str.indexOf("0x") == 0) {
                param16Str = param16Str.substring(2);
            } else {
                param16Str = Numeric.toHexStringNoPrefix(new BigInteger(s));
            }
            param16Str = zeroStr64.substring(0, 64 - param16Str.length()) + param16Str;//注释掉，使用紧凑压缩
            resultHash += param16Str;
        }
        return resultHash;
    }

    /**
     * BuyFee 对订单和手续费签名
     *
     * @param jsonParam
     * @return
     */
    @Log(title = "项目方对平台手续费签名", businessType = BusinessType.INSERT)
    @RequestMapping(value = "/buyerFeesign", method = {RequestMethod.POST})
    public AjaxResult buyerFeesign(@RequestBody JSONObject jsonParam) {
        String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

        String id = jsonParam.getString("id");

        NftTokenId nftTokenId = iNftTokenIdService.selectNftTokenIdById(id);
        List<NftContractConfig> nftContractConfigs = iNftContractConfigService.selectNftContractConfigList(new NftContractConfig());
        NftContractConfig nftContractConfig = nftContractConfigs.get(nftContractConfigs.size() - 1);

        String privateKey = "";

        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Map<String, Integer> map = new HashMap<>();
        map.put("ETH", 0);
        map.put("ERC20", 1);
        map.put("ERC1155", 2);
        map.put("ERC721", 3);
        map.put("ERC721Deprecated", 4);

        String owner = nftTokenId.getOwner();
        String salt = nftTokenId.getSaltValue();
        String stoken = nftTokenId.getToken();
        String stokenId = String.valueOf(nftTokenId.getTokenId());
        String sassetType = String.valueOf(map.get(nftTokenId.getAssettype()));
        String btoken = nftTokenId.getBuyToken();
        String btokenId = nftTokenId.getBuyTokenId();
        String bassetType = String.valueOf(map.get(nftTokenId.getBuyAssetType()));
        String selling = String.valueOf(nftTokenId.getValue());
        String buying = String.valueOf(nftTokenId.getFee());
        String sellerFee = String.valueOf(nftTokenId.getSellPrice());
        String buyerFee = String.valueOf(nftContractConfig.getBuyerFee());

        String abiCode = encodePacked(
                owner,
                salt,
                stoken,
                stokenId,
                sassetType,
                btoken,
                btokenId,
                bassetType,
                selling,
                buying,
                sellerFee,
                buyerFee);
        byte[] abiCode_bytes = Numeric.hexStringToByteArray(abiCode);
        byte[] hashByte = Hash.sha3(abiCode_bytes);
        System.out.println(abiCode);
        String hashStr = Numeric.toHexString(hashByte);
        hashStr = hashStr.replace("0x", "");
        System.out.println(hashStr);
        String fullMessage = PERSONAL_MESSAGE_PREFIX + hashStr.length() + hashStr;
        System.out.println(fullMessage);
        String abiCode_bytes1 = Numeric.toHexString(fullMessage.getBytes());
        byte[] hashByte1 = Hash.sha3(Numeric.hexStringToByteArray(abiCode_bytes1));
        System.out.println(Numeric.toHexString(hashByte1));
        Sign.SignatureData signatureData = Sign.signMessage(hashByte1, ecKeyPair, false);
        String br = Numeric.toHexString(signatureData.getR());
        String bs = Numeric.toHexString(signatureData.getS());
        String bv = String.valueOf(signatureData.getV());
//
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("r", br);
        jsonObject.put("s", bs);
        jsonObject.put("v", bv);
        System.out.println(jsonObject);
        nftTokenId.setSignature(JSONObject.toJSONString(jsonObject));
        nftTokenId.setBuyPrice(buyerFee);
        //存储数据库
        return AjaxResult.success(nftTokenId);
    }

}
