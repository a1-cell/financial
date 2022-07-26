package com.loans.pojo;

public class CreditEvaluation {
    //主键
    private Integer cid;
    //用户ID
    private Integer uid;
    //信誉分
    private Integer creditPoints;
    //信誉等级
    private String creditLecel;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(Integer creditPoints) {
        this.creditPoints = creditPoints;
    }

    public String getCreditLecel() {
        return creditLecel;
    }

    public void setCreditLecel(String creditLecel) {
        this.creditLecel = creditLecel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
