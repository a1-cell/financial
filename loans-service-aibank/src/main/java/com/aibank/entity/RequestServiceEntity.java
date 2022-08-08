package com.aibank.entity;

import java.util.Map;

public class RequestServiceEntity {

    private String serviceName;

    private String platformNo;

    private Map reqData;

    private Integer keySerial;

    private String sign;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public Map getReqData() {
        return reqData;
    }

    public void setReqData(Map reqData) {
        this.reqData = reqData;
    }

    public Integer getKeySerial() {
        return keySerial;
    }

    public void setKeySerial(Integer keySerial) {
        this.keySerial = keySerial;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
