package com.renren.mapper;

import io.renren.common.product.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Insert("insert into product set status=0,product_name=#{productName},product_type=#{productType},product_rate=#{productRate},borrow_count=#{borrowCount},return_way=#{returnWay},create_time=now(),capital_count=#{capitalCount}")
    void addProduct(Product product);
    @Select("select id,product_name productName,product_type productType,product_rate productRate,borrow_count borrowCount,return_way returnWay,create_time createTime,capital_count capitalCount from product")
    List<Product> getList();
}
