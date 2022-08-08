package com.aibank.mapper;

import com.aibank.entity.CreateUserEntity;
import com.aibank.entity.Platform;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface BankMapper {

    @Select("select id,platform_no platformNo,platform_name platformName from tb_platform where platform_no=#{platformNo}")
    Platform findPlatform(String platformNo);

    @Select("select secret from tb_secret where platform_no=#{platformNo}")
    List<String> findSecret(String platformNo);

    @Insert("insert into tb_user set platform_no=#{platformNo},realname=#{realname},idcard=#{idcard},bank_card=#{bankCard},phone=#{phone},user_role=#{role},platform_user_no=#{platformUserNo}")
    int createUser(CreateUserEntity user);
}
