package com.example.service.ipml;

import com.example.mapper.OperationLogMapper;
import com.example.pojo.OperationLog;
import com.example.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    @Autowired
    OperationLogMapper operationLogMapper;
    @Override
    public void setOperationLog(OperationLog operationLog) {
        operationLogMapper.setOperationLog(operationLog);
    }

    @Override
    public List<OperationLog> getOperationLog(String accountRole,Integer accountID) {
        return operationLogMapper.getOperationLog(accountRole,accountID);
    }
}
