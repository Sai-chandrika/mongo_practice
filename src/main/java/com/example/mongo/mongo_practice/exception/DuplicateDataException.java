package com.example.mongo.mongo_practice.exception;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
public class DuplicateDataException extends RuntimeException{
    public DuplicateDataException(String message) {
        super(message);
    }
}
