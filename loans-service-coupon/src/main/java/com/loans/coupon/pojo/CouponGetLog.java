package com.loans.coupon.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponGetLog {

    private Integer id;

    private Integer cid;

    private Integer uid;

    private Date getTime;

    private Date endTime;

}
