package com.example.mapper;

import com.example.pojo.Product;
import com.example.pojo.SalesMan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    //检查是否有重复商品名，返回重复的数量
    @Select("select count(id) from product where name=#{name}")
    public Integer nameCount(String name);

    //判断该商品是否额能正常销售
    @Select("select count(id) from product where id=#{productID} and state='有货'")
    public Integer getState(Integer productID);

    //插入新增的商品信息
    @Insert("insert into product (salesMan_ID,kind,name,price,image_Url,quantity,state) " +
            "values(#{salesManID},#{kind},#{name},#{price},#{imageUrl},#{quantity},#{state})")
    public void add_Product(Product product);

    //根据提供的商品名返回商品数据
    @Select("select * from product where name=#{name}")
    public Product InfoSelect(String name);

    //消费者视角返回所有商品信息
    public List<Product> getInfoByUser(List<String> kindList);

    @Select("select * from product where salesMan_ID=#{salesManID}")
    List<Product> getInfoBySalesMan(Integer salesManID);

    //更新商品信息(根据商品ID)
    @Update("update product set name=#{name},kind=#{kind},price=#{price},quantity=#{quantity},state=#{state}  " +
            " where id=#{id} and salesMan_ID=#{salesManID}")
    public void InfoUpdate(Product product);

    //移除商品信息(根据商品ID)
    @Delete("delete from product where id=#{ID}")
    public void deleteProduct(Integer ID);

    @Select("select * from product where id=#{productID}")
    Product getInfoByproductID(Integer productID);
}
