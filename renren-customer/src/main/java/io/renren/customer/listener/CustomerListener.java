package io.renren.customer.listener;

import io.renren.customer.CustomerWebsocket.CustomerWebsocket;
import io.renren.customer.entity.ChatMsg;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "chatServer1")
public class CustomerListener {
    @RabbitHandler
    public void receiveMessage(ChatMsg chatMsg){
//        System.out.println(chatMsg);
        CustomerWebsocket.sendMessage(chatMsg);
    }
}
