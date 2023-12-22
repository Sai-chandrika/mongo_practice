package com.example.mongo.mongo_practice.response;

import com.example.mongo.mongo_practice.dto.MenuDto;
import com.example.mongo.mongo_practice.dto.RestaurantDto;
import com.example.mongo.mongo_practice.dto.UserDto;
import com.example.mongo.mongo_practice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderResponse {
    private String id;
    private UserDto userData;
    private  RestaurantDto restaurant;
    private MenuDto menu;
    private String orderLocation;
    private LocalDateTime localTime;
    private Status status;

}
