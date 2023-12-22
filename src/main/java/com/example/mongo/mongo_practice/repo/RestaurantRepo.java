package com.example.mongo.mongo_practice.repo;

import com.example.mongo.mongo_practice.entity.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 14-12-2023
 */
@Repository
public interface RestaurantRepo extends MongoRepository<Restaurant, String> {
    Restaurant findByEmail(String email);
}
