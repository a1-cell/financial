package io.renren.pay.mapper;

import io.renren.common.borrow.Borrow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApliMapper {

    @Select("select a.borrow_id borrowId,a.cstatus,a.borrow_name borrowName,a.borrow_ren borrowRen,a.borrow_money borrowMoney,a.interest_rate interestRate,a.periods,a.back_way backWay,a.borrow_time borrowTime,a.borrow_status borrowStatus,a.behoof,a.userid,b.userphone,a.card_url1 cardUrl1,a.card_url2 cardUrl2,.a.pid,a.tid,a.ttid,a.cre_status creStatus,c.userid uid,b.username uname from tb_borrow a  LEFT JOIN product c on a.pid=c.id LEFT JOIN users b on c.userid=b.userid where a.borrow_id=#{borrowId}")
    Borrow findBorrowList(Integer borrowId);
    @Select("select borrow_id borrowId,borrow_name borrowName,borrow_money borrowMoney from tb_borrow")
    List<Borrow> findAll();
}
