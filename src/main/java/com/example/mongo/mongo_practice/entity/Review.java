package com.example.mongo.mongo_practice.entity;

import com.example.mongo.mongo_practice.dto.BaseDoc;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NegativeOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.ElementCollection;
import java.util.List;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "review")
public class Review extends BaseDoc {
    private String review;
    private List<String> files;
    @Min(0) @Max(5)
    private int rating;
    private Order order;
}
