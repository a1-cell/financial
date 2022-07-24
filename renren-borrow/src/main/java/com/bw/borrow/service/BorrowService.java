package com.bw.borrow.service;

import com.bw.borrow.mapper.BorrowMapper;
import io.renren.common.borrow.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowService {
    @Autowired
    BorrowMapper borrowMapper;

    public void add(Borrow borrow) {
        borrowMapper.add(borrow);
    }
}
