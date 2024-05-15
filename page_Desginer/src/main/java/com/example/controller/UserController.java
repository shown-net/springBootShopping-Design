package com.example.controller;

import com.example.anno.RoleCheck;
import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.pojo.BrowserLog;
import com.example.pojo.dto.UpdatePasswordDto;
import com.example.service.AccountSessionService;
import com.example.service.UserProfileService;
import com.example.service.UserService;
import com.example.utils.JwtUtils;
import com.example.utils.ThreadLocalUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    AccountSessionService accountSessionService;

    @Autowired
    UserProfileService userProfileService;


    // 增加注册用户,用单独的DTO类表示注册实体类
    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        Integer Repetition = userService.findByAccountName(registrationRequest.getAccountName());
        if (Repetition == 0) {
            userService.register(registrationRequest);
            //生成用户画像
            Integer userID = userService.getByAccountNamePassword(registrationRequest);
            userProfileService.setUserProfile(userID);
            return Result.success("注册账号成功");
        } else {
            return Result.error("存在重复用户名，请重试");
        }
    }

    // 用户登录信息查询
    @PostMapping("/login")
    public Result login(@RequestBody @Valid RegistrationRequest registrationRequest, HttpServletRequest request) {
        Integer userID = userService.getByAccountNamePassword(registrationRequest);
        String clientIP = accountSessionService.getClientIP(request);
        //返回的账号ID有效
        if (userID != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", userID);
            claims.put("accountName", registrationRequest.getAccountName());
            //用于角色权限控制
            claims.put("role", User.roleValue);
            String jwt = JwtUtils.generateJwt(claims);
            //登录日志记录
            accountSessionService.setLoginRecord(userID, User.roleValue, clientIP);
            // 返回jwt令牌
            return Result.success(jwt);
        }
        //返回错误信息
        else {
            return Result.error("用户名或者密码错误");
        }
    }

    // 用户退出登录日志记录
    @RoleCheck(User.roleValue)
    @PostMapping("/logout")
    public void setLogOutRecord(HttpServletRequest request) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userID = (Integer) map.get("id");
        String clientIP = accountSessionService.getClientIP(request);
        accountSessionService.setLogoutRecord(userID, User.roleValue, clientIP);
    }

    // 查询用户个人信息
    @RoleCheck(User.roleValue)
    @GetMapping("/selectInfo")
    public Result infoTest() throws JsonProcessingException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userID = (Integer) map.get("id");

        userProfileService.updateUserProfile(userID);

        User user = userService.findByAccountID(userID);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }

    // 获取根据用户画像推荐的商品列表
    @RoleCheck(User.roleValue)
    @GetMapping("/getPreferredProduct")
    public Result getPreferredProduct() throws JsonProcessingException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userID = (Integer) map.get("id");
        return Result.success(userProfileService.getUserProfile(userID));
    }

    // 更新用户个人信息
    @RoleCheck(User.roleValue)
    @PutMapping("/updateInfo")
    public Result updateInfo(@RequestBody @Valid User user) {
        Boolean result = userService.UpdateInfoByAccountName(user);
        if (result == Boolean.TRUE) {
            return Result.success("更新账号信息成功");
        } else {
            return Result.error("未知错误，请重试");
        }
    }

    // 设置用户浏览商品时长日志
    @RoleCheck(User.roleValue)
    @PostMapping("/sendBrowserTime")
    public void sendBrowserTime(@RequestBody @Valid BrowserLog browserLog) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userID = (Integer) map.get("id");
        browserLog.setUserID(userID);
        userService.sendBrowserTime(browserLog);
    }

    // 更新用户账户密码
    @RoleCheck(User.roleValue)
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        Boolean result = userService.updatePassword(updatePasswordDto);
        if (result == Boolean.TRUE) {
            return Result.success("更新账号密码成功");
        } else {
            return Result.error("未知错误，请重试");
        }
    }

    // 注销用户信息
    @RoleCheck(User.roleValue)
    @DeleteMapping("/LogOff")
    public Result deleteInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Boolean result = userService.DeleteUserInfo(userId);
        if (result == Boolean.TRUE) {
            return Result.success("注销成功");
        } else {
            return Result.error("注销失败");
        }
    }
}
