package com.example.controller;

import com.example.anno.OperationLogRecord;
import com.example.anno.RoleCheck;
import com.example.pojo.Result;
import com.example.pojo.Product;
import com.example.pojo.SalesMan;
import com.example.pojo.User;
import com.example.service.ProductService;
import com.example.utils.JwtUtils;
import com.example.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    // 增加新商品信息
    @RoleCheck("SALESMAN")
    @OperationLogRecord("增加新商品信息")
    @PostMapping("/add")
    public Result add(@RequestBody Product product) {
        Map<String, Object> map = ThreadLocalUtil.get();
        product.setSalesManID((Integer) map.get("id"));
        Boolean result = productService.getBy_Productname(product);
        if (result == Boolean.TRUE) {
            return Result.success("增加商品成功");
        } else {
            return Result.error("存在重复商品名，请重试");
        }
    }

    // 查询商品信息 --前端未配置相对查询
//    @PostMapping("/selectInfo")
//    public Result info(@RequestBody String name) {
//        Product product = productService.getInfoBy_name(name);
//        return Result.success(product);
//    }

    // 普通用户根据种类查询商品信息
    @PostMapping("/getProductInfo")
    public Result getProductInfo(@RequestBody List<String> kindList) {
        List<Product> product_List = productService.getInfoByUser(kindList);
        return Result.success(product_List);
    }

    // 根据商品ID查询商品信息
    @GetMapping("/getInfoByProductID")
    public Result getInfoByproductID(@RequestParam Integer productID) {
        return productService.getInfoByproductID(productID);
    }

    // 销售人员根据自己ID查询旗下商品信息
    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("销售人员查询旗下商品信息")
    @PostMapping("/getInfoBySalesMan")
    public Result getInfoBySalesMan() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer salesManID = (Integer) map.get("id");
        List<Product> product_List = productService.getInfoSalesMan(salesManID);
        return Result.success(product_List);
    }

    // 更新商品列表信息
    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("更新商品信息")
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestBody Product product) {
        Map<String, Object> map = ThreadLocalUtil.get();
        product.setSalesManID((Integer) map.get("id"));
        try {
            productService.UpdateInfoBy_ID(product);
            return Result.success("更新商品信息成功");
        } catch (Exception e) {
            return Result.error("更新失败");
        }

    }

    // 删除商品
    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("删除商品数据")
    @PostMapping("/delete")
    public Result deleteInfo(@RequestBody Integer ID) {
        productService.deleteProductBy_ID(ID);
        return Result.success("删除新商品成功");
    }


}
