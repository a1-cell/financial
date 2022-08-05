package com.bw.borrow.mapper;

import io.renren.common.borrow.Borrow;
import io.renren.common.es.BackList;
import io.renren.common.product.Product;
import io.renren.common.userEnttiy.User;
import io.renren.common.borrow.Rule;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BorrowMapper {
   // @Insert("insert into tb_borrow set borrow_name=#{borrowName},borrow_ren=#{borrowRen},borrow_money=#{borrowMoney},interest_rate=#{interestRate},periods=#{periods},back_way=#{backWay},borrow_time=now(),borrow_status=1,behoof=#{behoof},userid=#{userid},card_url1=#{cardUrl1},card_url2=#{cardUrl2},pid=#{pid},tid=#{tid},ttid=#{ttid},companyname=#{companyname},companyaddress=#{companyaddress},companyphone=#{companyphone}")
    void add(Borrow borrow);
    @Insert("insert into pawn set borrow_id=#{borrowId},housename=#{housename},houseaddress=#{houseaddress},houseUrl=#{houseUrl}")
    void addPawn(Borrow borrow);

    @Select("select id,borrow_id borrowId,product_name productName,capital_count capitalCount,status from product")
    List<Product> getList();
    @Select("select userid,username,userpassword,money,userphone from users where userid=#{userid}")
    User selectUsersById(Long userid);
    @Update("update product set capital_count=capital_count-#{capitalCount} where id=#{id}")
    void updatePro(Product product);
    @Select("select b.borrow_name borrowName,b.borrow_ren borrowRen,b.borrow_money borrowMoney,b.behoof,p.housename from tb_borrow b left join pawn p on b.borrow_id=p.borrow_id")
    List<Borrow> getlist();
    void addrule(Rule rule);
    @Select("select * from rule where name=#{name}")
    Rule getrule(String name);
    @Update("update rule set statue=0 where name=#{name}")
    void norul(String name);
    @Select("select * from rule where statue=1")
    List<Rule> getRuleList();
    @Select("select * from product")
    List<Product> getProductList();
    @Select("select * from blacklist")
    List<BackList> blacklist();
}
