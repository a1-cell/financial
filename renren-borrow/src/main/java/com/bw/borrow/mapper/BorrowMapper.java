package com.bw.borrow.mapper;

import io.renren.common.borrow.Borrow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BorrowMapper {
    @Insert("insert into tb_borrow set borrow_name=#{borrowName},borrow_ren=#{borrowRen},borrow_money=#{borrowMoney},interest_rate=#{interestRate},periods=#{periods},back_way=#{backWay},borrow_time=now(),borrow_status=1,behoof=#{behoof},userid=#{userid},card_url1=#{cardUrl1},card_url2=#{cardUrl2},pid=#{pid},tid=#{tid},ttid=#{ttid}")
    void add(Borrow borrow);
}
