package com.bw.user.controller;


import com.bw.user.service.UserService;
import io.renren.common.result.Result;
import io.renren.common.userEnttiy.User;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        return userService.login(user);
    }

}
