package com.example.aspect;

import com.example.anno.RoleCheck;
import com.example.utils.ThreadLocalUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@Aspect
@Component
public class RoleCheckAspect {

    @Before("@annotation(roleCheck)")
    public void doBefore(JoinPoint joinPoint, RoleCheck roleCheck) throws AccessDeniedException {
        // 获取方法上定义的角色
        String[] requiredRoles = roleCheck.value();
        Map<String, Object> map = ThreadLocalUtil.get();
        //当前角色
        String role = (String) map.get("role");
        // 执行校验逻辑
        for (String requiredRole : requiredRoles) {
            if (role.equals(requiredRole)) {
                //只要该角色存在于需要权限的集合
                return;
            }
        }
        //该角色不存在需要权限的集合，抛出异常
        throw new AccessDeniedException("用户权限非法");
    }
}
