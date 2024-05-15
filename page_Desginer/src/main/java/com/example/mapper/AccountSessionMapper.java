package com.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AccountSessionMapper {
    //用于判断用户是否在同一IP地址上没有触发退出登录事件而再次登录
    @Select("select count(account_id) from account_session_records where account_type=#{accountType} and " +
            "account_id=#{accountID} and ip_address=#{IpAddress} and logout_time is null")
    Integer getNotLogOut(Integer accountID, String accountType, String IpAddress);

    @Insert("insert into account_session_records (account_id,account_type,ip_address) " +
            "values (#{accountID},#{accountType},#{IpAddress})")
    void insertLoginRecord(Integer accountID, String accountType, String IpAddress);

    @Update("update account_session_records set login_time=now() " +
            "where account_type=#{accountType} and account_id=#{accountID} and ip_address=#{IpAddress}")
    void updateLoginRecord(Integer accountID, String accountType, String IpAddress);

    @Update("update account_session_records set logout_time=now() " +
            "where account_type=#{accountType} and account_id=#{accountID} and ip_address=#{IpAddress} and logout_time is null")
    void updateLogoutRecord(Integer accountID, String accountType, String IpAddress);


}
