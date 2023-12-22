package com.example.mongo.mongo_practice.dto;

import com.example.mongo.mongo_practice.entity.UserData;
import com.example.mongo.mongo_practice.enums.RoleType;
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
public class SignInResponse {
      private String id;
      private String name;
      private String location;
      private String email;
      private RoleType role;
      private String contact;
      private  String token;

      public SignInResponse(UserData userData) {
            this.id=userData.getId();
            this.name=userData.getName();
            this.location=userData.getLocation();
            this.email=userData.getEmail();
            this.role=userData.getRole();
            this.contact=userData.getContact();
      }
}
