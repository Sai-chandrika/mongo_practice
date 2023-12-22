package com.example.mongo.mongo_practice.service.impl;

import com.example.mongo.mongo_practice.dto.*;
import com.example.mongo.mongo_practice.entity.*;
import com.example.mongo.mongo_practice.exception.NotFoundException;
import com.example.mongo.mongo_practice.repo.*;
import com.example.mongo.mongo_practice.response.OrderResponse;
import com.example.mongo.mongo_practice.response.ReviewResponse;
import com.example.mongo.mongo_practice.service.FileUploadService;
import com.example.mongo.mongo_practice.service.ReviewService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    ReviewRepo reviewRepo;
    @Autowired
    FileDocumentRepo fileDocumentRepo;
    @Autowired
    FileUploadService uploadService;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    RestaurantRepo restaurantRepo;
    @Autowired
    MenuRepo menuRepo;
    @Autowired
    UserRepo userRepo;

    @Override
    public ApiResponse saveReview(ReviewDto reviewDto) {
        if (reviewDto.getId() != null) {
            return update(reviewDto);
        } else {
            Review review = dtoToReview(reviewDto);
            reviewRepo.save(review);
            return new ApiResponse(HttpStatus.OK.value(), "review save successfully",entityToDto( review));
        }
    }

public  ReviewResponse entityToDto(Review review) {
    ReviewResponse reviewResponse = new ReviewResponse();
    reviewResponse.setId(review.getId());
    reviewResponse.setReview(review.getReview());
    reviewResponse.setFiles(getFileDocuments(review.getFiles()));
    reviewResponse.setRating(review.getRating());
    reviewResponse.setOrderDto(orderTODto(review.getOrder()));
    return reviewResponse;
}

    private  List<FileDocument> getFileDocuments(List<String> fileIds) {
        List<FileDocument> fileDocuments = new ArrayList<>();
        for (String fileId : fileIds) {
            FileDocument fileDocument = new FileDocument();
            fileDocument =uploadService.getFileDetails(fileId);
            fileDocuments.add(fileDocument);
        }
        return fileDocuments;
    }


    public ApiResponse update(ReviewDto reviewDto) {
        List<String> fileList = new ArrayList<>();
        Optional<Review> optional = reviewRepo.findById(reviewDto.getId());
        if (optional.isEmpty()) throw new NotFoundException("review is not found with given this id");
        Review review = optional.get();
        review.setReview(reviewDto.getReview());
        review.setRating(reviewDto.getRating());
        review.setOrder(orderRepo.findById(reviewDto.getOrderDto()).orElseThrow(() -> new NotFoundException("order not found")));
        if (!reviewDto.getFiles().isEmpty()) {
            for (String file : reviewDto.getFiles()) {
                GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(file)));
                if (gridFSFile != null && gridFSFile.getMetadata() != null) {
                    fileList.add(file);
                } else throw new NotFoundException("No file content found with given id");
            }
            review.setFiles(fileList);
        }
        reviewRepo.save(review);
        return new ApiResponse(HttpStatus.OK.value(), "review update successfully", review);
    }

    private Review dtoToReview(ReviewDto reviewDto) {
        List<String> fileList = new ArrayList<>();
        Review review = new Review();
        review.setReview(reviewDto.getReview());
        review.setRating(reviewDto.getRating());
        review.setOrder(orderRepo.findById(reviewDto.getOrderDto()).orElseThrow(() -> new NotFoundException("order not found")));
        if (!reviewDto.getFiles().isEmpty()) {
            for (String file : reviewDto.getFiles()) {
                GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(file)));
                if (gridFSFile != null && gridFSFile.getMetadata() != null) {
                    fileList.add(file);
                } else throw new NotFoundException("No file content found with given id");
            }
            review.setFiles(fileList);
        }
        return review;
    }

    public OrderResponse orderTODto(Order order){
        OrderResponse orderResponse=new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setLocalTime(order.getLocalTime());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setRestaurant(restaurantToDto(order.getRestaurant()));
        orderResponse.setMenu(menuToDto(order.getMenu()));
        orderResponse.setUserData(userToDto(order.getUserData()));
        orderResponse.setOrderLocation(order.getOrderLocation());
        return orderResponse;
    }
    public UserDto userToDto(UserData userData){
        UserDto userDto=new UserDto();
        userDto.setId(userData.getId());
        userDto.setName(userData.getName());
//        userDto.setEmail(userData.getEmail());
//        userDto.setPassword(userData.getPassword());
//        userDto.setLocation(userData.getLocation());
//        userDto.setRole(userData.getRole());
//        userDto.setContact(userData.getContact());
        return userDto;
    }
    private MenuDto menuToDto(Menu menuDto) {
        MenuDto menu=new MenuDto();
        menu.setId(menuDto.getId());
        menu.setName(menuDto.getName());
        menu.setType(menuDto.getType());
        menu.setCost(menuDto.getCost());
//        menu.setRestaurantDto(menu.getRestaurantDto());
        return menu;
    }

    private RestaurantDto restaurantToDto(Restaurant restaurant) {
        RestaurantDto restaurantDto=new RestaurantDto();
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setLocation(restaurant.getLocation());
//        restaurantDto.setEmail(restaurant.getEmail());
//        restaurantDto.setPassword(restaurantDto.getPassword());
        return restaurantDto;
    }

}


