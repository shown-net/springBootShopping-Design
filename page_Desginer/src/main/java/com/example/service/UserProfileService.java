package com.example.service;

import com.example.pojo.Order;
import com.example.pojo.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface UserProfileService {
    public void setUserProfile(Integer accountID);
    public void updateUserProfile(Integer accountID) throws JsonProcessingException;

    public List<Product> getUserProfile(Integer accountID) throws JsonProcessingException;
}
