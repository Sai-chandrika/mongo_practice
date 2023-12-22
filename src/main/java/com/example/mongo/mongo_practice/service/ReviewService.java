package com.example.mongo.mongo_practice.service;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.ReviewDto;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */
public interface ReviewService {
    ApiResponse saveReview(ReviewDto reviewDto);
}
