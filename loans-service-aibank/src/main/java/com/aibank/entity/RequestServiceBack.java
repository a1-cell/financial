package com.aibank.entity;

import java.util.Map;

public class RequestServiceBack {
    private Map respData;
    private String sign;

    public Map getRespData() {
        return respData;
    }

    public void setRespData(Map respData) {
        this.respData = respData;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public RequestServiceBack(Map respData, String sign) {
        this.respData = respData;
        this.sign = sign;
    }

    public RequestServiceBack() {
    }
}
