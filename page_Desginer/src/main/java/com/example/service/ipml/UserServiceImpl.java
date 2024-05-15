package com.example.service.ipml;

import com.example.mapper.UserMapper;
import com.example.pojo.BrowserLog;
import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.User;
import com.example.pojo.dto.UpdatePasswordDto;
import com.example.service.UserService;
import com.example.utils.Md5Util;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByAccountID(Integer userID) {
        //查询用户名对应的用户
        User user = userMapper.findByAccountID(userID);
        //后端发送的实体类密码字段不允许泄露给前端
        user.setPassword(null);
        return userMapper.findByAccountID(userID);
    }

    @Override
    public void register(RegistrationRequest registrationRequest) {
        //密码字段加密
        registrationRequest.setPassword(Md5Util.getMD5String(registrationRequest.getPassword()));
        userMapper.register(registrationRequest);
    }

    @Override
    public Integer findByAccountName(String accountName) {
        return userMapper.findByAccountName(accountName);
    }

    @Override
    public Boolean updatePassword(UpdatePasswordDto updatePasswordDto) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer accountID = (Integer) map.get("id");
        updatePasswordDto.setAccountID(accountID);
        //明文密码加密，为了与数据库密文密码对比
        updatePasswordDto.setOldPassword(Md5Util.getMD5String(updatePasswordDto.getOldPassword()));
        Integer realUserID = userMapper.userCheck(updatePasswordDto.getAccountName(), updatePasswordDto.getOldPassword());
        if (accountID.equals(realUserID)) {
            //旧密码合法且正确返回了用户对应ID,新密码加密
            updatePasswordDto.setNewPassword(Md5Util.getMD5String(updatePasswordDto.getNewPassword()));
            userMapper.updatePassword(updatePasswordDto);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sendBrowserTime(BrowserLog browserLog) {
        Integer browserTime = userMapper.getBrowserTime(browserLog.getUserID(), browserLog.getKind());
        if (browserTime == null) {
            //设置该用户新商品类别访问时间记录
            userMapper.setBrowserTime(browserLog.getUserID(), browserLog.getKind(), browserLog.getBrowseTime());
        } else {
            //更新记录
            browserTime += browserLog.getBrowseTime();
            userMapper.updateBrowserTime(browserLog.getUserID(), browserLog.getKind(), browserTime);
        }

    }


    @Override
    public Integer getByAccountNamePassword(RegistrationRequest registrationRequest) {
        registrationRequest.setPassword(Md5Util.getMD5String(registrationRequest.getPassword()));
        return userMapper.userCheck(registrationRequest.getAccountName(), registrationRequest.getPassword());
    }


    @Override
    public Boolean UpdateInfoByAccountName(User user) {
        Map<String, Object> map = ThreadLocalUtil.get();
        user.setId((Integer) map.get("id"));
        user.setPassword(Md5Util.getMD5String(user.getPassword()));
        userMapper.InfoUpdate(user);
        return Boolean.TRUE;
    }

    @Override
    public Boolean DeleteUserInfo(Integer ID) {
        try {
            userMapper.LogOff(ID);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public User selectEmail(Integer userID) {
        return userMapper.userEmailSelect(userID);
    }


}
