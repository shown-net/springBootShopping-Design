package com.example.anno;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.example.validation.StateValidation;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {StateValidation.class}   //指定提供校验规则的类
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface State {
    //校验失败的提示信息
    String message() default "state属性的值违法";

    //指定分组
    Class<?>[] groups() default {};

    //负载    获取state的附加信息
    Class<? extends Payload>[] payload() default {};
}
