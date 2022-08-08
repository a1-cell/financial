package com.loans.coupon.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loans.coupon.dao.NoticeMapper;
import com.loans.coupon.pojo.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public void saveOrUpdate(Notice notice) {
        noticeMapper.saveOrUpdate(notice);
    }


    public List<Notice> selectAll() {
        return noticeMapper.selectAll();
    }
}
