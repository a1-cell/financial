package com.bw.borrow.service;

import com.bw.borrow.mapper.BorrowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowService {
    @Autowired
    BorrowMapper borrowMapper;
}
