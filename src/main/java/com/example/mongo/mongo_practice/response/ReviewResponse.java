package com.example.mongo.mongo_practice.response;

import com.example.mongo.mongo_practice.dto.OrderDto;
import com.example.mongo.mongo_practice.entity.FileDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ReviewResponse {
    private String id;
    private String review;
    private List<FileDocument> files;
    private int rating;
    private OrderResponse orderDto;
}
