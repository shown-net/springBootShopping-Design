package com.example.service;

import jakarta.servlet.http.HttpServletRequest;

public interface AccountSessionService {
    // 获取客户端IP地址的工具方法
    String getClientIP(HttpServletRequest request);

    void setLoginRecord(Integer accountID, String accountType, String IpAddress);

    void setLogoutRecord(Integer accountID, String accountType, String IpAddress);

}
