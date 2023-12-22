package com.example.mongo.mongo_practice.entity;

import com.example.mongo.mongo_practice.dto.BaseDoc;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 19-12-2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "file_document")
public class FileDocument extends BaseDoc {
      private String fileId;
      private String fileName;
      @JsonIgnore
      private InputStream file;
      @JsonIgnore
      private String fileType;
      private LocalDateTime localDateTime;
}
