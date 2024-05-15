package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private Integer id;
    // 用户偏好信息，可以从订单和浏览记录中分析得出
    private String preferredCategories; // 商品类别偏好(Map<String, Integer>)
    private String PricePerCategory; // 各类别总消费额(Map<String, Integer>)
    private String QuantityPerCategory; // 各类别总消费数量(Map<String, Integer>)
    private String preferredAveragePrice; // 各类别总消费平均额(Map<String, Double>)
    private LocalDateTime updateTime;//更新时间
}
