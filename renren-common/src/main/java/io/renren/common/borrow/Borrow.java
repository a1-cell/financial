package io.renren.common.borrow;

import java.math.BigDecimal;

public class Borrow {
    private Integer borrowId;
    private String borrowName;
    private String borrowRen;
    private BigDecimal borrowMoney;
    private BigDecimal interestRate;
    private String periods;
    private String backWay;
    private String borrowTime;
    private Integer borrowStatus;
    private String behoof;
    private Integer userid;
    private String cardUrl1;
    private String cardUrl2;
    private Integer pid;
    private Integer tid;
    private Integer ttid;

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getBorrowRen() {
        return borrowRen;
    }

    public void setBorrowRen(String borrowRen) {
        this.borrowRen = borrowRen;
    }

    public BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getBackWay() {
        return backWay;
    }

    public void setBackWay(String backWay) {
        this.backWay = backWay;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Integer getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(Integer borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

    public String getBehoof() {
        return behoof;
    }

    public void setBehoof(String behoof) {
        this.behoof = behoof;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getCardUrl1() {
        return cardUrl1;
    }

    public void setCardUrl1(String cardUrl1) {
        this.cardUrl1 = cardUrl1;
    }

    public String getCardUrl2() {
        return cardUrl2;
    }

    public void setCardUrl2(String cardUrl2) {
        this.cardUrl2 = cardUrl2;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getTtid() {
        return ttid;
    }

    public void setTtid(Integer ttid) {
        this.ttid = ttid;
    }
}
