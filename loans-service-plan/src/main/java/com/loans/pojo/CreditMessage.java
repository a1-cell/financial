package com.loans.pojo;

import java.io.Serializable;

public class CreditMessage implements Serializable {
    //征信信息主键
    private Integer creditId;
    //用户ID
    private Integer uid;
    //黑名单次数
    private Integer blacklist;
    //逾期次数
    private Integer overdue;
    //查询次数
    private Integer query;
    //欠款
    private Integer debtMoney;
    //信誉分
    private Integer creditPoints;
    //信誉等级
    private String creditLevel;
    //记录时间
    private String writeTime;

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Integer blacklist) {
        this.blacklist = blacklist;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }

    public Integer getQuery() {
        return query;
    }

    public void setQuery(Integer query) {
        this.query = query;
    }

    public Integer getDebtMoney() {
        return debtMoney;
    }

    public void setDebtMoney(Integer debtMoney) {
        this.debtMoney = debtMoney;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    public Integer getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(Integer creditPoints) {
        this.creditPoints = creditPoints;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }
}
