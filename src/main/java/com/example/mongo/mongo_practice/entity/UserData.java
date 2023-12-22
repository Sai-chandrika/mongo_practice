package com.example.mongo.mongo_practice.entity;

import com.example.mongo.mongo_practice.dto.BaseDoc;
import com.example.mongo.mongo_practice.enums.RoleType;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
@Document(collection = "users")
public class UserData extends BaseDoc {
    private String name;
    private String location;
    @Indexed(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private RoleType role;
    private String contact;
}
