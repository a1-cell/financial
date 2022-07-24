package com.renren.service;

import com.renren.mapper.ProductMapper;
import com.renren.pojos.Product;
import io.renren.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired(required = false)
    private ProductMapper mapper;

    /**
     * 产品添加
     * @param product
     * @return
     */
    public boolean addProduct(Product product) {
        mapper.addProduct(product);
        return new Result().success();
    }

    /**
     * 查询所有产品信息
     * @return
     */
    public List<Product> getList() {
        return mapper.getList();
    }
}
