package com.example.service;

import com.example.pojo.BrowserLog;
import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.SalesMan;

import java.util.List;


public interface SalesManService {

    SalesMan findByName(String accountName);

    Integer getBy_Name_Password(RegistrationRequest registrationRequest);

    Boolean UpdateInfoBy_Name(SalesMan salesMan);

    Boolean DeleteInfo(Integer ID);

    void register(RegistrationRequest registrationRequest);

    List<BrowserLog> getUserBrowseLog(Integer accountID);
}
