package io.renren.customer.CustomerWebsocket;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.customer.entity.ChatMsg;
import io.renren.customer.service.CustomerService;
import io.renren.customer.utils.SpringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/stu/chat/{name}")
public class CustomerWebsocket {
    @Autowired
    private CustomerService customerService;

    private static final Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("name") String name, Session session) {
        System.out.println("有新的连接,ID："+name );
        add(name, session);
        System.out.println("当前在线用户数："+clients.size() );
        //每次连线时，查询离线期间的未读消息。发送给前端
        CustomerService chatService = SpringUtils.getBean(CustomerService.class);
        List<ChatMsg> unReadList = chatService.findUnRead(name);
        if(unReadList!=null){
            for (ChatMsg chatMessage : unReadList) {
                RabbitTemplate rabbitTemplate = SpringUtils.getBean(RabbitTemplate.class);
                rabbitTemplate.convertAndSend("chatExchange","",chatMessage);
            }
        }
    }

    /**
     * 发送消息，前端将消息转成json字符串，后端转成对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ChatMsg msg = mapper.readValue(message, ChatMsg.class);
            if ("1".equals(msg.getSendType())) {
                // 广播
                sendMessageAll(msg.getMsg(), msg.getSendUser());
            } else if ("2".equals(msg.getSendType())) {
                // 群聊
                sendMessageGroup(msg.getMsg(), msg.getMsg());
            } else if ("3".equals(msg.getSendType())) {
                msg.setIsRead("0");
                RabbitTemplate rabbitTemplate = SpringUtils.getBean(RabbitTemplate.class);
                rabbitTemplate.convertAndSend("chatExchange","",msg);
                //不管收消息的用户是否需在线，先持久化保存消息入库，状态为未
                new Thread(() -> {
                    CustomerService chatMsgService = SpringUtils.getBean(CustomerService.class);
                    if (chatMsgService == null) {
                        System.out.println("保存聊天消息出错：chatMsgService为null");
                        return;
                    }
                    chatMsgService.addMsg(msg);
                }).start();
            }
        } catch (Exception e) {
            System.out.println("发送消息出错");
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(@PathParam("name") String name, Session session) {
        System.out.println(name+"：退出聊天" );
        remove(name);
        System.out.println("当前在线用户数："+clients.size());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            System.out.println("退出发生异常： "+e.getMessage() );
        }
        System.out.println("连接出现异常： "+throwable.getMessage() );
    }

    /**
     * 新增一个连接
     */
    public static void add(String name, Session session) {
        if (!name.isEmpty() && session != null) {
            clients.put(name, session);
        }
    }

    /**
     * 删除一个连接
     */
    public static void remove(String name) {
        if (!name.isEmpty()) {
            clients.remove(name);
        }
    }

    /**
     * 获取在线人数
     */
    public static int count() {
        return clients.size();
    }

    /**
     * 广播
     *
     * @param message  发送的消息
     * @param username 发送人
     */
    public static void sendMessageAll(String message, String username) {
        System.out.println("广播消息:"+message );
        clients.forEach((key, session) -> {
            if (!username.equals(key)) {
                RemoteEndpoint.Async remote = session.getAsyncRemote();
                if (remote == null) {
                    return;
                }
                remote.sendText(message);
            }
        });
    }

    /**
     * 群聊
     *
     * @param message  发送的消息
     * @param username 发送人
     */
    public static void sendMessageGroup(String message, String username) {
        System.out.println("群发消息");
        clients.forEach((key, session) -> {
            if (!username.equals(key)) {
                RemoteEndpoint.Async remote = session.getAsyncRemote();
                if (remote == null) {
                    return;
                }
                remote.sendText(message);
            }
        });
    }

    /**
     * 单聊
     * @param msg   发送的消息
     */
    public static void sendMessage(ChatMsg msg) {
        Session session = clients.get(msg.getAcceptUser());
        if (session != null) {
            //收消息用户存在，推送给前端
            final RemoteEndpoint.Async basic = session.getAsyncRemote();
            if (basic == null) {
                return;
            }
            String s = JSONArray.toJSON(msg).toString();
            basic.sendText(s);
            //修改消息状态为已读
            msg.setIsRead("1");
            CustomerService chatMsgService = SpringUtils.getBean(CustomerService.class);
            new Thread(() -> {
                if (chatMsgService == null) {
                    System.out.println("保存聊天消息出错：chatMsgService为null");
                    return;
                }
                chatMsgService.update(msg);
            }).start();
        } else {
            //用户session不存在，可能离线或者连接的不是本台服务器
            return;
        }
    }

}
