package com.example.mongo.mongo_practice.entity;

import com.example.mongo.mongo_practice.dto.BaseDoc;
import com.example.mongo.mongo_practice.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Document(collection = "menu")
public class Menu extends BaseDoc {
    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;
    private double cost;
    private Restaurant restaurant;
}
