package io.renren.modules.check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.renren.common.borrow.Borrow;
import io.renren.common.check.Content;
import io.renren.common.result.Result;
import io.renren.modules.check.service.CheckService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/check/borrow")
@RestController
@CrossOrigin
class CheckController {
    @Autowired
    private CheckService checkService;


    @RequestMapping("/list")
    @RequiresPermissions("check:borrow:list")
    public Result list(){
        List<Content> list=checkService.list();
        System.out.println("哈哈哈哈");
        return new Result(true,"查询成功",list);
    }



    @GetMapping("/hh")
    public Result hh() throws Exception {

        List<Borrow> borrow=checkService.borrowList();
        for (Borrow borrow1 : borrow) {
            String borrowTime = borrow1.getBorrowTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(borrowTime);
            String periods = borrow1.getPeriods();
            String s = periods.split("个")[0];
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Date time = calendar.getTime();
//            System.out.println("发布时间："+formatter.format(time)+"------"+s);
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+Integer.parseInt(s));
            Date time1 = calendar.getTime();
//            System.out.println("还款时间："+formatter.format(time1)+"++++++++"+s);
            calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-7);
            Date time2 = calendar.getTime();
            System.out.println("计算出还款时间并且得出前七天的时间："+formatter.format(time2));
            Date date1 = new Date();
            if (time2.before(date1)){
                System.out.println("发短信get用户");
            }
        }

        return new Result(true,"查询成功",null);
    }
}
