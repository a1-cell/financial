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

    public boolean addProduct(Product product) {
        mapper.addProduct(product);
        return new Result().success();
    }

    public List<Product> getList() {
        return mapper.getList();
    }
}
