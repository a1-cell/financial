package io.renren.common.es;

import java.math.BigDecimal;

public class BackList {
    private Integer id;
    private Integer userid;
    private String username;
    private Integer seenum;
    private Integer overduenum;
    private BigDecimal money;
    private String creattime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSeenum() {
        return seenum;
    }

    public void setSeenum(Integer seenum) {
        this.seenum = seenum;
    }

    public Integer getOverduenum() {
        return overduenum;
    }

    public void setOverduenum(Integer overduenum) {
        this.overduenum = overduenum;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCreattime() {
        return creattime;
    }

    public void setCreattime(String creattime) {
        this.creattime = creattime;
    }
}
