package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private Integer id;
    private Integer userID;
    private Integer productID;
    private String kind;
    private String productName;
    private Integer price;
    private Integer quantity;
    private String buyTime;
}
