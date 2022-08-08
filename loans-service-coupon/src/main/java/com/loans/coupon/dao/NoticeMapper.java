package com.loans.coupon.dao;

import com.loans.coupon.pojo.Notice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoticeMapper {
    @Insert("insert into `notice`(`name`,`content`,`time`,`user`) values('${notice.name}','${notice.content}','${notice.time}','${notice.user}')")
    void saveOrUpdate(@Param("notice") Notice notice);

    @Select("select * from `notice`")
    List<Notice> selectAll();
}
