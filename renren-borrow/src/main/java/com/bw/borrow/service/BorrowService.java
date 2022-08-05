package com.bw.borrow.service;

import com.bw.borrow.mapper.BorrowMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.renren.common.borrow.Borrow;
import io.renren.common.es.BackList;
import io.renren.common.product.Product;
import io.renren.common.result.Result;
import io.renren.common.userEnttiy.User;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import io.renren.common.borrow.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import java.util.List;

@Service
public class BorrowService {
    @Autowired(required = false)
    BorrowMapper borrowMapper;
    @Autowired(required = false)
    private RedissonClient redissonClient;
    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    public void add(Borrow borrow) {
        borrowMapper.add(borrow);
        if(borrow.getHousename()!=null){
            borrowMapper.addPawn(borrow);
        }
    }

    public List<Borrow> getlist() {
        return borrowMapper.getlist();
    }

    public void addrule(Rule rule) {
        borrowMapper.addrule(rule);
    }

    public Rule getrule(String name) {
        return borrowMapper.getrule(name);
    }

    public void norul(String name) {
        borrowMapper.norul(name);
    }

    public List<Rule> getRuleList() {
        return borrowMapper.getRuleList();
    }

    public List<Product> getList() throws JsonProcessingException {
        List<Product> list = borrowMapper.getList();
        HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
        ObjectMapper mapper = new ObjectMapper();
        for (Product product : list) {
            if(product.getStatus()==1){
                Map<Object, Object> map = ops.entries("id" + product.getId());
                map.put("saleNum",String.valueOf(product.getCapitalCount().doubleValue()));
                map.put("lockNum","0.0");
                ops.putAll("id"+product.getId(),map);
            }
        }
        ValueOperations<String, String> ops2 = stringRedisTemplate.opsForValue();
        ops2.set("id",mapper.writeValueAsString(list));
        return list;
    }

    public Result tou(Product product) throws InterruptedException {
        double capitalCount = product.getCapitalCount().doubleValue();
        User user = borrowMapper.selectUsersById(product.getUserid());
        BigDecimal a1 = new BigDecimal(String.valueOf(product.getCapitalCount()));
        BigDecimal a2 = new BigDecimal(String.valueOf(user.getMoney()));
        if(a1.compareTo(a2)>0){
            return new Result(false,"企业或个人资金不足","");
        }
        //添加锁
        RLock lock = redissonClient.getLock("lock"+product.getId());
        boolean b = lock.tryLock(10, 5, TimeUnit.MINUTES);
        Double saleNum=0.0;
        if(b){
            try {
                Map<Object, Object> obj = stringRedisTemplate.opsForHash().entries("id" + product.getId());
                saleNum= Double.parseDouble((String)obj.get("saleNum"));
                Double lockNum=Double.parseDouble((String)obj.get("lockNum"));
                if(1>saleNum){
                    return new Result(false,"资金已经足够","");
                }
                saleNum=saleNum-capitalCount;
                lockNum=lockNum+capitalCount;
                obj.put("saleNum",String.valueOf(saleNum));
                obj.put("lockNum",String.valueOf(lockNum));
                stringRedisTemplate.opsForHash().putAll("id"+product.getId(),obj);
                //修改数据库
                borrowMapper.updatePro(product);
            }finally {
                lock.unlock();
            }
        }else{
            return new Result(false,"其他人正在投标中","");
        }
        return new Result(true,"投标成功","");
    }

    public List<Product> getProductList() {
        return borrowMapper.getProductList();
    }

    public List<BackList> blacklist() {
        return borrowMapper.blacklist();
    }

//    public Result tou(Borrow borrow) {
//        //通过编号查询出投标人的金额
//        Integer userid = borrow.getUserid();
//        User user=borrowMapper.selectUsersById(userid);
//
//        RLock lock = redissonClient.getLock("lock" + borrow.getBorrowId());
//
//
//
//
//
//        return null;
//    }
}
