package com.loans.coupon.dao;

import com.loans.coupon.pojo.CouponGetLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface CouponGetLogMapper {


    @Insert("insert into coupon_get_log(cid,uid,get_time,end_time) values(#{couponGetLog.cid},#{couponGetLog.uid},#{couponGetLog.getTime},#{couponGetLog.endTime})")
    void addCoupon(@Param("couponGetLog") CouponGetLog couponGetLog);
}
