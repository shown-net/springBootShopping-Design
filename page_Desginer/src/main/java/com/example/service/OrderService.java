package com.example.service;


import com.example.pojo.Order;
import com.example.pojo.Product;
import com.example.pojo.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    void add_Order(List<Order> Order);

    List<Order> getOrderByUpdateTime(LocalDateTime localDateTime);

    List<Order> getInfoBy_userId(Integer userID);

    List<Order> getInfoBy_salesManId(Integer salesManID);

    List<Order> getStatisticsBySalesMan(Integer salesManID);

    void delete_Order(List<Integer> id_List);

    List<Product> getStatisticsByManager();
}
