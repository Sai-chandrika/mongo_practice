package com.example.mongo.mongo_practice.repo;

import com.example.mongo.mongo_practice.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */
@Repository
public interface OrderRepo extends MongoRepository<Order, String> {
}
