package com.example.pojo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesMan {
    public final static String roleValue = "SALESMAN";
    private Integer id;
    @NotEmpty
    private String accountName;
    @NotEmpty
    private String password;
    @NotEmpty
    @Email
    private String email;
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
