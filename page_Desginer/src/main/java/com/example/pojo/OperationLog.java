package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog {
    private Long id;
    private String operationRes;//操作结果(成功|失败)
    private LocalDateTime operatorTime;//触发这个操作的时间
    private String accountRole;//操作人角色
    private Integer accountID; // 操作人账号
    private String content; // 操作内容
    private String ipAddress; // IP地址
}
