package com.bw.user.controller;


import com.bw.user.service.UserService;
import io.renren.common.result.Result;
import io.renren.common.userEnttiy.User;
import io.renren.common.utils.JwtUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/v1")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;



    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User user1=userService.getuser(user);
        if(user1==null){
            return new Result(false,"不存在该用户!","");
        }
        SimpleHash md5 = new SimpleHash("MD5", user.getUserpassword(), user1.getUserphone(), 5);
        String s = md5.toBase64();
        String token="";
        if(s.equals(user1.getUserpassword())){
            //生成token
            try {
                token = JwtUtil.sign(String.valueOf(user1.getUserid()), "", "root");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return new Result(true,"登录成功!",token);
        }else{
            return new Result(false,"用户名或密码错误!","");
        }
    }

    @GetMapping("/user")
    public Result userList(){
        List<User> list=userService.userList();
        return new Result(true,"查询成功",list);
    }
    @PostMapping("/adduser")
    public Result addUser(@RequestBody User user) throws NoSuchAlgorithmException {
        System.out.println("0");
        return  userService.addUser(user);
    }
    @GetMapping("/getuser")
    public Result getuserbyid(){
        List<User> list=userService.getuserbyid();
        return new Result(true,"查询成功",list);
    }



}
