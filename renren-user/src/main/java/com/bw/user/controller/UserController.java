package com.bw.user.controller;


import com.bw.user.entity.Result;
import com.bw.user.entity.User;
import com.bw.user.service.UserService;
import oracle.jdbc.proxy.annotation.Post;
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


//    @PostMapping("/login")
//    public Result login(@RequestBody User user){
//        return userService.login(user);
//    }

}
