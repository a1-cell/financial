package io.renren.pay.service;

import io.renren.common.borrow.Borrow;
import io.renren.pay.mapper.ApliMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApliService {

    @Autowired(required = false)
    private ApliMapper apliMapper;

    public Borrow findBorrowList(Integer borrowId) {
        return apliMapper.findBorrowList(borrowId);
    }

    public List<Borrow> findAll() {
        return apliMapper.findAll();
    }
}
