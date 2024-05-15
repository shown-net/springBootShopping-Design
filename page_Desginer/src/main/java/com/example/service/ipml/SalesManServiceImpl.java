package com.example.service.ipml;

import com.example.mapper.SalesManMapper;
import com.example.pojo.BrowserLog;
import com.example.pojo.User;
import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.SalesMan;
import com.example.service.SalesManService;
import com.example.utils.Md5Util;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SalesManServiceImpl implements SalesManService {
    @Autowired
    SalesManMapper salesManMapper;

    @Override
    public SalesMan findByName(String AccountName) {
        //查询用户名对应的用户
        SalesMan salesMan = salesManMapper.findByAccountName(AccountName);
        //后端发送的实体类密码字段不允许泄露给前端
        salesMan.setPassword(null);
        return salesMan;
    }

    @Override
    public void register(RegistrationRequest registrationRequest) {
        //密码字段加密
        registrationRequest.setPassword(Md5Util.getMD5String(registrationRequest.getPassword()));
        salesManMapper.register(registrationRequest);
    }

    @Override
    public List<BrowserLog> getUserBrowseLog(Integer accountID) {
        return salesManMapper.getUserBrowseLog(accountID);
    }

    @Override
    public Integer getBy_Name_Password(RegistrationRequest registrationRequest) {
        registrationRequest.setPassword(Md5Util.getMD5String(registrationRequest.getPassword()));
        return salesManMapper.salesManCheck(registrationRequest);
    }


    @Override
    public Boolean UpdateInfoBy_Name(SalesMan salesMan) {
        Map<String, Object> map = ThreadLocalUtil.get();
        salesMan.setId((Integer) map.get("id"));
        salesMan.setPassword(Md5Util.getMD5String(salesMan.getPassword()));
        salesManMapper.InfoUpdate(salesMan);
        return Boolean.TRUE;
    }

    @Override
    public Boolean DeleteInfo(Integer ID) {
        try {
            salesManMapper.LogOff(ID);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
