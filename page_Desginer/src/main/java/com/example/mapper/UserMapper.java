package com.example.mapper;

import com.example.pojo.dto.RegistrationRequest;
import com.example.pojo.User;
import com.example.pojo.dto.UpdatePasswordDto;
import com.example.pojo.dto.browseTime_Kind_Dto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    //根据用户名查找用户
    @Select("select * from user where id=#{userID}")
    public User findByAccountID(Integer userID);

    //插入新增的注册用户信息
    @Insert("insert into user (accountName,password,email,create_time,update_time) " +
            "values(#{accountName},#{password},#{email},now(),now())")
    public void register(RegistrationRequest registrationRequest);

    //查询该用户名是否存在且密码是否匹配，返回Id列表
    @Select("select id from user where accountName=#{accountName} and password=#{password} ")
    public Integer userCheck(String accountName, String password);

    @Select("select accountName,email from user where id=#{userID}")
    public User userEmailSelect(Integer userID);

    //更新用户信息(根据用户ID)
    @Update("update user set password=#{password}," +
            "gender=#{gender},age=#{age},email=#{email},phoneNumber=#{phoneNumber},update_time=now() " +
            "where id=#{id}")
    public void InfoUpdate(User user);

    //注销用户信息
    @Delete("delete from user where id=#{id}")
    public void LogOff(Integer id);

    @Select("select count(id) from user where accountName=#{accountName}")
    Integer findByAccountName(String accountName);

    @Update("update user set password=#{newPassword} where id=#{accountID}")
    Boolean updatePassword(UpdatePasswordDto updatePasswordDto);

    @Select("select kind,browse_time as browseTime from browse_log  where user_ID=#{userID}")
    List<browseTime_Kind_Dto> getBrowserTimeGroupByKind(Integer userID);

    @Select("select browse_time from browse_log where user_ID=#{userID} and kind=#{kind} ")
    Integer getBrowserTime(Integer userID, String kind);

    @Insert("insert into browse_log(user_ID, kind, browse_time) " +
            "values (#{userID},#{kind},#{browseTime})")
    void setBrowserTime(Integer userID, String kind, Integer browseTime);

    @Update("update browse_log set browse_time = #{browseTime} where user_ID=#{userID} and kind=#{kind} ")
    void updateBrowserTime(Integer userID, String kind, Integer browseTime);
}
