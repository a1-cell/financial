package com.loans.coupon.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loans.coupon.pojo.Notice;
import com.loans.coupon.service.NoticeService;
import com.loans.coupon.utils.ResultInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notice")
@CrossOrigin
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    private final String now = DateUtil.now();

    // 新增
    @PostMapping("/save")
    public ResultInfo save(@RequestBody Notice notice) {

        notice.setTime(DateUtil.now());
        notice.setUser("admin");
        noticeService.saveOrUpdate(notice);
        return new ResultInfo(true,"执行成功",null);
    }

    @GetMapping("/page")
    public ResultInfo page(){

        List<Notice> list = noticeService.selectAll();

        return new ResultInfo(true,"执行成功",list);
    }


}
