package com.example.service.ipml;

import com.example.mapper.AccountSessionMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.service.AccountSessionService;

@Service
public class AccountSessionServiceImpl implements AccountSessionService {

    @Autowired
    AccountSessionMapper accountSessionMapper;

    public String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        // 如果需要，可以通过逗号分隔的值获取第一个IP（当有多个代理时）
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }


    @Override
    public void setLoginRecord(Integer accountID, String accountType, String IpAddress) {
        if (accountSessionMapper.getNotLogOut(accountID, accountType, IpAddress) == 0) {
            //确定该IP地址的用户没有二次登录,再进行数据插入
            accountSessionMapper.insertLoginRecord(accountID, accountType, IpAddress);
        } else {
            //更新此IP地址下的用户登录时间
            accountSessionMapper.updateLoginRecord(accountID, accountType, IpAddress);
        }
    }

    @Override
    public void setLogoutRecord(Integer accountID, String accountType, String IpAddress) {
        accountSessionMapper.updateLogoutRecord(accountID, accountType, IpAddress);
    }


}
