package com.example.mongo.mongo_practice.entity;

import com.example.mongo.mongo_practice.dto.BaseDoc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "restaurant")
public class Restaurant extends BaseDoc {
    private String name;
    private String location;
    private String email;
    private String password;
}
