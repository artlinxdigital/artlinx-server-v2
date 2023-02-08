package com.nft.mall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nft.common.core.domain.AjaxResult;
import com.nft.mall.domain.NftSign;
import com.nft.mall.domain.NftSignExample;
import com.nft.mall.mapper.NftSignMapper;
import com.nft.mall.service.IToeknService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@Service
public class ToeknServiceImpl implements IToeknService {


    @Autowired
    private NftSignMapper nftSignMapper;

    @Override
    public AjaxResult insertsignNftByTokenId(String contractAddress) {
        NftSignExample signExample = new NftSignExample();
        signExample.createCriteria().andContractAddressEqualTo(contractAddress);
        Long count = nftSignMapper.countByExample(signExample);
        count = count + 1;
        String privateKey = "";
//        String privateKey = "";
        String tokenId = String.valueOf(count); // ---commit

        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));

        if (!Numeric.containsHexPrefix(tokenId)) {
            tokenId = Numeric.toHexStringNoPrefixZeroPadded(new BigInteger(tokenId).abs(), 64);
        }
        if (!Numeric.containsHexPrefix(contractAddress)) {
            contractAddress = Numeric.toHexStringNoPrefixZeroPadded(new BigInteger(contractAddress).abs(), 64);
        }

        String abiCode = contractAddress.toLowerCase().concat(tokenId);

        byte[] abiCode_bytes = Numeric.hexStringToByteArray(abiCode);
        Sign.SignatureData signatureData = Sign.signMessage(abiCode_bytes, ecKeyPair, true);
        String r = Numeric.toHexString(signatureData.getR());
        String s = Numeric.toHexString(signatureData.getS());
        String v = String.valueOf(signatureData.getV());

        //插入数据库
        NftSign nftSign = new NftSign();
        nftSign.setContractAddress(contractAddress);
        nftSign.setNonce(String.valueOf(count));
        nftSignMapper.insertSelective(nftSign);

        AjaxResult ajaxResult = new AjaxResult();
//        "value": 989,
//                "v": 28,
//                "r": "0x0f7f703291e2dff452298b278c9667436996df8bb865a522ecdbe2f128507d3c",
//                "s": "0x276ede7afd43f1f62ceb5693154c80094aafc828d2ea26d80d544482b8b67a86"
        ajaxResult.put("value", count);
        ajaxResult.put("v", v);
        ajaxResult.put("r", r);
        ajaxResult.put("s", s);
        return ajaxResult;
    }

    public static String toChecksumAddress(String address) {
        if (address == null) return "";
        address = address.toLowerCase().replace("0x", "");
        String addressHash = Hash.sha3(address).replace("0x", "");
        String checksumAddress = "0x";

        for (int i = 0; i < address.toCharArray().length; i++) {
            // If ith character is 8 to f then make it uppercase
            if (Integer.parseInt(String.valueOf(addressHash.toCharArray()[i]), 16) > 7) {
                checksumAddress += String.valueOf(address.toCharArray()[i]).toUpperCase();
                System.out.println(String.valueOf(address.toCharArray()[i]).toUpperCase());
            } else {
                checksumAddress += String.valueOf(address.toCharArray()[i]).toLowerCase();
            }
        }
        return checksumAddress;
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

    public static void main(String[] args) {

        String privateKey = "";
        /**
         * 以太坊自定义的签名消息都以以下字符开头
         * 参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
         */
        String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));

        String owner = "0x70997970c51812dc3a010c7d01b50e0d17dc79c8";
        String salt = "07876053615722796232576263467801877818179840787062707403963871427523614154116";
        String stoken = "0x075FbeB3802AfdCDe6DDEB1d807E4805ed719eca";
        String stokenId = String.valueOf(14);
        String sassetType = 3 + "";
        String btoken = "0x0000000000000000000000000000000000000000";
        String btokenId = String.valueOf(0);
        String bassetType = String.valueOf(0);
        String selling = String.valueOf(1);
        String buying = "10000000000000000000";
//        buying = Numeric.toHexStringWithPrefix(new BigInteger(buying));
        String sellerFee = String.valueOf(250);
        String buyerFee = String.valueOf(250);

        String abiCode = encodePacked(owner, salt, stoken, stokenId, sassetType, btoken, btokenId, bassetType, selling, buying, sellerFee, buyerFee);
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
    }

}
