package com.example.mongo.mongo_practice.repo;

import com.example.mongo.mongo_practice.entity.FileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 19-12-2023
 */
@Repository
public interface FileDocumentRepo extends MongoRepository<FileDocument, String> {
    Optional<FileDocument> findByFileId(String file);
}
