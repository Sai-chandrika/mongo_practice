package com.example.mongo.mongo_practice.exception;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 14-12-2023
 */
public class NotFoundException extends RuntimeException{
      public NotFoundException(String message) {
            super(message);
      }

      public NotFoundException(String message, Throwable cause) {
            super(message, cause);
      }
}
