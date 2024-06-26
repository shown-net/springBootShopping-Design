package com.example.pojo;

import com.example.anno.State;
import jdk.jfr.Unsigned;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private Long id;
    private Integer salesManID;
    private String kind;
    private Integer quantity;
    private String name;
    private Float price;
    private String imageUrl;
    @State
    private String state;

}
