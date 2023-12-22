package com.example.mongo.mongo_practice.service;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.LoginDto;
import com.example.mongo.mongo_practice.dto.UserDto;
import com.example.mongo.mongo_practice.entity.UserData;
import com.nimbusds.jose.JOSEException;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 14-12-2023
 */
public interface UserService {

      ApiResponse signUp(UserDto userData);

      ApiResponse login(LoginDto loginDto) throws JOSEException;
}
