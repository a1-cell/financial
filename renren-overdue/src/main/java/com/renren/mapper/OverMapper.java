package com.renren.mapper;

import io.renren.common.ocerdue.Overdue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OverMapper {

    @Select("select oid,userid,tel,overday from overdue")
    List<Overdue> getList();
}
