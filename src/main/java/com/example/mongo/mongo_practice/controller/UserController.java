package com.example.mongo.mongo_practice.controller;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.LoginDto;
import com.example.mongo.mongo_practice.dto.UserDto;
import com.example.mongo.mongo_practice.entity.UserData;
import com.example.mongo.mongo_practice.repo.UserRepo;
import com.example.mongo.mongo_practice.service.UserService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 14-12-2023
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody UserDto userData){
      return userService.signUp(userData);
    }
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginDto loginDto) throws JOSEException {
        return userService.login(loginDto);

    }
}
