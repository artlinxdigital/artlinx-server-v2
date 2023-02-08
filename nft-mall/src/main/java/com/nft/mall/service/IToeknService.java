package com.nft.mall.service;

import com.alibaba.fastjson.JSONObject;
import com.nft.common.core.domain.AjaxResult;
import com.nft.mall.domain.Charge;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface IToeknService {

    AjaxResult insertsignNftByTokenId(String contractAddress);
}
