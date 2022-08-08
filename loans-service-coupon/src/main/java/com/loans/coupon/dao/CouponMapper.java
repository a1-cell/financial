package com.loans.coupon.dao;

import com.loans.coupon.pojo.Coupon;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CouponMapper {

    @Insert("insert into tb_coupon(uid,cname,interest,create_time,end_time,tid,`type`,statue,grante,num,used_num,days,money,found_time,max_money) values " +
            "   (#{coupon.uid},#{coupon.cname},#{coupon.interest},#{coupon.createTime},#{coupon.endTime},#{coupon.tid},#{coupon.type},#{coupon.statue},#{coupon.grante},#{coupon.num},#{coupon.userdNum},#{coupon.days},#{coupon.money},#{coupon.foundTime},#{coupon.maxMoney})")
    void insertCoupon(@Param("coupon") Coupon coupon);

    @Select("select cid,uid,cname,interest,create_time createTime,end_time endTime,tid,type,statue,grante,num,days,money,max_money maxMoney,found_time foundTime from tb_coupon where statue = 0")
    List<Coupon> getList();


    @Update("UPDATE tb_coupon set statue = 1,aid=1,examine_time=now() where cid = #{cid}")
    void update(@Param("cid") Integer cid);

    @Select("UPDATE tb_coupon set statue = 2,aid=1,examine_time=now() where cid = #{cid}")
    void update1(Integer cid);

    @Select("select cid,uid,cname,interest,create_time createTime,end_time endTime,tid,type,statue,grante,num,days,money,max_money maxMoney,found_time foundTime from tb_coupon")
    List<Coupon> selectList();

    @Select("select cid,uid,cname,interest,create_time createTime,end_time endTime,tid,type,statue,grante,num,days,money,max_money maxMoney,found_time foundTime from tb_coupon where cid=#{cid}")
    Coupon getCouponById(String cid);

    @Select("select cid,uid,cname,interest,create_time createTime,end_time endTime,tid,type,statue,grante,num,days,money,max_money maxMoney,found_time foundTime from tb_coupon where cid=#{cid} where statue=1")
    List<Coupon> getListBystatue();
}
