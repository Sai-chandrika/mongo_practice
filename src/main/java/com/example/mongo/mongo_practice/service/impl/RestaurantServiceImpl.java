package com.example.mongo.mongo_practice.service.impl;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.MenuDto;
import com.example.mongo.mongo_practice.dto.RestaurantDto;
import com.example.mongo.mongo_practice.dto.Validation;
import com.example.mongo.mongo_practice.entity.Menu;
import com.example.mongo.mongo_practice.entity.Restaurant;
import com.example.mongo.mongo_practice.entity.UserData;
import com.example.mongo.mongo_practice.exception.DuplicateDataException;
import com.example.mongo.mongo_practice.exception.InvalidDataException;
import com.example.mongo.mongo_practice.exception.NotFoundException;
import com.example.mongo.mongo_practice.repo.MenuRepo;
import com.example.mongo.mongo_practice.repo.RestaurantRepo;
import com.example.mongo.mongo_practice.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    RestaurantRepo restaurantRepo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ApiResponse addRestaurant(RestaurantDto restaurantDto) {
        if (restaurantDto.getId() != null) {
            return update(restaurantDto);
        } else {
            Restaurant restaurant = restaurantDtoToEntity(restaurantDto);
            restaurantRepo.save(restaurant);
            return new ApiResponse(HttpStatus.OK.value(), "restaurant save successfully", restaurantToDto(restaurant));
        }
    }

    @Override
    public ApiResponse getAll() {
        List<Restaurant> restaurants=restaurantRepo.findAll();
        return new ApiResponse(HttpStatus.OK.value(), restaurants);
    }

    private RestaurantDto restaurantToDto(Restaurant restaurant) {
        RestaurantDto restaurantDto=new RestaurantDto();
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setLocation(restaurant.getLocation());
        restaurantDto.setEmail(restaurant.getEmail());
        restaurantDto.setPassword(bCryptPasswordEncoder.encode(restaurant.getPassword()));
        return restaurantDto;
    }


    public ApiResponse update(RestaurantDto restaurantDto) {
        Optional<Restaurant> optional = restaurantRepo.findById(restaurantDto.getId());
        if (optional.isEmpty()) throw new NotFoundException("restaurant id is not found :(");
        Restaurant restaurant = optional.get();
        if (restaurantDto.getName() != null) {
            restaurant.setName(restaurantDto.getName());
        }
        if (restaurantDto.getLocation() != null) {
            restaurant.setLocation(restaurantDto.getLocation());
        }
        if(restaurantDto.getEmail()!=null) {
            String s = emailVerification(restaurantDto.getEmail());
            if (s != null) throw new DuplicateDataException("restaurant is already existed with given this email");
            if (Boolean.TRUE.equals(Validation.isValidEmailPattern(restaurantDto.getEmail()))) {
                restaurant.setEmail(restaurantDto.getEmail());
            } else throw new InvalidDataException("please provide valid email");
        }
        if(restaurantDto.getPassword()!=null) {
            restaurant.setPassword(bCryptPasswordEncoder.encode(restaurantDto.getPassword()));
        }
        return new ApiResponse(HttpStatus.OK.value(), " restaurant updated successfully :)", restaurant);
    }



    private Restaurant restaurantDtoToEntity(RestaurantDto restaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantDto.getName());
        restaurant.setLocation(restaurantDto.getLocation());
        String s=emailVerification(restaurantDto.getEmail());
        if(s!=null) throw  new DuplicateDataException("restaurant is already existed with given this email");
        if(Boolean.TRUE.equals(Validation.isValidEmailPattern(restaurantDto.getEmail()))) {
            restaurant.setEmail(restaurantDto.getEmail());
        }else throw new InvalidDataException("please provide valid email");
        restaurant.setPassword(bCryptPasswordEncoder.encode(restaurantDto.getPassword()));
        return restaurant;
    }


    public String emailVerification(String email){
        Restaurant optional=restaurantRepo.findByEmail(email);
        if(optional!=null){
            return "email is already existed";
        }else{
            return null;
        }

    }


}
