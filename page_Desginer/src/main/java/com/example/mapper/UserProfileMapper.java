package com.example.mapper;

import com.example.pojo.UserProfile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserProfileMapper {

    @Insert("insert into user_profile(id, updateTime) " +
            "values (#{accountID},now())")
    void setUserProfile(Integer accountID);

    @Select("select * from user_profile where id=#{accountID}")
    UserProfile getUserProfile(Integer accountID);

    @Update("update user_profile set preferredCategories=#{preferredKindRes},pricePerCategory=#{priceRes}," +
            "quantityPerCategory = #{quantityRes},preferredAveragePrice=#{averagePriceRes},updateTime=now() where id=#{accountID}")
    void updateUserProfile(Integer accountID, String preferredKindRes, String priceRes, String quantityRes,
    String averagePriceRes);
}
