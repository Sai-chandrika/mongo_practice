package com.example.mongo.mongo_practice.dto;

import com.example.mongo.mongo_practice.entity.Menu;
import com.example.mongo.mongo_practice.entity.Restaurant;
import com.example.mongo.mongo_practice.entity.UserData;
import com.example.mongo.mongo_practice.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderDto {
    private String id;
    private String userData;
    private String restaurant;
    private String menu;
    private Status status;
    private String orderLocation;
    private LocalDateTime localTime;


}
