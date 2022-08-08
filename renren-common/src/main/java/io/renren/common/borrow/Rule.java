package io.renren.common.borrow;

import java.math.BigDecimal;

public class Rule {
    private Integer id;
    private Integer companyid;
    private BigDecimal money;
    private BigDecimal ratemin;
    private BigDecimal ratemax;
    private Integer periodsmin;
    private Integer periodsmax;
    private BigDecimal retainmoney;
    private Integer statue;
    private Integer num;
    private BigDecimal moneymax;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getRatemin() {
        return ratemin;
    }

    public void setRatemin(BigDecimal ratemin) {
        this.ratemin = ratemin;
    }

    public BigDecimal getRatemax() {
        return ratemax;
    }

    public void setRatemax(BigDecimal ratemax) {
        this.ratemax = ratemax;
    }

    public Integer getPeriodsmin() {
        return periodsmin;
    }

    public void setPeriodsmin(Integer periodsmin) {
        this.periodsmin = periodsmin;
    }

    public Integer getPeriodsmax() {
        return periodsmax;
    }

    public void setPeriodsmax(Integer periodsmax) {
        this.periodsmax = periodsmax;
    }

    public BigDecimal getRetainmoney() {
        return retainmoney;
    }

    public void setRetainmoney(BigDecimal retainmoney) {
        this.retainmoney = retainmoney;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getMoneymax() {
        return moneymax;
    }

    public void setMoneymax(BigDecimal moneymax) {
        this.moneymax = moneymax;
    }
}
