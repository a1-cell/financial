package com.loans.coupon.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    //券id
    private Integer cid;
    //创建人的id
    private Integer uid;
    //审核人的id
    private Integer aid;
    //券名
    private String cname;
    //利率
    private BigDecimal interest;
    //活动开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //活动结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    //券的类型
    private Integer tid;
    //使用类型
    private Integer type;
    //状态
    private String statue;
    //发放类型
    private Integer grante;
    //发放的数量
    private Integer num;
    //已经领取了的数量
    private Integer userdNum;
    //有效天数
    private Integer days;
    //最小投资金额monry
    private BigDecimal money;
    //最大投资金额
    private BigDecimal maxMoney;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date foundTime;
    //审批时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date examineTime;



}
