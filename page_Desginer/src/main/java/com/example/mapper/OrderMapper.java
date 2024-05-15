package com.example.mapper;

import com.example.pojo.Order;
import com.example.pojo.Product;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    //插入新增的订单信息
    @Insert("insert into orders (user_id,kind,product_ID,product_Name,price,quantity,buy_time) " +
            " values(#{userID},#{kind},#{productID},#{productName},#{price},#{quantity},#{buyTime})")
    public void add_Order(Order orderItem);

    //根据提供的用户id返回用户订单数据 --数据字段起别名
    public List<Order> getInfoByUser(Integer userID);

    //根据提供的销售人员id返回对应商品订单数据 --数据字段起别名
    public List<Order> getInfoBySaleMan(Integer salesManID);

    //返回订单统计数据 --数据字段起别名
    public List<Order> getStatisticsBySalesMan(Integer salesManID);

    //返回所有订单统计数据 根据商品类别分组
    public List<Product> getStatisticsByManager();

    //更新订单信息，删除用户已删除订单
    public void deleteOrder(List<Integer> Id_List);

    @Select("select * from orders where buy_time>=#{localDateTime}")
    public List<Order> getOrderByUpdateTime(LocalDateTime localDateTime);
}
