package com.loans.mapper;

import com.loans.pojo.CreditMessage;
import com.loans.pojo.RefundPlan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PlanMapper {


    @Insert("insert into refund_plan values (#{uid},#{loansId},#{refundPeriods},#{refundTime},#{refundMoney},#{refundInterest},#{sumMoney},#{refundBalance},#{serviceMoney},#{securityCost})")
    void plan(RefundPlan refundPlan);

    @Select("select loans_id loansId,refund_time refundTime,refund_money refundMoney,refund_interest refundInterest,sum_money sumMoney,refund_balance refundBalance from refund_plan where uid=#{uid}")
    List<RefundPlan> list(int uid);

    @Select("select credit_id creditId, uid,blacklist,overdue,query,debt_money debtMoney,credit_points creditPoints,credit_level creditLevel,write_time writeTime from credit_message where uid=#{uid}")
    CreditMessage getCreditMessage(Integer uid);

    @Update("update credit_message set credit_points=#{creditPoints},credit_level=#{creditLevel} where uid=#{uid}")
    void setScore(CreditMessage creditMessage);
}
