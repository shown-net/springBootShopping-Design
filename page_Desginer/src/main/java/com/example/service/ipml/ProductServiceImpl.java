package com.example.service.ipml;

import com.example.mapper.ProductMapper;
import com.example.pojo.Product;
import com.example.pojo.Result;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public Boolean getBy_Productname(Product product) {
        //先查询重复商品名的数量
        Integer count = productMapper.nameCount(product.getName());
        //不存在重复商品名，执行插入操作
        if (count == 0) {
            productMapper.add_Product(product);
            return Boolean.TRUE;
        }
        //返回错误bool值
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Product getInfoBy_name(String accountName) {
        //不返回salesMan_ID和字段
        return productMapper.InfoSelect(accountName);
    }

    @Override
    public List<Product> getInfoByUser(List<String> kindList) {
        return productMapper.getInfoByUser(kindList);
    }

    @Override
    public List<Product> getInfoSalesMan(Integer salesManID) {
        return productMapper.getInfoBySalesMan(salesManID);
    }

    @Override
    public Result getInfoByproductID(Integer productID) {
        Product product = productMapper.getInfoByproductID(productID);
        if (product != null) {
            return Result.success(product);
        } else {
            return Result.error("不存在该商品");
        }
    }

    @Override
    public void UpdateInfoBy_ID(Product product) {
        productMapper.InfoUpdate(product);
    }

    @Override
    public void deleteProductBy_ID(Integer ID) {
        productMapper.deleteProduct(ID);
    }


}
