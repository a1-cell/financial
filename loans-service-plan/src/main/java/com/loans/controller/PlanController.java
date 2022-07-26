package com.loans.controller;

import com.loans.pojo.RefundPlan;
import com.loans.pojo.Result;
import com.loans.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v1")
public class PlanController {
    @Autowired
    private PlanService planService;

    //还款计划 等额本息以及等额本金
    @PostMapping("/plan")
    public void plan(@RequestBody RefundPlan refundPlan){
        if (refundPlan.getType()==1){
            planService.principalAndInterest(refundPlan);
        }else {
            planService.getPrincipal(refundPlan);
        }
    }

    //还款计划查询列表
    @RequestMapping("/planlist")
    public Result planlist(@RequestBody RefundPlan refundPlan){
        List<RefundPlan> list =planService.list(refundPlan.getUid());
        return new Result(true,"查询成功",list);
    }



    //征信评估
    @PostMapping("/getScore")
    public void getScore(Integer uid){
        planService.getScore(uid);
    }


}
