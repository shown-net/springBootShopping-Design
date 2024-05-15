package com.example.mapper;

import com.example.pojo.BrowserLog;
import com.example.pojo.Manager;
import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.SalesMan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SalesManMapper {

    //根据用户名查找用户
    @Select("select * from salesman where account_name=#{accountName}")
    public SalesMan findByAccountName(String accountName);


    //插入新增的注册用户信息
    @Insert("insert into salesman (account_name,password,email,create_time,update_time) " +
            "values(#{accountName},#{password},#{email},now(),now())")
    public void register(RegistrationRequest registrationRequest);

    //查询该用户名是否存在且密码和账户类型是否匹配，返回Id列表
    @Select("select id from salesman where account_name=#{accountName} and password=#{password} ")
    public Integer salesManCheck(RegistrationRequest registrationRequest);


    //更新用户信息(根据用户ID)
    @Update("update salesman set password=#{password},email=#{email},update_time=now() where id=#{id}")
    public void InfoUpdate(SalesMan salesman);

    //注销用户信息
    @Delete("delete from salesman where id=#{id}")
    public void LogOff(Integer id);

    List<BrowserLog> getUserBrowseLog(Integer accountID);
}
