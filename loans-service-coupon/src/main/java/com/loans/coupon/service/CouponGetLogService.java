package com.loans.coupon.service;

import com.loans.coupon.dao.CouponGetLogMapper;
import com.loans.coupon.pojo.CouponGetLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponGetLogService {

    @Autowired
    private CouponGetLogMapper couponGetLogMapper;


    public void addCoupon(CouponGetLog couponGetLog) {
        couponGetLogMapper.addCoupon(couponGetLog);

    }
}
