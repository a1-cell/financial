package io.renren.modules.check.service;

import io.renren.common.borrow.Borrow;
import io.renren.common.check.Content;
import io.renren.modules.check.mapper.CheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckService {

    @Autowired(required = false)
    private CheckMapper checkMapper;


    public List<Content> list() {
        return checkMapper.list();
    }

    public List<Borrow> borrowList() {
        return checkMapper.blist();
    }
}
