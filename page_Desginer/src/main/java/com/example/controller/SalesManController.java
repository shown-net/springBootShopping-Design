package com.example.controller;

import com.example.anno.OperationLogRecord;
import com.example.anno.RoleCheck;
import com.example.pojo.BrowserLog;
import com.example.pojo.OperationLog;
import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.Result;
import com.example.pojo.SalesMan;
import com.example.service.AccountSessionService;
import com.example.service.OperationLogService;
import com.example.service.SalesManService;
import com.example.utils.JwtUtils;
import com.example.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/salesMan")
//销售人员控制类
public class SalesManController {
    @Autowired
    private SalesManService salesManService;

    @Autowired
    AccountSessionService accountSessionService;

    @Autowired
    OperationLogService operationLogService;

    // // 增加注册用户,用单独的DTO类表示注册实体类
    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        SalesMan salesMan = salesManService.findByName(registrationRequest.getAccountName());
        if (salesMan == null) {
            salesManService.register(registrationRequest);
            return Result.success("注册账号成功");
        } else {
            return Result.error("存在重复用户名，请重试");
        }
    }

    // 用户登录信息查询
    @PostMapping("/login")
    public Result login(@RequestBody @Valid RegistrationRequest registrationRequest, HttpServletRequest request) {
        Integer accountID = salesManService.getBy_Name_Password(registrationRequest);
        String clientIP = accountSessionService.getClientIP(request);
        //返回的账号只有一个,生成令牌
        if (accountID != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", accountID);
            claims.put("accountName", registrationRequest.getAccountName());
            //用于角色权限控制
            claims.put("role", SalesMan.roleValue);
            String jwt = JwtUtils.generateJwt(claims);
            //登录日志记录
            accountSessionService.setLoginRecord(accountID, SalesMan.roleValue, clientIP);
            // 返回jwt令牌
            return Result.success(jwt);
        }
        //返回错误信息
        else {
            return Result.error("用户名或者密码错误");
        }
    }

    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("登出账号")
    @PostMapping("/logout")
    public void setLogOutRecord(HttpServletRequest request) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer accountID = (Integer) map.get("id");
        String clientIP = accountSessionService.getClientIP(request);
        accountSessionService.setLogoutRecord(accountID, SalesMan.roleValue, clientIP);
    }

    // 查询用户个人信息
    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("查询账号信息")
    @GetMapping("/selectInfo")
    public Result infoTest(@RequestParam String accountName) {
        SalesMan salesMan = salesManService.findByName(accountName);
        if (salesMan != null) {
            return Result.success(salesMan);
        } else {
            return Result.error("用户不存在");
        }
    }

    // 查询该账号的操作日志
    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("查询操作日志")
    @GetMapping("/getOperationLog")
    public Result getOperationLog() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer accountID = (Integer) map.get("id");
        List<OperationLog> operationLogList = operationLogService.getOperationLog(SalesMan.roleValue, accountID);
        return Result.success(operationLogList);
    }

    // 查询普通用户的浏览商品信息
    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("查询用户的浏览商品信息")
    @GetMapping("/getUserBrowseLog")
    public Result getUserBrowseLog() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer accountID = (Integer) map.get("id");
        List<BrowserLog> browserLogs = salesManService.getUserBrowseLog(accountID);
        return Result.success(browserLogs);
    }

    // 更新用户个人信息
    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("更新账号个人信息")
    @PutMapping("/updateInfo")
    public Result updateInfo(@RequestBody SalesMan salesMan) {
        Boolean result = salesManService.UpdateInfoBy_Name(salesMan);
        if (result == Boolean.TRUE) {
            return Result.success("更新账号信息成功");
        } else {
            return Result.error("未知错误，请重试");
        }
    }

    // 删除用户信息
    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("注销账号")
    @DeleteMapping("/LogOff")
    public Result deleteInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Boolean result = salesManService.DeleteInfo(userId);
        if (result == Boolean.TRUE) {
            return Result.success("注销成功");
        } else {
            return Result.error("注销失败");
        }
    }
}
