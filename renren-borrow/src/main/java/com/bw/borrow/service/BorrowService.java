package com.bw.borrow.service;

import com.bw.borrow.mapper.BorrowMapper;
import io.renren.common.borrow.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BorrowService {
    @Autowired
    BorrowMapper borrowMapper;
    @Transactional
    public void add(Borrow borrow) {
        borrowMapper.add(borrow);
        if(borrow.getHousename()!=null){
            borrowMapper.addPawn(borrow);
        }
    }
}
