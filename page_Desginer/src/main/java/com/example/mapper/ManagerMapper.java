package com.example.mapper;

import com.example.pojo.Manager;
import com.example.pojo.Result;
import com.example.pojo.SalesMan;
import com.example.pojo.dto.RegistrationRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ManagerMapper {
    @Select("select * from manager where id=#{accountID}")
    public Manager findByAccountID(Integer accountID);


    //插入新增的注册用户信息
    @Insert("insert into manager (account_name,password,create_time,update_time) " +
            "values(#{accountName},#{password},now(),now())")
    public void register(String accountName, String password);

    //查询该用户名是否存在且密码和账户类型是否匹配，返回Id列表
    @Select("select id from manager where account_name=#{accountName} and password=#{password} ")
    public Integer managerCheck(RegistrationRequest registrationRequest);


    //更新用户信息(根据用户ID)
    @Update("update manager set password=#{password},update_time=now() where id=#{id}")
    public void InfoUpdate(Manager manager);

    //注销用户信息
    @Delete("delete from manager where id=#{id}")
    public void LogOff(Integer id);
    @Select("select id,account_name,password,email from salesman")
    List<SalesMan> getSalesManAccount();

    @Update("update salesman set password=#{password} where id=#{id}")
    void updateSalesManAccount(SalesMan salesMan);
}
