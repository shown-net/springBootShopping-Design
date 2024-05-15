package com.example.service;


import com.example.pojo.Product;
import com.example.pojo.Result;

import java.util.List;

public interface ProductService {

    Boolean getBy_Productname(Product product);


    Product getInfoBy_name(String name);

    List<Product> getInfoByUser(List<String> kindList);

    void UpdateInfoBy_ID(Product product);

    void deleteProductBy_ID(Integer ID);

    List<Product> getInfoSalesMan(Integer salesManID);

    Result getInfoByproductID(Integer productID);
}
