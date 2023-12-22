package com.example.mongo.mongo_practice.controller;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.entity.FileDocument;
import com.example.mongo.mongo_practice.exception.NotFoundException;
import com.example.mongo.mongo_practice.service.FileUploadService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 19-12-2023
 */
@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "http://localhost:4200") // Replace with the origin of your client application
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;


    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ApiResponse upload(@RequestParam List<MultipartFile> multipartFile) throws IOException {
        return fileUploadService.upload(multipartFile);
    }
    @PostMapping(value = "/upload/file", consumes = {"multipart/form-data"})
    public ApiResponse uploadFiles(@RequestParam List<MultipartFile> multipartFiles) throws IOException {
        return fileUploadService.uploadFiles(multipartFiles);
    }
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) throws IOException {
        return fileUploadService.downloadFile(fileId);
    }

    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileUploadService.uploadImage(file);
            return ResponseEntity.status(HttpStatus.OK).body("Image uploaded successfully. File ID: " + fileId);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }
    @GetMapping("/image/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageId) {
        byte[] imageData = fileUploadService.getImageById(new ObjectId(imageId));
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(imageData));
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @PostMapping(value = "/uploadVideo", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadVideo(@RequestParam("video") MultipartFile video) throws IOException {
        String fileId = fileUploadService.uploadVideo(video);
        return ResponseEntity.status(HttpStatus.OK.value()).body("Video uploaded successfully. File ID: " + fileId);
    }


    @GetMapping("/view/{fileId}")
    public ResponseEntity<byte[]> viewVideo(@PathVariable String fileId) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fileUploadService.viewVideo(fileId, outputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "video.mp4");
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK.value());
    }
    @PostMapping(value = "/video/upload", consumes = {"multipart/form-data"})
    ApiResponse addVideo(@RequestParam MultipartFile videoFile) throws IOException {
        return fileUploadService.addVideo(videoFile);
    }
    @GetMapping("/play/{video}")
    public ResponseEntity<ByteArrayResource> view(@PathVariable String video) throws IOException {
        try {
            System.out.println("Requested videoId: " + video);
            ResponseEntity<ByteArrayResource> response = fileUploadService.viewVideos(video);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Video found and returned successfully.");
            } else {
                System.out.println("Video not found. Status code: " + response.getStatusCodeValue());
            }
            return response;
        } catch (Exception e) {
            System.err.println("Error while processing the request: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/videos/{videoId}")
    public ResponseEntity<byte[]> viewVideos(@PathVariable String videoId) throws IOException {
        ObjectId objectId;
        try {
            objectId = new ObjectId(videoId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        byte[] videoData = fileUploadService.getVideoById(objectId);

        if (videoData.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("video/mp4"));
            headers.setContentLength(videoData.length);

            return new ResponseEntity<>(videoData, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<String> getVideoPlayUrl(@PathVariable String videoId) {
        try {
            ObjectId objectId = new ObjectId(videoId);
            String videoPlayUrl = fileUploadService.generateVideoStreamLink(objectId);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.TEXT_PLAIN);
            return ResponseEntity.ok()
//                    .headers(headers)
                    .body(videoPlayUrl);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    }


















//    @GetMapping("/image/{imageId}")
//    public ResponseEntity<List<Resource>> getImages(@RequestParam List<String> imageId) {
//        List<ObjectId> objectIdList = imageId.stream()
//                .map(ObjectId::new)
//                .collect(Collectors.toList());
//        List<byte[]> images = fileUploadService.getImageByIds(objectIdList);
//        List<Resource> resources = images.stream()
//                .map(imageData -> new InputStreamResource(new ByteArrayInputStream(imageData)))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(resources);
//

