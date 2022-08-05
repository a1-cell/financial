package io.renren.common.check;

import io.swagger.models.auth.In;

import java.math.BigDecimal;

public class Creditor {

    private Long credutorId;
    private String title;
    private String tranRen;
    private BigDecimal money;
    private BigDecimal rateXi;
    private BigDecimal tranMoney;
    private BigDecimal rate;
    private String daiHuan;
    private String credutorStatus;
    private Integer borrowId;
    private String borrowRen;

    public String getBorrowRen() {
        return borrowRen;
    }

    public void setBorrowRen(String borrowRen) {
        this.borrowRen = borrowRen;
    }

    public Long getCredutorId() {
        return credutorId;
    }

    public void setCredutorId(Long credutorId) {
        this.credutorId = credutorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTranRen() {
        return tranRen;
    }

    public void setTranRen(String tranRen) {
        this.tranRen = tranRen;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getRateXi() {
        return rateXi;
    }

    public void setRateXi(BigDecimal rateXi) {
        this.rateXi = rateXi;
    }

    public BigDecimal getTranMoney() {
        return tranMoney;
    }

    public void setTranMoney(BigDecimal tranMoney) {
        this.tranMoney = tranMoney;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getDaiHuan() {
        return daiHuan;
    }

    public void setDaiHuan(String daiHuan) {
        this.daiHuan = daiHuan;
    }

    public String getCredutorStatus() {
        return credutorStatus;
    }

    public void setCredutorStatus(String credutorStatus) {
        this.credutorStatus = credutorStatus;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }
}
