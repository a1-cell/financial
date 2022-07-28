package com.bw.user.mapper;

import io.renren.common.userEnttiy.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
   // @Select("select * from user")
    List<User> userList();
    @Insert("insert into users set username=#{username},userpassword=#{userpassword},usertype=1,userphone=#{userphone},name=#{name},money=0,usernum=20,realstatue=0,companystatue=0,statue=1,creattime=now()")
    void addUser(User user);
    @Select("select count(*) from users where username=#{username}")
    int checkUserName(String username);
    @Select("select userid,username,userpassword,money,userphone from users where username=#{username}")
    User findByUserName(String username);
}
