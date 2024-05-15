package com.example.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleCheck {
    // 定义注解的属性，比如需要检查的角色
    /*普通用户:USER*/
    /*销售人员:SALESMAN*/
    /*管理者:MANAGER*/
    String[] value() default {};
}
