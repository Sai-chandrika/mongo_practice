package com.example.mongo.mongo_practice.dto;

import com.example.mongo.mongo_practice.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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
public class UserDto {
    private String id;
    @NotBlank(message = "name is mandatory")
    private String name;
    @NotBlank(message = "location is mandatory")
    private String location;
    @NotBlank(message = "email is mandatory")
    private String email;
    @NotBlank(message = "password is mandatory")
    private String password;
    @NotBlank(message = "role is mandatory")
    private RoleType role;
    @NotBlank(message = "contact is mandatory")
    private String contact;
}
