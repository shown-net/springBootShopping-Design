package com.example.service;


import com.example.pojo.OperationLog;

import java.util.List;

public interface OperationLogService {
    public void setOperationLog(OperationLog operationLog);
    public List<OperationLog> getOperationLog(String accountRole,Integer accountID);
}
