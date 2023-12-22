package com.example.mongo.mongo_practice.exception;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 14-12-2023
 */
public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException(String message) {
        super(message);
    }
}
