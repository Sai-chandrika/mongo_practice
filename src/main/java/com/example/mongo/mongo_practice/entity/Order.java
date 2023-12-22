package com.example.mongo.mongo_practice.entity;
import com.example.mongo.mongo_practice.dto.BaseDoc;
import com.example.mongo.mongo_practice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "order")
public class Order extends BaseDoc {
     private UserData userData;
     private Restaurant restaurant;
     private Menu menu;
     private Status status;
     private LocalDateTime localTime;
     private String orderLocation;
}
