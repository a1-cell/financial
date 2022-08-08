package io.renren.common.exception;


import io.renren.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@ControllerAdvice    //全局异常捕获
public class CatchException {

    private static HashMap<String,String> map=new HashMap<>();
    static {
        map.put("java.lang.ArithmeticException","分母不能为0");
    }
    @ExceptionHandler
    @ResponseBody
    public Result catchException(Exception exception){

        exception.printStackTrace();
        String name = exception.getClass().getName();
       // System.out.println(name);
        //判断是否为已知异常
        if(name.equals("com.pinxixi.exception.PinxixiException")){
            //强转为已知异常
            EducationException pinxixiException= (EducationException) exception;
            String message = pinxixiException.getMessage();
            return new Result(false,"异常",message);
        }
        String s = map.get(name);
        if(s!=null){
            return new Result(false,"异常",s);
        }
        return new Result(false,"异常",name);
    }
}
