package com.bw.bank.service;

import com.bw.bank.entity.RequestEntity;
import com.bw.bank.mapper.BankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
}
