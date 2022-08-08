package com.loans.coupon.controller;

import com.loans.coupon.pojo.Coupon;
import com.loans.coupon.pojo.CouponGetLog;
import com.loans.coupon.service.CouponGetLogService;
import com.loans.coupon.service.CouponService;
import com.loans.coupon.utils.ResultInfo;
import io.renren.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/coupon")
@CrossOrigin
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CouponGetLogService couponGetLogService;


    //新增优惠券
    @PostMapping("/add")
    public boolean add(@RequestBody Coupon coupon){
        coupon.setUid(1);
        coupon.setFoundTime(new Date());
        coupon.setStatue("0");
        couponService.add(coupon);
        System.out.println(coupon);
        Result<Object> result = new Result<>();
        return result.success();
    }


    //得到未审核的优惠券信息
    @RequestMapping("/getList")
    public ResultInfo getList(){

        List<Coupon> list = couponService.getList();


        return new ResultInfo(true,"执行成功",list);
    }
    //得到全部的优惠券信息
    @GetMapping("/getNewList")
    public ResultInfo getMap(){
        //查询优惠券
        List<Coupon> list = couponService.selectList();


        return new ResultInfo(true, "执行成功",list);
    }


    //审核同意
    @GetMapping("/upStutue")
    public ResultInfo update(Integer cid) {
        System.out.println(cid);
        System.out.println("审核的id为:"+cid);
        if (cid == null) {
            return new ResultInfo(false,"执行失败","传入的参数不能为空");
        }
        couponService.update(cid);

        return new ResultInfo(true, "执行成功",null);
    }
    //审核拒绝
    @GetMapping("/upStatue")
    public ResultInfo updateStatue(Integer cid){

        couponService.update1(cid);
        return new ResultInfo(true, "执行成功",null);
    }

    //抢券方法
    @RequestMapping("/snag")
    public ResultInfo snag(HttpServletRequest request){

        String localKey = "lock01";
        String clentId = UUID.randomUUID().toString();

        try {
            Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(localKey,clentId,10, TimeUnit.SECONDS);
            if (aBoolean) {
                //得到用户id和券id
                String cid = request.getParameter("cid");
                String uid = request.getParameter("uid");
                System.out.println(cid);
                System.out.println(uid);
                String s = stringRedisTemplate.opsForValue().get(cid + uid);
                //根据券id得到券信息
                Coupon coupon = couponService.getCouponById(cid);
                //创建领取记录表
                CouponGetLog couponGetLog = new CouponGetLog();
                couponGetLog.setCid(Integer.parseInt(cid));
                couponGetLog.setUid(Integer.parseInt(uid));
                //得到库存
                Integer num = coupon.getNum();
                //计算库存
                num = num -1;
                //更新库存
                coupon.setNum(num);
                //判断领取了的数量
                Integer userdNum = coupon.getUserdNum();
                //是空的话将其数量设为0
                if (userdNum == null) {
                    userdNum = 0;
                }
                //每领一次领取的数量+1
                userdNum = userdNum+1;
                //设置领取了的数量
                coupon.setUserdNum(userdNum);
                //根据有效天数设置过期时间
                Integer days = coupon.getDays();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                couponGetLog.setGetTime(new Date());
                date.setDate(date.getDate()+days);
                couponGetLog.setEndTime(date);
                System.out.println(couponGetLog);

                couponGetLogService.addCoupon(couponGetLog);
            }
        }finally {
            if (clentId.equals(stringRedisTemplate.opsForValue().get(localKey))){
                stringRedisTemplate.delete(localKey);
            }
        }


        return new ResultInfo(true, "执行成功",null);
    }





}
