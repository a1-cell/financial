package io.renren.modules.check.controller;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.xml.bind.SchemaOutputResolver;

@Component
public class TestController {

    @XxlJob("jobTest")
    public void Test(){
        System.out.println("-----------启动成功");
    }

}
