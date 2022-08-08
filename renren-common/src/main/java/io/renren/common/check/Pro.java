package io.renren.common.check;

import java.math.BigDecimal;

public class Pro {
    private Integer id;
    private Integer uid;
    private Integer borrowId;
    private BigDecimal qq;

    public BigDecimal getQq() {
        return qq;
    }

    public void setQq(BigDecimal qq) {
        this.qq = qq;
    }

    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
