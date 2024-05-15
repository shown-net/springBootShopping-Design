package com.example.service;

import com.example.mapper.SalesManMapper;
import com.example.pojo.Manager;
import com.example.pojo.SalesMan;
import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.Result;

import java.util.List;

public interface ManagerService {
    Manager findByID(Integer accountID);

    Integer getBy_Name_Password(RegistrationRequest registrationRequest);

    void UpdateInfoBy_Name(Manager manager);

    void DeleteInfo(Integer ID);

    void register(String accountName, String password);

    List<SalesMan> getSalesManAccount();

    Result updateSalesManAccount(SalesMan salesMan);
}
