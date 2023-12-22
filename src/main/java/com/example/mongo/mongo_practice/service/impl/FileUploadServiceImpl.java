package com.example.mongo.mongo_practice.service.impl;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.entity.FileDocument;
import com.example.mongo.mongo_practice.exception.InvalidDataException;
import com.example.mongo.mongo_practice.exception.NotFoundException;
import com.example.mongo.mongo_practice.repo.FileDocumentRepo;
import com.example.mongo.mongo_practice.service.FileUploadService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;


/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 19-12-2023
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    FileDocumentRepo fileDocumentRepo;
    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    String databaseName = "food_box";
    String bucketName = "fs";
    @Value("${app.base-url}")
    private String baseUrl;



    @Override
    public ApiResponse upload(List<MultipartFile> files) throws IOException {
        List<FileDocument> fileDocuments = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty() || isEmpty(file.getOriginalFilename()) || isEmpty(file.getContentType()))
                throw new InputMismatchException("Uploaded File" + file.getOriginalFilename() + "is not valid");
            DBObject dbObject = new BasicDBObject();
            dbObject.put("type", file.getContentType());
            dbObject.put("title", file.getOriginalFilename());
            dbObject.put("time", LocalDateTime.now());
            ObjectId objectId = gridFsTemplate.store(file.getInputStream(), file.getContentType(), file.getOriginalFilename(), dbObject);
            FileDocument fileDocument = new FileDocument();
            fileDocument.setFileName(file.getOriginalFilename());
            fileDocument.setFileType(file.getContentType());
            fileDocument.setLocalDateTime(LocalDateTime.now());
            fileDocument.setFileId(objectId.toString());
            fileDocumentRepo.save(fileDocument);
            fileDocuments.add(fileDocument);
        }
        return new ApiResponse(HttpStatus.OK.value(), "file uploaded successfully", fileDocuments);
    }


    @Override
    public FileDocument getFileDetails(String fileId) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            FileDocument fileDocument = new FileDocument();
            Optional<FileDocument> optional = fileDocumentRepo.findByFileId(fileId);
            if (optional.isEmpty()) throw new InvalidDataException("file is not found");
            fileDocument.setFileId(fileId);
            fileDocument.setFileType(optional.get().getFileType());
            fileDocument.setFileName(optional.get().getFileName());
            fileDocument.setLocalDateTime(LocalDateTime.now());
            return fileDocument;
        } else {
            throw new NotFoundException("No file content found with ID: " + fileId);
        }
    }

    @Override
    public ApiResponse uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase(databaseName), bucketName);
        List<ObjectId> fileIds = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile.isEmpty() || isEmpty(multipartFile.getOriginalFilename()) || isEmpty(multipartFile.getContentType()))
                    throw new InputMismatchException("Uploaded File " + multipartFile.getOriginalFilename() + " is not valid");
                InputStream inputStream = multipartFile.getInputStream();
                GridFSUploadOptions options = new GridFSUploadOptions()
                        .chunkSizeBytes(1024 * 1024)
                        .metadata(createMetadata(multipartFile));
                ObjectId fileId = gridFSBucket.uploadFromStream(multipartFile.getOriginalFilename(), inputStream, options);
                fileIds.add(fileId);

                inputStream.close();
            }

            return new ApiResponse(HttpStatus.OK.value(), "Files uploaded successfully", fileIds);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error uploading files: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Error processing files: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String fileId) throws IOException {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase(databaseName), bucketName);
        ObjectId objectId = new ObjectId(fileId);
        GridFSFile gridFSFile = gridFSBucket.find().first();
        if (gridFSFile != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", gridFSFile.getFilename());
            InputStream stream = gridFSBucket.openDownloadStream(objectId);
            byte[] fileBytes = new byte[(int) gridFSFile.getLength()];
            stream.read(fileBytes);
            stream.close();
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        org.bson.Document metadata = new org.bson.Document();
        metadata.put("filename", file.getOriginalFilename());
        String fileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(), metadata).toString();
        return fileId;
    }

    @Override
    public byte[] getImageById(ObjectId imageId) {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase(databaseName), bucketName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            gridFSBucket.downloadToStream(imageId, outputStream);
        }catch (Exception e){
            throw new NotFoundException("provide file id", e);
        }
        return outputStream.toByteArray();
    }
    @Override
    public byte[] getVideoById(ObjectId videoId) {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase(databaseName), bucketName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(videoId);
            int bufferSize = 1024 * 1024;
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            while ((bytesRead = downloadStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            downloadStream.close();
        } catch (Exception e) {
            throw new NotFoundException("Provide video id", e);
        }

        return outputStream.toByteArray();
    }

    @Override
    public String uploadVideo(MultipartFile video) throws IOException {
        InputStream inputStream = video.getInputStream();
        org.bson.Document metadata = new org.bson.Document();
        metadata.put("filename", video.getOriginalFilename());
        String fileId = gridFsTemplate.store(inputStream, video.getOriginalFilename(), video.getContentType(), metadata).toString();
        return fileId;
    }

    @Override
    public void viewVideo(String fileId, ByteArrayOutputStream outputStream) {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase(databaseName), bucketName);
        gridFSBucket.downloadToStream(new ObjectId(fileId), outputStream);
    }


    @Override
    public ApiResponse addVideo(MultipartFile videoFile) throws IOException {
        String videoId = generateUniqueVideoId();
        String contentType = videoFile.getContentType();
        DBObject metadata=new BasicDBObject();
        metadata.put("originalFileName",videoFile.getOriginalFilename());
        metadata.put("fileType",videoFile.getContentType());
        if ("video/mp4".equals(contentType)) {
//            int chunkSize = 10 * 1024 * 1024; // 10MB
            int chunkSize = 5 * 60 * 1024 * 1024;  // 5 min
            byte[] buffer = new byte[chunkSize];
            int bytesRead;
            int chunkNumber = 0;

            try (InputStream inputStream = videoFile.getInputStream()) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byte[] chunkData = new byte[bytesRead];
                    System.arraycopy(buffer, 0, chunkData, 0, bytesRead);
                    gridFsTemplate.store(new ByteArrayInputStream(chunkData), videoId + "-part-" + chunkNumber,contentType,metadata);
                    chunkNumber++;
                }
            }
            String downloadUrl = baseUrl+"/play/"+videoId;
            return new ApiResponse(HttpStatus.OK.value(),  videoId, Collections.singletonMap("downloadUrl", downloadUrl));
        }else throw new InvalidDataException("Only mp4 video's are allowed");
    }

    @Override
    public ResponseEntity<ByteArrayResource> viewVideos(String videoId) throws IOException {
//        Query query = new Query(Criteria.where("filename").regex("^" + videoId + "-part-.*$"));
        Query query = new Query(Criteria.where("filename").is(videoId));
        List<GridFSFile> chunkFiles = new ArrayList<>();
        MongoCursor<GridFSFile> cursor = gridFsTemplate.find(query).iterator();
        try {
            while (cursor.hasNext()) {
                chunkFiles.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        if (!chunkFiles.isEmpty()) {
            chunkFiles.sort(Comparator.comparing(GridFSFile::getFilename));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (GridFSFile chunkFile : chunkFiles) {
                try (InputStream inputStream = gridFsTemplate.getResource(chunkFile).getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("video/mp4"));
            headers.setContentLength(outputStream.size());
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ByteArrayResource(outputStream.toByteArray()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


@Override
     public String generateVideoStreamLink(ObjectId videoId) throws NotFoundException {
        if (!videoExists(videoId)) {
            throw new NotFoundException("Video not found");
        }
        return baseUrl + "/video/stream/" + videoId;
    }
    private boolean videoExists(ObjectId videoId) {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase(databaseName), bucketName);
        GridFSFile gridFSFile = gridFSBucket.find(new Document("_id", videoId)).first();
        return gridFSFile != null;
    }
    public String generateUniqueVideoId() {
        return UUID.randomUUID().toString();
    }






    















   /* public List<byte[]> getImageByIds(List<ObjectId> imageIds) {
        List<byte[]> objectIds=new ArrayList<>();
     for(ObjectId objectId:imageIds){
         objectIds.add(getImageById(objectId));
     }
        return objectIds;
    }*/

    private Document createMetadata(MultipartFile multipartFile) {
        String originalFilename = String.valueOf(multipartFile.getOriginalFilename());
        String originalType = String.valueOf(multipartFile.getContentType());
        String fileSize = String.valueOf(multipartFile.getSize());
        return new Document()
                .append("originalFilename", originalFilename)
                .append("fileSize", fileSize)
                .append("fileType", originalType);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
