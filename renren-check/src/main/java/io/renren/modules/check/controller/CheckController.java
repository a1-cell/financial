package io.renren.modules.check.controller;

import com.github.pagehelper.PageHelper;
import io.renren.common.result.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/check/v1")
@RestController
class CheckController {


    @RequestMapping("/list")
    @RequiresPermissions("check:v1:list")
    public Result list(@RequestParam Integer page, @RequestParam Integer size){
        PageHelper.startPage(page,size);

        System.out.println("哈哈哈哈");
        return new Result(true,"查询成功",null);
    }
}
