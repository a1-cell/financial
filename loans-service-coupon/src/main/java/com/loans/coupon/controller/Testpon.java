package com.loans.coupon.controller;

import com.loans.coupon.pojo.Coupon;
import com.loans.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/coupon")
@CrossOrigin
public class Testpon {

    @Autowired
    private CouponService couponService;

    @RequestMapping("/test")
    public String test() {

        List<Coupon> list = couponService.getListBystatue();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list",list);
        modelAndView.setViewName("list");
        return "test1";
    }

}
