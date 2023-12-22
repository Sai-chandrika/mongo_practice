package com.example.mongo.mongo_practice.repo;

import com.example.mongo.mongo_practice.entity.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 14-12-2023
 */
@Repository
public interface UserRepo extends MongoRepository<UserData,String> {
    UserData findByEmail(String userName);

    UserData findByName(String userName);
}
