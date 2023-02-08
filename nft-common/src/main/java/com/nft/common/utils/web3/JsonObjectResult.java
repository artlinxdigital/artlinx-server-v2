package com.nft.common.utils.web3;

import com.alibaba.fastjson.JSONObject;

public class JsonObjectResult {
    //错误码
    private int code = 200;
    //亲求信息描述
    private String message = "";
    //响应正文
    private Object data;

    public JsonObjectResult JSONObjectResultSuccess(Object data) {
        this.data = data;
        return this;
    }

    public JsonObjectResult JSONObjectResultError(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
