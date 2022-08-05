package io.renren.note.mapper;

import io.renren.common.borrow.Borrow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MqMapper {
    @Update("update tb_borrow set cstatus='逾期中' where borrow_id=#{borrowId}")
    void updateBorrow(Borrow borrow);
}
