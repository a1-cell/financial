package com.bw.user.service;

import com.bw.user.entity.Result;
import com.bw.user.entity.User;
import com.bw.user.mapper.UserMapper;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    public Result login(User user) {
        User user1=userMapper.findByUserName(user.getUsername());
        if(user1==null){
            return new Result(false,"用户名称不存在!","");
        }
        //将输入的密码进行加密再匹配
        SimpleHash md5 = new SimpleHash("MD5", user.getUserpassword(), user1.getUserphone(), 5);
        String s = md5.toBase64();
        System.out.println(s);
        if(user1.getUserpassword().equals(s)){
            return new Result(true,"登录成功",user1);
        }else{
            return new Result(false,"密码错误","");
        }
    }

    public User getuser(User user) {
        return userMapper.getuser(user);
    }

    public List<User> getuserbyid() {
        return userMapper.getuserbyid();
    }
}
