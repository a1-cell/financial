package com.bw.bank.service;

import com.bw.bank.entity.Bank;
import com.bw.bank.entity.CreateUserEntity;
import com.bw.bank.entity.RequestEntity;
import com.bw.bank.mapper.BankMapper;
import com.bw.bank.utils.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class BankService {

    @Autowired
    BankMapper bankMapper;

    public Map register(RequestEntity requestEntity) {
        Map<Object, Object> map = new HashMap<>();
        //验签

        //验签成功
        map.put("data",requestEntity.getReqData());
        return map;
    }

    public void createUser(Bank bank) {
       bankMapper.creatUser(bank);
    }
}
