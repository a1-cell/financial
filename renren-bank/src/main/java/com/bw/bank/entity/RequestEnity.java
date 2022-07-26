package com.bw.bank.entity;

public class RequestEnity {

    private String serviceName;

    private String platformNo;

    private String userDevice;

    private String reqData;

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

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
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

    public String getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(String userDevice) {
        this.userDevice = userDevice;
    }
}
