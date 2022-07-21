package io.renren.modules.check.service;

import io.renren.modules.check.dao.CheckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckService {

    @Autowired(required = false)
    private CheckMapper checkMapper;


}
