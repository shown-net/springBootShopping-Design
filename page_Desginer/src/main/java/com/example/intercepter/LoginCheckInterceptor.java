package com.example.intercepter;

import com.example.utils.JwtUtils;
import com.example.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    // 自定义异常，用于表示JWT解析失败
    private static class JwtParseException extends RuntimeException {
        public JwtParseException(String message) {
            super(message);
        }
    }

    private Map<String, Object> parseAndValidateJwt(HttpServletRequest request) throws JwtParseException {
        String jwt = request.getHeader("Authorization");
        if (jwt == null || jwt.isEmpty()) {
            throw new JwtParseException("JWT token is missing");
        }
        return JwtUtils.parseJWT(jwt);
    }

    @Override //目标资源运行前执行，返回true放行，返回false不放行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //处理预检请求
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        //尝试解析jwt令牌
        try {
            Map<String, Object> claims = parseAndValidateJwt(request);
            //把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
        } catch (JwtParseException e) { //jwt解析失败，返回错误信息
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        }
        //放行该请求
        return true;
    }

    @Override   //目标资源运行后执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override   //视图渲染完毕后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空ThreadLocal数据
        ThreadLocalUtil.remove();
    }
}
