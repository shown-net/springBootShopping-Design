package com.example.mapper;

import com.example.pojo.OperationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface OperationLogMapper {
    @Insert("insert into operation_log(operationRes,operatorTime, accountRole, accountID, content, ipAddress)" +
            "values (#{operationRes},now(),#{accountRole},#{accountID},#{content},#{ipAddress})")
    void setOperationLog(OperationLog operationLog);

    @Select("select id, operationRes, content, ipAddress, operatorTime from operation_log where accountRole=#{accountRole} and accountID=#{accountID}")
    List<OperationLog> getOperationLog(String accountRole, Integer accountID);
}
