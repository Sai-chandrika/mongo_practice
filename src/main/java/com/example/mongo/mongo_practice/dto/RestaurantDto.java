package com.example.mongo.mongo_practice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 14-12-2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RestaurantDto {
    private String id;
    private String name;
    private String location;
    private String email;
    private String password;
}
