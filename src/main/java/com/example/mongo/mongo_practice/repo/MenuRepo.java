package com.example.mongo.mongo_practice.repo;

import com.example.mongo.mongo_practice.entity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
@Repository
public interface MenuRepo extends MongoRepository<Menu,String> {
    List<Menu> findAllByRestaurantId(String restaurantId);
}
