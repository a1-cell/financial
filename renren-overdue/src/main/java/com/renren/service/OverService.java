package com.renren.service;

import com.renren.mapper.OverMapper;
import io.renren.common.ocerdue.Overdue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverService {
    @Autowired
    private OverMapper mapper;

    public List<Overdue> getList() {
        return mapper.getList();
    }
}
