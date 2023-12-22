package com.example.mongo.mongo_practice.controller;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.RestaurantDto;
import com.example.mongo.mongo_practice.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @PostMapping("/add-restaurant")

    public ApiResponse addRestaurant(@RequestBody RestaurantDto restaurantDto){
        return restaurantService.addRestaurant(restaurantDto);
    }
@GetMapping("/get-all-restaurants")
    public ApiResponse getAll(){
        return restaurantService.getAll();
    }

}
