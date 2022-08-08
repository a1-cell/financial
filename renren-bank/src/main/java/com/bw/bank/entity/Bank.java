package com.bw.bank.entity;

public class Bank {
    private Integer bankid;
    private Integer userid;
    private String bankname;
    private String bankphone;
    private String bankidcard;
    private String creattime;
    private String altertime;

    public String getBankphone() {
        return bankphone;
    }

    public void setBankphone(String bankphone) {
        this.bankphone = bankphone;
    }

    public Integer getBankid() {
        return bankid;
    }

    public void setBankid(Integer bankid) {
        this.bankid = bankid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankidcard() {
        return bankidcard;
    }

    public void setBankidcard(String bankidcard) {
        this.bankidcard = bankidcard;
    }

    public String getCreattime() {
        return creattime;
    }

    public void setCreattime(String creattime) {
        this.creattime = creattime;
    }

    public String getAltertime() {
        return altertime;
    }

    public void setAltertime(String altertime) {
        this.altertime = altertime;
    }
}
