package io.renren.customer.mapper;

import io.renren.customer.entity.ChatMsg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CustomerMapper {
    @Insert("insert into th_chat_info set msg=#{msg},msg_type=#{msgType},send_type=#{sendType},send_time=#{sendTime},send_user=#{sendUser},accept_user=#{acceptUser},is_read=#{isRead}")
    void insert(ChatMsg message);

    @Update("update th_chat_info set is_read='1' where send_user=#{sendUser} and accept_user=#{acceptUser} and send_time=#{sendTime}")
    void updateStatus(ChatMsg message);

    @Select("select msg,msg_type msgType,send_type sendType,send_time sendTime,send_user sendUser,accept_user acceptUser,is_read isRead from th_chat_info where accept_user=#{acceptUser} and is_read='0'")
    List<ChatMsg> findUnread(String name);
}
