package com.example.aspect;

import com.example.anno.OperationLogRecord;
import com.example.pojo.OperationLog;
import com.example.pojo.Result;
import com.example.service.OperationLogService;
import com.example.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    OperationLogService operationLogService;

    @Around("@annotation(operationLogRecord)")
    public Result doAround(ProceedingJoinPoint joinPoint, OperationLogRecord operationLogRecord) throws Throwable {
        // 控制层方法执行前的逻辑
        Result result = (Result) joinPoint.proceed(); // 执行目标方法
        // 获取操作内容
        String content = operationLogRecord.value();
        // ThreadLocalUtil
        Map<String, Object> map = ThreadLocalUtil.get();
        //操作账号角色
        String accountRole = (String) map.get("role");
        //当前账号ID
        Integer accountID = (Integer) map.get("id");
        //IP地址
        String ipAddress = getIpAddress();
        //操作结果
        String operationRes;
        //创建operationLog对象
        if (result == null || result.getCode() == 0) {
            operationRes = "成功";
        } else {
            operationRes = "失败";
        }
        OperationLog operationLog = new OperationLog(null,operationRes, null,
                accountRole, accountID, content, ipAddress);
        operationLogService.setOperationLog(operationLog);
        return result;
    }

    private String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getRemoteAddr();
    }
}
