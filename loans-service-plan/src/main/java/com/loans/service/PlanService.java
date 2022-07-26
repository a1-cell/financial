package com.loans.service;

import com.loans.mapper.PlanMapper;
import com.loans.pojo.CreditMessage;
import com.loans.pojo.RefundPlan;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlanService {
    @Autowired
    private PlanMapper planMapper;


    //等额本息
    public void principalAndInterest(RefundPlan refundPlan) {
        refundPlan.setInterest(0.012);
        List<RefundPlan> list=new Vector<>();
        Map<Integer, BigDecimal> map = new HashMap<>();
        BigDecimal periodInterest;
        for (int i = 1; i < refundPlan.getPeriods() + 1; i++) {
            BigDecimal multiply = new BigDecimal(refundPlan.getPrincipal()).multiply(new BigDecimal(refundPlan.getInterest()));
            BigDecimal s = new BigDecimal(Math.pow(1 + refundPlan.getInterest(), refundPlan.getPeriods()))
                    .subtract(new BigDecimal(Math.pow(1 + refundPlan.getInterest(), i - 1)));
            periodInterest = multiply.multiply(s).divide(new BigDecimal(Math.pow(1 + refundPlan.getInterest(), refundPlan.getPeriods()) - 1),
                    6, BigDecimal.ROUND_HALF_UP);
            periodInterest = periodInterest.setScale(5, BigDecimal.ROUND_HALF_UP);
            map.put(i, periodInterest);
        }
        BigDecimal monthIncome = new BigDecimal(refundPlan.getPrincipal())
                .multiply(new BigDecimal(refundPlan.getInterest() * Math.pow(1 + refundPlan.getInterest(), refundPlan.getPeriods())))
                .divide(new BigDecimal(Math.pow(1 + refundPlan.getInterest(), refundPlan.getPeriods()) - 1), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal smoney = new BigDecimal(refundPlan.getPrincipal());
        System.out.println(smoney);
        for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
            // smoney=smoney.add(entry.getValue());
            RefundPlan refundPlan1 = new RefundPlan();
            refundPlan1.setUid(refundPlan.getUid());
            refundPlan1.setLoansId(refundPlan.getLoansId());
            refundPlan1.setRefundPeriods(entry.getKey());
            refundPlan1.setRefundMoney(monthIncome.subtract(entry.getValue()));
            refundPlan1.setRefundInterest(entry.getValue());
            refundPlan1.setSumMoney(monthIncome);
            list.add(refundPlan1);
        }
        Date date = new Date();
        int i=0;
        for (RefundPlan plans : list) {
            i++;
            date.setMonth(date.getMonth()+1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            String format = sdf.format(date);
            System.out.println(format);
            if (i!= refundPlan.getPeriods()){
                System.out.println(i);
                smoney=smoney.subtract(plans.getRefundMoney());
                plans.setRefundBalance(smoney);
            }else {
                plans.setRefundBalance(new BigDecimal(0));
            }
            System.out.println(plans.getRefundBalance());
            plans.setRefundTime(format);
            planMapper.plan(plans);
        }
    }


    /**
     * 计算获取还款方式为等额本金的每期偿还本金和利息
     *
     * principal;本金
     * interest;月利率
     * periods;期数
     * 公式：每期偿还本金=(贷款本金÷还款期数)+(贷款本金-已归还本金累计额)×期利率
     * @return 每期偿还本金和利息,四舍五入
     */
    public void  getPrincipal(RefundPlan refundPlan) {
        refundPlan.setInterest(0.012);
        List<RefundPlan> list=new Vector<>();
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        // 每期本金
        BigDecimal monthIncome = new BigDecimal(refundPlan.getPrincipal()).divide(new BigDecimal(refundPlan.getPeriods()), 4, BigDecimal.ROUND_HALF_UP);
        double period = monthIncome.doubleValue();
        for (int i = 1; i <= refundPlan.getPeriods(); i++) {
            double monthRes = period + (refundPlan.getPrincipal() - period * (i - 1)) * refundPlan.getInterest();
            monthRes = new BigDecimal(monthRes).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put(i, monthRes);
        }


        BigDecimal principal = monthIncome;
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            BigDecimal principalBigDecimal = principal;
            BigDecimal principalInterestBigDecimal = new BigDecimal(entry.getValue());
            BigDecimal interestBigDecimal = principalInterestBigDecimal.subtract(principalBigDecimal);
            interestBigDecimal = interestBigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP);



            RefundPlan refundPlan1 = new RefundPlan();
            refundPlan1.setUid(refundPlan.getUid());
            refundPlan1.setLoansId(refundPlan.getLoansId());
            refundPlan1.setRefundPeriods(entry.getKey());
            refundPlan1.setRefundMoney(monthIncome);
            refundPlan1.setRefundInterest(interestBigDecimal);
            refundPlan1.setSumMoney(BigDecimal.valueOf(entry.getValue()));
            list.add(refundPlan1);
        }
        Date date = new Date();
        BigDecimal smoney = new BigDecimal(refundPlan.getPrincipal());
        int i=0;
        for (RefundPlan plans : list) {
            i++;
            date.setMonth(date.getMonth()+1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            String format = sdf.format(date);
            System.out.println(format);
            if (i!= refundPlan.getPeriods()){
                System.out.println(i);
                smoney=smoney.subtract(plans.getRefundMoney());
                plans.setRefundBalance(smoney);
            }else {
                plans.setRefundBalance(new BigDecimal(0));
            }
            System.out.println(plans.getRefundBalance());
            plans.setRefundTime(format);
            planMapper.plan(plans);
        }
    }






    public void getScore(Integer uid){
        CreditMessage creditMessage=planMapper.getCreditMessage(uid);
        creditMessage.setCreditPoints(500);
        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();

        KieSession kieSession = kc.newKieSession("rule1KS");

        kieSession.insert(creditMessage);

        kieSession.fireAllRules();

        kieSession.dispose();

        System.out.println("信誉积分为"+creditMessage.getCreditPoints()+",评级为："+creditMessage.getCreditLevel());

        planMapper.setScore(creditMessage);
    }


    public List<RefundPlan> list(int uid) {
        return   planMapper.list(uid);
    }
}
