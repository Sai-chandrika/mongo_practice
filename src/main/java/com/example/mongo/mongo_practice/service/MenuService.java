package com.example.mongo.mongo_practice.service;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.MenuDto;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
public interface MenuService {

      ApiResponse menuAdd(MenuDto menuDto);

      ApiResponse getAll(String restaurantId);
}
