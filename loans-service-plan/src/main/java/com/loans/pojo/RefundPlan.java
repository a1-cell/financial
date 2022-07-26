package com.loans.pojo;

import java.math.BigDecimal;

public class RefundPlan {
    private int uid;
    private int loansId;
    private int refundPeriods;
    private String refundTime;
    private BigDecimal refundMoney;
    private BigDecimal refundInterest;
    private BigDecimal sumMoney;
    private BigDecimal refundBalance;
    private BigDecimal serviceMoney;
    private BigDecimal securityCost;

    private int type;
    private double principal;
    private double interest;
    private int periods;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRefundPeriods() {
        return refundPeriods;
    }

    public void setRefundPeriods(int refundPeriods) {
        this.refundPeriods = refundPeriods;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getLoansId() {
        return loansId;
    }

    public void setLoansId(int loansId) {
        this.loansId = loansId;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    public BigDecimal getRefundInterest() {
        return refundInterest;
    }

    public void setRefundInterest(BigDecimal refundInterest) {
        this.refundInterest = refundInterest;
    }

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }

    public BigDecimal getRefundBalance() {
        return refundBalance;
    }

    public void setRefundBalance(BigDecimal refundBalance) {
        this.refundBalance = refundBalance;
    }

    public BigDecimal getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(BigDecimal serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public BigDecimal getSecurityCost() {
        return securityCost;
    }

    public void setSecurityCost(BigDecimal securityCost) {
        this.securityCost = securityCost;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    @Override
    public String toString() {
        return "RefundPlan{" +
                "uid=" + uid +
                ", loansId=" + loansId +
                ", refundTime='" + refundTime + '\'' +
                ", refundMoney=" + refundMoney +
                ", refundinterest=" + refundInterest +
                ", sumMoney=" + sumMoney +
                ", refundBalance=" + refundBalance +
                ", serviceMoney=" + serviceMoney +
                ", securityCost=" + securityCost +
                ", principal=" + principal +
                ", interest=" + interest +
                ", periods=" + periods +
                '}';
    }
}
