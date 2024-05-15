package com.example.service;


import com.example.pojo.BrowserLog;
import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.User;
import com.example.pojo.dto.UpdatePasswordDto;

import java.util.List;

public interface UserService {

    User findByAccountID(Integer userID);

    Integer getByAccountNamePassword(RegistrationRequest registrationRequest);

    Boolean UpdateInfoByAccountName(User user);

    Boolean DeleteUserInfo(Integer ID);

    User selectEmail(Integer userID);

    void register(RegistrationRequest registrationRequest);

    Integer findByAccountName(String accountName);

    Boolean updatePassword(UpdatePasswordDto updatePasswordDto);

    void sendBrowserTime(BrowserLog browserLog);
}
