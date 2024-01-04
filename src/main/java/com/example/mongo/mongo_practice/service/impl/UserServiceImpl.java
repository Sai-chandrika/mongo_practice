package com.example.mongo.mongo_practice.service.impl;

import com.example.mongo.mongo_practice.dto.*;
import com.example.mongo.mongo_practice.entity.UserData;
import com.example.mongo.mongo_practice.exception.DuplicateDataException;
import com.example.mongo.mongo_practice.exception.InvalidDataException;
import com.example.mongo.mongo_practice.exception.NotFoundException;
import com.example.mongo.mongo_practice.repo.UserRepo;
import com.example.mongo.mongo_practice.security.JwtTokenUtils;
import com.example.mongo.mongo_practice.service.UserService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 14-12-2023
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Override
    public ApiResponse signUp(UserDto user) {
        if(user.getId()!=null){
            return userUpdate(user);
        }else {
            UserData userData = userDtoToUser(user);
            userRepo.save(userData);
            return new ApiResponse(HttpStatus.OK.value(), "user sign-up successfully :)", userToDto(userData));
        }
    }

    @Override
    public ApiResponse login(LoginDto loginDto) throws JOSEException {
       UserData userData=userRepo.findByEmail(loginDto.getEmail());
       if(userData==null) throw new NotFoundException("user not found with this given email :(");
       if(bCryptPasswordEncoder.matches(loginDto.getPassword(),userData.getPassword())) {
           SignInResponse signInResponse = new SignInResponse(userData);
           signInResponse.setToken(jwtTokenUtils.getToken(userData));
           return new ApiResponse(HttpStatus.OK.value(), "login successfully :) " , signInResponse);
       }else
           throw new InvalidDataException("please check password once :(");
    }

    public ApiResponse userUpdate(UserDto user){
        Optional<UserData> optional=userRepo.findById(user.getId());
        if(optional.isEmpty()) throw  new NotFoundException("user id not found :(");
        UserData userData=optional.get();
        if(user.getName()!=null){
            userData.setName(user.getName());
        }
        if(user.getRole()!=null){
            userData.setRole(user.getRole());
        }
        if(user.getPassword()!=null){
            userData.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if(user.getLocation()!=null){
            userData.setLocation(user.getLocation());
        }
        if(user.getEmail()!=null){
            if(Boolean.TRUE.equals(uniqueEmailValidation(user))) {
                if (Boolean.TRUE.equals(Validation.isValidEmailPattern(user.getEmail()))) {
                    userData.setEmail(user.getEmail());
                } else {
                    throw new InvalidDataException("please provide valid email");
                }
            }else{
                throw new DuplicateDataException("email is already existed..");
            }
        }
        if(user.getContact()!=null){
            if(Boolean.TRUE.equals(Validation.isValidMobileNumber(user.getContact()))) {
                userData.setContact(user.getContact());
            }else {
                throw new DuplicateDataException("please provide valid contact");
            }
        }
        userRepo.save(userData);
        return new ApiResponse(HttpStatus.OK.value(), "user updated successfully", userToDto(userData));

    }


    public UserData userDtoToUser(UserDto dto){
        UserData userData=new UserData();
        userData.setName(dto.getName());
       if(Boolean.TRUE.equals(uniqueEmailValidation(dto))) {
           if (Boolean.TRUE.equals(Validation.isValidEmailPattern(dto.getEmail()))) {
               userData.setEmail(dto.getEmail());
           } else {
               throw new InvalidDataException("please provide valid email");
           }
       }else{
           throw new DuplicateDataException("email is already existed..");
       }
        userData.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userData.setLocation(dto.getLocation());
        userData.setRole(dto.getRole());
        if(Boolean.TRUE.equals(Validation.isValidMobileNumber(dto.getContact()))) {
            userData.setContact(dto.getContact());
        }else {
            throw new DuplicateDataException("please provide valid contact");
        }
        return userData;
    }

    public UserDto userToDto(UserData userData){
        UserDto userDto=new UserDto();
        userDto.setId(userData.getId());
        userDto.setName(userData.getName());
        userDto.setEmail(userData.getEmail());
        userDto.setPassword(userData.getPassword());
        userDto.setLocation(userData.getLocation());
        userDto.setRole(userData.getRole());
        userDto.setContact(userData.getContact());
        return userDto;
    }


    private Boolean uniqueEmailValidation(UserDto dto) {
        if (dto.getEmail() != null) {
            Optional<UserData> optionalClient = Optional.ofNullable(userRepo.findByEmail(dto.getEmail()));
            if (optionalClient.isPresent()) {
                if (dto.getId() != null && dto.getId().equals(optionalClient.get().getId())) {
                    return Boolean.TRUE;
                } else return Boolean.FALSE;
            } else return Boolean.TRUE;
        } else return null;
    }
}
