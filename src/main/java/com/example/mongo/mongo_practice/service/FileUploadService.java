package com.example.mongo.mongo_practice.service;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.entity.FileDocument;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 19-12-2023
 */
public interface FileUploadService {
    ApiResponse upload(List<MultipartFile> multipartFile) throws IOException;

    FileDocument getFileDetails(String fileId);

   ApiResponse uploadFiles(List<MultipartFile> multipartFiles) throws IOException;

    ResponseEntity<byte[]> downloadFile(String fileId) throws IOException;

    String uploadImage(MultipartFile file) throws IOException;

//    List<byte[]> getImageByIds(List<ObjectId> imageIds);

    byte[] getImageById(ObjectId objectId);

    byte[] getVideoById(ObjectId videoId);

    String uploadVideo(MultipartFile video) throws IOException;

    void viewVideo(String fileId, ByteArrayOutputStream outputStream);

    ApiResponse addVideo(MultipartFile videoFile) throws IOException;

    ResponseEntity<ByteArrayResource> viewVideos(String videoId) throws IOException;



    String generateVideoStreamLink(ObjectId objectId);
}
