package com.bw.user.controller;

import com.bw.user.service.UserService;
import io.renren.common.userEnttiy.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> userList(){
        List<User> list=userService.userList();
        return list;
    }

}
