package com.loans.coupon.service;

import com.loans.coupon.dao.CouponMapper;
import com.loans.coupon.pojo.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {


    @Autowired
    CouponMapper couponMapper;


    public void add(Coupon coupon){

        couponMapper.insertCoupon(coupon);


    }


    public List<Coupon> getList() {
        return couponMapper.getList();
    }

    public void update(Integer cid) {

        couponMapper.update(cid);

    }

    public void update1(Integer cid) {

        couponMapper.update1(cid);

    }

    public List<Coupon> selectList() {
        return couponMapper.selectList();
    }

    public Coupon getCouponById(String cid) {
        return couponMapper.getCouponById(cid);
    }

    public List<Coupon> getListBystatue() {
        return couponMapper.getListBystatue();
    }
}
