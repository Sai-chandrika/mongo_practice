package com.example.mongo.mongo_practice.dto;

import com.example.mongo.mongo_practice.enums.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class MenuDto {
      private String id;
      private String name;
      private Type type;
      private double cost;
      private String restaurantDto;
}
