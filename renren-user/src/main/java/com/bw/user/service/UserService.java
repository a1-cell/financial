package com.bw.user.service;

import com.bw.user.mapper.UserMapper;
import io.renren.common.userEnttiy.User;
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
}
