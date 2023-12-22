package com.example.mongo.mongo_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class LoginDto {
    private String email;
    private String password;
}
