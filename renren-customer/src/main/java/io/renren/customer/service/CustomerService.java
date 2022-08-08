package io.renren.customer.service;

import io.renren.customer.entity.ChatMsg;
import io.renren.customer.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired(required = false)
    private CustomerMapper chatMapper;

    public void addMsg(ChatMsg msg) {
        //将消息入库保存
        //insert添加
        chatMapper.insert(msg);
        System.out.println("多线程未读消息入库");
    }

    public void update(ChatMsg msg) {
        //修改消息的状态
        //根据msg里存储的发送人ID、收信人ID、发送时间修改状态
        chatMapper.updateStatus(msg);
        System.out.println("多线程修改消息状态");
    }

    public List<ChatMsg> findUnRead(String name) {
        System.out.println("查询未读消息");
        //根据name也就是用户ID，查询所有未读消息
        List<ChatMsg> unread = chatMapper.findUnread(name);
        return unread;
    }

}
