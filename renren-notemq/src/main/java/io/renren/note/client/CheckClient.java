package io.renren.note.client;

import io.renren.common.borrow.Borrow;
import io.renren.note.mapper.MqMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class CheckClient {
    @Autowired(required = false)
    private MqMapper mqMapper;

    public void updateBorrow(@RequestBody Borrow borrow){
        mqMapper.updateBorrow(borrow);
    }
}
