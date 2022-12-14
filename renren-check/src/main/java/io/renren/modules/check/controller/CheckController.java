package io.renren.modules.check.controller;




import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.renren.common.borrow.Borrow;
import io.renren.common.check.Content;
import io.renren.common.check.Creditor;
import io.renren.common.exception.EducationException;
import io.renren.common.result.Result;
import io.renren.modules.check.service.CheckService;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.data.redis.core.StringRedisTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.beans.Transient;

import java.math.BigDecimal;

import java.beans.Transient;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequestMapping("/check")
@RestController
@CrossOrigin
class CheckController {
    private static final Logger log = LoggerFactory.getLogger(CheckController.class);
    @Autowired
    private CheckService checkService;

    @Autowired(required = false)
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/list")
    public Result list(){
        List<Content> list=checkService.list();
        System.out.println("????????????");
        return new Result(true,"????????????",list);
    }



    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;



    @GetMapping("/mqcun")
    @XxlJob("jobLiu")
    public Result mqCun(){
        System.out.println("??????666");
        List<Borrow> borrow=checkService.borrowList();
        for (Borrow borrow1 : borrow) {
            if (borrow1.getCstatus().equals("???????????????")){
                rabbitTemplate.convertAndSend("wxmq",borrow1);
            }
        }
        return new Result(true,"mq???????????????????????????",null);
    }

    @GetMapping("/mqhh")
    public Result mqhh(){
        List<Borrow> borrow=checkService.borrowList();
        return new Result(true,"??????",borrow);
    }


    @PostMapping("/credutor")
    public Result creList(@RequestParam("page")Integer page,@RequestParam("size")Integer size,@RequestParam("userid")Integer userid){
        PageHelper.startPage(page,size);
        List<Borrow> list=checkService.creList(userid);
        PageInfo<Borrow> borrowPageInfo = new PageInfo<>(list);
        Map map=new HashMap();
        map.put("list",borrowPageInfo.getList());
        map.put("total",borrowPageInfo.getTotal());
        return new Result(true,"???????????????????????????????????????",map);
    }
    @PostMapping("/creBack/{borrowId}")
    public Result getCreList(@PathVariable Integer borrowId){
        Borrow borrow1 = new Borrow();
        borrow1.setBorrowId(borrowId);
        Borrow borrow=checkService.getCreList(borrow1);
        return new Result(true,"????????????????????????",borrow);
    }

    @PostMapping("/addCredutor")
    @Transient
    public Result addCredutor(@RequestBody Creditor credutor){
        try {
            checkService.addCre(credutor);
            checkService.updateCre(credutor);
        }catch (Exception e){
            EducationException.exception("????????????");
        }
        return new Result(true,"???????????????????????????",null);
    }
    @PostMapping("/updateBorrow")
    public Result updateBorrow(@RequestBody Borrow borrow){
        checkService.updateBorrow(borrow);
        return new Result(true,"??????????????????",null);
    }

    @GetMapping("/credutorList")
    public Result credutorList(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
        PageHelper.startPage(page,size);
        List<Creditor> list=checkService.credutorList();
        PageInfo<Creditor> borrowPageInfo = new PageInfo<>(list);
        Map map=new HashMap();
        map.put("list",borrowPageInfo.getList());
        map.put("total",borrowPageInfo.getTotal());
        return new Result(true,"???????????????????????????",map);
    }
    @GetMapping("/getPrice/{credutorId}")
    public Result getPrice(@PathVariable Integer credutorId){
        Creditor credutor=checkService.getPrice(credutorId);
        return new Result(true,"??????????????????",credutor);
    }

    @PostMapping("/addSet/{creductorId}/{uid}/{qq}")
    public Result addSet(@PathVariable Integer creductorId, @PathVariable Integer uid, @PathVariable BigDecimal qq){
        checkService.addSet(creductorId,uid,qq);
        return new Result(true,"????????????",null);
    }


    @GetMapping("/borrowList/{borrowId}")
    public Result borrowList(@PathVariable Integer borrowId){
        Borrow list=checkService.findBorrowList(borrowId);
        return new Result(true,"????????????",list);
    }


}
