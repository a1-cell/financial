package com.bw.bank.mapper;

import com.bw.bank.entity.Bank;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BankMapper {

    @Insert("insert into banktable set userid=1,bankname=#{bankname},bankidcard=#{bankidcard},bankphone=#{bankphone},creattime=now()")
    void creatUser(Bank bank);
}
