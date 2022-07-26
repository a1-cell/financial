package com.bw.user.service;

import com.bw.user.mapper.UserMapper;
import io.renren.common.result.Result;
import io.renren.common.userEnttiy.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> userList() {
        return userMapper.userList();
    }

    public Result addUser(User user) {
        int i=userMapper.checkUserName(user.getUsername());
        if(i>=1){
            return new Result(false,"用户名称重复!","");
        }
        //密码进行多次md5加密
        SimpleHash md5 = new SimpleHash("MD5", user.getUserpassword(), user.getUserphone(), 5);
        String s = md5.toBase64();
        System.out.println(">>>>>>>>>"+s);
        user.setUserpassword(s);
        userMapper.addUser(user);
        return new Result(true,"注册成功!","");
    }
}
