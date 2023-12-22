package com.example.mongo.mongo_practice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReviewDto {
      private String id;
      private String review;
      private List<String> files;
      private int rating;
      private String orderDto;
}
