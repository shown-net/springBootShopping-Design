package com.example.controller;

import com.example.anno.OperationLogRecord;
import com.example.anno.RoleCheck;
import com.example.pojo.Manager;
import com.example.pojo.OperationLog;
import com.example.pojo.Result;
import com.example.pojo.SalesMan;
import com.example.pojo.dto.RegistrationRequest;
import com.example.service.AccountSessionService;
import com.example.service.ManagerService;
import com.example.service.OperationLogService;
import com.example.utils.JwtUtils;
import com.example.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    AccountSessionService accountSessionService;

    @Autowired
    OperationLogService operationLogService;

//    // 增加注册用户
//    @PostMapping("/register")
//    public Result register(@Pattern(regexp = "^\\S{5,16}$") String accountName, @Pattern(regexp = "^\\S{5,16}$") String password) {
//        Manager manager = managerService.findByName(accountName);
//        if (manager == null) {
//            managerService.register(accountName, password);
//            return Result.success("注册账号成功");
//        } else {
//            return Result.error("存在重复用户名，请重试");
//        }
//    }

    // 用户登录信息查询
    @PostMapping("/login")
    public Result login(@RequestBody @Valid RegistrationRequest registrationRequest, HttpServletRequest request) {
        Integer accountID = managerService.getBy_Name_Password(registrationRequest);
        String clientIP = accountSessionService.getClientIP(request);
        //返回的账号只有一个,生成令牌
        if (accountID != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", accountID);
            claims.put("accountName", registrationRequest.getAccountName());
            //用于角色权限控制
            claims.put("role", Manager.roleValue);
            String jwt = JwtUtils.generateJwt(claims);
            //登录日志记录
            accountSessionService.setLoginRecord(accountID, Manager.roleValue, clientIP);
            // 返回jwt令牌
            return Result.success(jwt);
        }
        //返回错误信息
        else {
            return Result.error("用户名或者密码错误");
        }
    }

    @RoleCheck(Manager.roleValue)
    @OperationLogRecord("登出账号")
    @PostMapping("/logout")
    public void setLogOutRecord(HttpServletRequest request) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer accountID = (Integer) map.get("id");
        String clientIP = accountSessionService.getClientIP(request);
        accountSessionService.setLogoutRecord(accountID, SalesMan.roleValue, clientIP);
    }

    @RoleCheck(Manager.roleValue)
    @OperationLogRecord("查询管理者账号个人信息")
    // 查询用户个人信息
    @GetMapping("/selectInfo")
    public Result infoTest() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer accountID = (Integer) map.get("id");
        Manager manager = managerService.findByID(accountID);
        return Result.success(manager);
    }

    @RoleCheck(Manager.roleValue)
    @OperationLogRecord("查询操作日志")
    @GetMapping("/getOperationLog")
    public Result getOperationLog() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer accountID = (Integer) map.get("id");
        List<OperationLog> operationLogList = operationLogService.getOperationLog(Manager.roleValue, accountID);
        return Result.success(operationLogList);
    }

    @RoleCheck(Manager.roleValue)
    @OperationLogRecord("查询所有销售账号个人信息")
    // 查询销售账号信息
    @GetMapping("/getSalesManAccount")
    public Result getSalesManAccount() {
        List<SalesMan> salesManAccount = managerService.getSalesManAccount();
        return Result.success(salesManAccount);
    }


    @RoleCheck(Manager.roleValue)
    @OperationLogRecord("更新单个销售账号个人信息")
    // 更新销售账号信息
    @PostMapping("/updateSalesManAccount")
    public Result updateSalesManAccount(@RequestBody SalesMan salesMan) {
        return managerService.updateSalesManAccount(salesMan);
    }

    @RoleCheck(Manager.roleValue)
    @OperationLogRecord("更新管理者账号个人信息")
    // 更新管理者个人信息
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestBody Manager manager) {
        managerService.UpdateInfoBy_Name(manager);
        return Result.success("更新账号信息成功");
    }

//    @RoleCheck(Manager.roleValue)
//    // 删除管理者信息
//    @PostMapping("/LogOff")
//    public Result deleteInfo() {
//        Map<String, Object> map = ThreadLocalUtil.get();
//        Integer userId = (Integer) map.get("id");
//        managerService.DeleteInfo(userId);
//        return Result.success("注销成功");
//    }
}
