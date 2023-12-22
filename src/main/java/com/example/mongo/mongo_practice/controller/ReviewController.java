package com.example.mongo.mongo_practice.controller;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.ReviewDto;
import com.example.mongo.mongo_practice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */
@RestController
@RequestMapping("review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping("/review-save")
    public ApiResponse reviewSave(ReviewDto reviewDto){
        return reviewService.saveReview(reviewDto);

    }
}
