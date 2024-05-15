package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
    public final static String roleValue = "MANAGER";
    @JsonIgnore
    private Integer id;
    private String accountName;
    @JsonIgnore
    private String password;
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
