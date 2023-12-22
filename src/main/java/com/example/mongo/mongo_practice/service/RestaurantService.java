package com.example.mongo.mongo_practice.service;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.RestaurantDto;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
public interface RestaurantService {
    ApiResponse addRestaurant(RestaurantDto restaurantDto);

    ApiResponse getAll();
}
