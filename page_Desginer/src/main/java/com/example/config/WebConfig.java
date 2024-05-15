package com.example.config;

import com.example.intercepter.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@EnableAspectJAutoProxy
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置白名单
        List<String> while_List = new ArrayList<>();
        while_List.add("/product/getProductInfo");
        while_List.add("/product/getInfoByProductID");
        while_List.add("/user/register");
        while_List.add("/user/login");
        while_List.add("/salesMan/register");
        while_List.add("/salesMan/login");
        while_List.add("/manager/register");
        while_List.add("/manager/login");
        //配置黑名单
        List<String> black_List = new ArrayList<>();
        black_List.add("/user/*");
        black_List.add("/salesMan/*");
        black_List.add("/manager/*");
        black_List.add("/order/*");
        black_List.add("/product/*");
        registry.addInterceptor(loginCheckInterceptor)
                //拦截所有发往有关用户资源和订单资源的请求
                .addPathPatterns(black_List)
                //不拦截注册和登录页面资源
                .excludePathPatterns(while_List);
    }
}
