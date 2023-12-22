package com.example.mongo.mongo_practice.repo;

import com.example.mongo.mongo_practice.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */

public interface ReviewRepo extends MongoRepository<Review, String> {
}
