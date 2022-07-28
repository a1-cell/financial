package com.bw.borrow.service;

import com.bw.borrow.mapper.BorrowMapper;
import io.renren.common.borrow.Borrow;
import io.renren.common.borrow.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
