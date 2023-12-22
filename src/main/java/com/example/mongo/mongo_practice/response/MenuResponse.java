package com.example.mongo.mongo_practice.response;

import com.example.mongo.mongo_practice.dto.RestaurantDto;
import com.example.mongo.mongo_practice.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MenuResponse {
      private String id;
      private String name;
      private Type type;
      private double cost;
      private RestaurantDto restaurantDto;
}
