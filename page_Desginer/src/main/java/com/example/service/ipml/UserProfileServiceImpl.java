package com.example.service.ipml;

import com.example.mapper.ProductMapper;
import com.example.mapper.UserMapper;
import com.example.mapper.UserProfileMapper;
import com.example.pojo.Order;
import com.example.pojo.Product;
import com.example.pojo.UserProfile;
import com.example.pojo.dto.browseTime_Kind_Dto;
import com.example.service.OrderService;
import com.example.service.UserProfileService;
import com.example.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    OrderService orderService;
    @Autowired
    UserProfileMapper userProfileMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ProductMapper productMapper;

    @Override
    public void setUserProfile(Integer accountID) {
        userProfileMapper.setUserProfile(accountID);
    }

    @Override
    public void updateUserProfile(Integer accountID) throws JsonProcessingException {
        UserProfile userProfile = userProfileMapper.getUserProfile(accountID);
        //当未被录入用户给画像的订单超过数量5时,再进行更新
        //获取未被录入用户给画像的订单数据
        List<Order> orderList = orderService.getOrderByUpdateTime(userProfile.getUpdateTime());
        if (orderList.size() < 5) {
            return;
        }
        //json格式字符串反序列化处理
        ObjectMapper objectMapper = new ObjectMapper();
        // 商品类别偏好(种类->购买的总数量)
        Map<String, Integer> preferredCategories = objectMapper.readValue(userProfile.getPreferredCategories(), new TypeReference<>() {
        });
        // 各类别总消费额
        Map<String, Integer> pricePerCategory = objectMapper.readValue(userProfile.getPricePerCategory(), new TypeReference<>() {
        });
        // 各类别总消费数量
        Map<String, Integer> quantityPerCategory = objectMapper.readValue(userProfile.getQuantityPerCategory(), new TypeReference<>() {
        });
        for (Order order : orderList) {
            preferredCategories.merge(order.getKind(), 1, Integer::sum);
            pricePerCategory.merge(order.getKind(), order.getPrice(), Integer::sum);
            quantityPerCategory.merge(order.getKind(), order.getQuantity(), Integer::sum);
        }
        // 商品价格偏好(种类->平均购买价格)
        Map<String, Double> preferredAveragePrice = new HashMap<>();
        for (Map.Entry<String, Integer> entry : pricePerCategory.entrySet()) {
            String kind = entry.getKey(); // 获取条目的键（类别）
            Integer priceSum = entry.getValue(); // 获取条目的值（总价格）
            double roundedValue = Math.round(((double) priceSum / quantityPerCategory.get(kind)) * 10.0) / 10.0;
            //平均购买价格最多只有一位小数
            preferredAveragePrice.put(kind, roundedValue);
        }
        //json序列化
        String preferredKindRes = objectMapper.writeValueAsString(preferredCategories);
        String priceRes = objectMapper.writeValueAsString(pricePerCategory);
        String quantityRes = objectMapper.writeValueAsString(quantityPerCategory);
        String averagePriceRes = objectMapper.writeValueAsString(preferredAveragePrice);
        userProfileMapper.updateUserProfile(accountID, preferredKindRes, priceRes, quantityRes, averagePriceRes);
    }

    //返回推荐的商品列表
    @Override
    public List<Product> getUserProfile(Integer accountID) throws JsonProcessingException {
        UserProfile userProfile = userProfileMapper.getUserProfile(accountID);
        //json格式字符串反序列化处理
        ObjectMapper objectMapper = new ObjectMapper();
        // 商品类别偏好(种类->购买的订单总数量+0.1*浏览时间)
        Map<String, Integer> preferredCategories = objectMapper.readValue(userProfile.getPreferredCategories(), new TypeReference<>() {
        });
        //商品类别浏览时间计算
        List<browseTime_Kind_Dto> objectList = userMapper.getBrowserTimeGroupByKind(accountID);
        for (browseTime_Kind_Dto object : objectList) {
            preferredCategories.merge(object.getKind(), object.getBrowseTime() / 10, Integer::sum);
        }
        //最喜爱的5个类别  列表
        List<String> top5Categories = preferredCategories.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // 按值降序排序
                .map(Map.Entry::getKey) // 提取键
                .limit(5) // 限制结果为前5个
                .toList(); // 收集到List中
        List<Product> productList = productMapper.getInfoByUser(top5Categories);
        // 商品价格偏好(种类->平均购买价格)
        Map<String, Double> preferredAveragePrice = objectMapper.readValue(userProfile.getPreferredAveragePrice(), new TypeReference<>() {
        });
        // 创建一个Map来存储每个类别的商品列表
        Map<String, List<Product>> productsByCategory = productList.stream()
                .collect(Collectors.groupingBy(Product::getKind));
        // 创建结果列表
        List<Product> closestProducts = new ArrayList<>();
        for (Map.Entry<String, List<Product>> entry : productsByCategory.entrySet()) {
            String category = entry.getKey();
            List<Product> categoryProducts = entry.getValue();

            // 获取该类别的平均消费
            Double averageConsumption = preferredAveragePrice.getOrDefault(category, 0.0);

            // 找到最接近平均消费的商品
            Optional<Product> closestProductOptional = categoryProducts.stream()
                    .min(Comparator.comparing(p -> Math.abs(p.getPrice() - averageConsumption)));

            // 如果找到最接近的商品，则添加到结果列表中
            closestProductOptional.ifPresent(closestProducts::add);
        }
        return closestProducts;
    }
}
