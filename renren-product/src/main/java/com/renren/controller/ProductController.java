package com.renren.controller;

import io.renren.common.product.Product;
import com.renren.service.ProductService;
import io.renren.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1")
public class ProductController {
    @Autowired
    private ProductService service;


    @PostMapping("/add")
    public boolean addProduct(@RequestBody Product product){
        return service.addProduct(product);
    }

    @GetMapping("/list")
    public Result getProductList(){
        List<Product> list=service.getList();
        return new Result(true,"查询成功",list);
    }
}
