package com.renren.pojos;

import java.math.BigDecimal;

public class Product {
    private Long id;

    private BigDecimal interest;

    private String capitalType;

    private Integer status;

    private String productName;

    private String productType;

    private BigDecimal productRate;

    private Integer borrowCount;

    private String returnWay;

    private String createTime;

    private BigDecimal capitalCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCapitalCount() {
        return capitalCount;
    }

    public String getCapitalType() {
        return capitalType;
    }

    public void setCapitalType(String capitalType) {
        this.capitalType = capitalType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getProductRate() {
        return productRate;
    }

    public void setProductRate(BigDecimal productRate) {
        this.productRate = productRate;
    }

    public void setCapitalCount(BigDecimal capitalCount) {
        this.capitalCount = capitalCount;
    }

    public Integer getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(Integer borrowCount) {
        this.borrowCount = borrowCount;
    }

    public String getReturnWay() {
        return returnWay;
    }

    public void setReturnWay(String returnWay) {
        this.returnWay = returnWay;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public Product() {
    }

}
