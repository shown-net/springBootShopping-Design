package com.example.service.ipml;

import com.example.mapper.OrderMapper;
import com.example.mapper.ProductMapper;
import com.example.pojo.Order;
import com.example.pojo.Product;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void add_Order(List<Order> orderList) {
        orderList.forEach(orderItem -> {
            if (productMapper.getState(orderItem.getProductID()) > 0) {
                //只有销售状态正常的商品，才执行单个订单插入操作
                orderMapper.add_Order(orderItem);
            }
        });
    }

    @Override
    public List<Order> getOrderByUpdateTime(LocalDateTime localDateTime) {
        return orderMapper.getOrderByUpdateTime(localDateTime);
    }

    @Override
    public List<Order> getInfoBy_userId(Integer userID) {
        return orderMapper.getInfoByUser(userID);
    }

    @Override
    public List<Order> getInfoBy_salesManId(Integer salesManID) {
        return orderMapper.getInfoBySaleMan(salesManID);
    }

    @Override
    public List<Order> getStatisticsBySalesMan(Integer salesManID) {
        return orderMapper.getStatisticsBySalesMan(salesManID);
    }

    @Override
    public void delete_Order(List<Integer> id_List) {
        orderMapper.deleteOrder(id_List);
    }

    @Override
    public List<Product> getStatisticsByManager() {
        return orderMapper.getStatisticsByManager();
    }


}
