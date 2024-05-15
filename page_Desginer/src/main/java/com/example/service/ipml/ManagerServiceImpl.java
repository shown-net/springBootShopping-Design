package com.example.service.ipml;

import com.example.mapper.ManagerMapper;
import com.example.pojo.Manager;
import com.example.pojo.Result;
import com.example.pojo.SalesMan;
import com.example.pojo.dto.RegistrationRequest;
import com.example.service.ManagerService;
import com.example.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    ManagerMapper managerMapper;

    @Override
    public Manager findByID(Integer accountID) {
        //查询管理者ID对应的账户
        return managerMapper.findByAccountID(accountID);
    }

    @Override
    public void register(String accountName, String password) {
        //加密
//        String md5String = Md5Util.getMD5String(password);
        managerMapper.register(accountName, password);
    }

    @Override
    public List<SalesMan> getSalesManAccount() {
        return managerMapper.getSalesManAccount();
    }

    @Override
    public Result updateSalesManAccount(SalesMan salesMan) {
        salesMan.setPassword(Md5Util.getMD5String(salesMan.getPassword()));
        managerMapper.updateSalesManAccount(salesMan);
        return Result.success("更新密码成功");
    }

    @Override
    public Integer getBy_Name_Password(RegistrationRequest registrationRequest) {
        return managerMapper.managerCheck(registrationRequest);
    }


    @Override
    public void UpdateInfoBy_Name(Manager manager) {
        managerMapper.InfoUpdate(manager);
    }

    @Override
    public void DeleteInfo(Integer ID) {
        managerMapper.LogOff(ID);
    }
}
