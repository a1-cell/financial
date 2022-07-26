package io.renren.modules.check.mapper;


import io.renren.common.borrow.Borrow;
import io.renren.common.check.Content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CheckMapper {
//    @Select("select * from tb_borrow")
    List<Content> list();
    @Select("select a.borrow_id borrowId,a.borrow_name borrowName,b.username borrowRen,a.borrow_money borrowMoney,a.interest_rate interestRate,a.periods,a.back_way backWay,a.borrow_time borrowTime,a.borrow_status borrowStatus,a.behoof,a.userid,b.userphone,a.card_url1 cardUrl1,a.card_url2 cardUrl2,.a.pid,a.tid,a.ttid from tb_borrow a LEFT JOIN users b on a.userid=b.userid \n" +
            "LEFT JOIN product c on a.pid=c.id")
    List<Borrow> blist();

}
