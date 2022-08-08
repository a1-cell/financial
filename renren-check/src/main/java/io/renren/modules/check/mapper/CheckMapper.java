package io.renren.modules.check.mapper;


import io.renren.common.borrow.Borrow;
import io.renren.common.check.Content;
import io.renren.common.check.Creditor;
import io.renren.common.check.Pro;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CheckMapper {
//    @Select("select * from tb_borrow")
    List<Content> list();
    @Select("select a.borrow_id borrowId,a.cstatus,a.borrow_name borrowName,a.borrow_ren borrowRen,a.borrow_money borrowMoney,a.interest_rate interestRate,a.periods,a.back_way backWay,a.borrow_time borrowTime,a.borrow_status borrowStatus,a.behoof,a.userid,b.userphone,a.card_url1 cardUrl1,a.card_url2 cardUrl2,.a.pid,a.tid,a.ttid,a.cre_status creStatus,c.userid uid,b.username uname from tb_borrow a  LEFT JOIN product c on a.pid=c.id LEFT JOIN users b on c.userid=b.userid")
    List<Borrow> blist();
    @Select("select a.borrow_id borrowId,a.cstatus,a.borrow_name borrowName,a.borrow_ren borrowRen,a.borrow_money borrowMoney,a.interest_rate interestRate,a.periods,a.back_way backWay,a.borrow_time borrowTime,a.borrow_status borrowStatus,a.behoof,a.userid,b.userphone,a.card_url1 cardUrl1,a.card_url2 cardUrl2,.a.pid,a.tid,a.ttid,a.cre_status creStatus,c.userid uid,b.username uname from tb_borrow a  LEFT JOIN product c on a.pid=c.id LEFT JOIN users b on c.userid=b.userid where c.userid=#{userid}")
    List<Borrow> cerList(Integer userid);
    @Select("select a.borrow_id borrowId,a.cstatus,a.borrow_name borrowName,a.borrow_ren borrowRen,a.borrow_money borrowMoney,a.interest_rate interestRate,a.periods,a.back_way backWay,a.borrow_time borrowTime,a.borrow_status borrowStatus,a.behoof,a.userid,b.userphone,a.card_url1 cardUrl1,a.card_url2 cardUrl2,.a.pid,a.tid,a.ttid,a.cre_status creStatus,c.userid uid,b.username uname from tb_borrow a  LEFT JOIN product c on a.pid=c.id LEFT JOIN users b on c.userid=b.userid where a.borrow_id=#{borrowId}")
    Borrow getCreList(Borrow borrow1);
    @Insert("insert into tb_credutor set title=#{title},tran_ren=#{tranRen},money=#{money},rate_xi=#{rateXi},tran_money=#{tranMoney},rate=#{rate},dai_huan=#{daiHuan},credutor_status=#{credutorStatus},borrow_id=#{borrowId}")
    void addCre(Creditor credutor);
    @Update("update tb_borrow set cstatus='逾期中' where borrow_id=#{borrowId}")
    void updateBorrow(Borrow borrow);
    @Update("update tb_borrow set cre_status=1 where borrow_id=#{borrowId}")
    void updateCre(Creditor credutor);
    @Select("select a.credutor_id credutorId,a.title,a.tran_ren tranRen,a.money,a.rate_xi rateXi,a.tran_money tranMoney,a.rate,a.dai_huan daiHuan,a.credutor_status credutorStatus,a.borrow_id borrowId,b.borrow_ren borrowRen from tb_credutor a left join tb_borrow b on a.borrow_id=b.borrow_id")
    List<Creditor> credutorList();
    @Select("select a.credutor_id credutorId,a.title,a.tran_ren tranRen,a.money,a.rate_xi rateXi,a.tran_money tranMoney,a.rate,a.dai_huan daiHuan,a.credutor_status credutorStatus,a.borrow_id borrowId,b.borrow_ren borrowRen from tb_credutor a left join tb_borrow b on a.borrow_id=b.borrow_id where a.credutor_id=#{credutorId}")
    Creditor getPrice(Integer credutorId);
    @Select("select borrow_id borrowId from tb_credutor where credutor_id=#{crectorId}")
    Integer getBorrowId(Integer creductorId);
    @Update("update tb_borrow set cre_status=0 where borrow_id=#{borrowId}")
    void updateZhai(Integer borrowId);
    @Select("select pid from tb_borrow where borrow_id=#{borrowId}")
    Integer getPid(Integer borrowId);
    @Update("update tb_borrow set pid=#{id} where borrow_id=#{borrowId}")
    void updatePro(Pro pro);
    @Delete("delete from tb_credutor where credutor_id=#{credutorId}")
    void delCre(Integer creductorId);
    @Select("select id from product where userid=#{uid}")
    Integer getPro(Pro pro);
    @Select("select pid from tb_borrow where borrow_id=#{borrowId}")
    Integer getOldPid(Pro pro);
    @Update("update product set capital_count=capital_count+#{qq} where id=#{id}")
    void updateOldAddqq(Pro pro);
    @Update("update product set capital_count=capital_count-#{qq} where id=#{id}")
    void updateDelqq(Pro pro);
    @Select("select a.borrow_id borrowId,a.cstatus,a.borrow_name borrowName,a.borrow_ren borrowRen,a.borrow_money borrowMoney,a.interest_rate interestRate,a.periods,a.back_way backWay,a.borrow_time borrowTime,a.borrow_status borrowStatus,a.behoof,a.userid,b.userphone,a.card_url1 cardUrl1,a.card_url2 cardUrl2,.a.pid,a.tid,a.ttid,a.cre_status creStatus,c.userid uid,b.username uname from tb_borrow a  LEFT JOIN product c on a.pid=c.id LEFT JOIN users b on c.userid=b.userid where a.borrow_id=#{borrowId}")
    Borrow findBorrowList(Integer borrowId);
}
