package com.example.mongo.mongo_practice.service.impl;

import com.example.mongo.mongo_practice.dto.*;
import com.example.mongo.mongo_practice.entity.Menu;
import com.example.mongo.mongo_practice.entity.Order;
import com.example.mongo.mongo_practice.entity.Restaurant;
import com.example.mongo.mongo_practice.entity.UserData;
import com.example.mongo.mongo_practice.enums.Status;
import com.example.mongo.mongo_practice.exception.DuplicateDataException;
import com.example.mongo.mongo_practice.exception.NotFoundException;
import com.example.mongo.mongo_practice.repo.MenuRepo;
import com.example.mongo.mongo_practice.repo.OrderRepo;
import com.example.mongo.mongo_practice.repo.RestaurantRepo;
import com.example.mongo.mongo_practice.repo.UserRepo;
import com.example.mongo.mongo_practice.response.OrderResponse;
import com.example.mongo.mongo_practice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 18-12-2023
 */
@Service
public class OrderserviceImpl implements OrderService {
    @Autowired
    RestaurantRepo restaurantRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    MenuRepo menuRepo;
    @Autowired
    OrderRepo orderRepo;
    @Override
    public ApiResponse save(OrderDto orderDto) {
        if(orderDto.getId()!=null){
          return update(orderDto);
        }else{
          Order order=dtoToOrder(orderDto);
          orderRepo.save(order);
          return new ApiResponse(HttpStatus.OK.value(), "order save successfully", orderTODto(order));
        }
    }

    public ApiResponse update(OrderDto orderDto){
        Optional<Order> optional=orderRepo.findById(orderDto.getId());
        if(optional.isEmpty()) throw new NotFoundException("order is not found");
        Order order=optional.get();
        order.setLocalTime(LocalDateTime.now());
        order.setStatus(orderDto.getStatus());
        order.setRestaurant(restaurantRepo.findById(orderDto.getRestaurant()).orElseThrow(()-> new NotFoundException("restaurant not found with this given id")));
        order.setMenu(menuRepo.findById(orderDto.getMenu()).orElseThrow(()->new NotFoundException("menu is not available")));
        order.setUserData(userRepo.findById(orderDto.getUserData()).orElseThrow(()->new NotFoundException("user not found")));
        order.setOrderLocation(orderDto.getOrderLocation());
        return new ApiResponse(HttpStatus.OK.value(), "order updated successfully", orderTODto(order));
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


    public Order dtoToOrder(OrderDto orderDto){
        Order order=new Order();
        order.setLocalTime(LocalDateTime.now());
        order.setStatus(Status.CONFIRMED);
        order.setRestaurant(restaurantRepo.findById(orderDto.getRestaurant()).orElseThrow(()-> new NotFoundException("restaurant not found with this given id")));
        order.setMenu(menuRepo.findById(orderDto.getMenu()).orElseThrow(()->new NotFoundException("menu is not available")));
        order.setUserData(userRepo.findById(orderDto.getUserData()).orElseThrow(()->new NotFoundException("user not found")));
        order.setOrderLocation(orderDto.getOrderLocation());
        return order;
    }
    public UserDto userToDto(UserData userData){
        UserDto userDto=new UserDto();
        userDto.setId(userData.getId());
        userDto.setName(userData.getName());
        userDto.setEmail(userData.getEmail());
        userDto.setPassword(userData.getPassword());
        userDto.setLocation(userData.getLocation());
        userDto.setRole(userData.getRole());
        userDto.setContact(userData.getContact());
        return userDto;
    }
    private MenuDto menuToDto(Menu menuDto) {
        MenuDto menu=new MenuDto();
        menu.setId(menuDto.getId());
        menu.setName(menuDto.getName());
        menu.setType(menuDto.getType());
        menu.setCost(menuDto.getCost());
        menu.setRestaurantDto(menu.getRestaurantDto());
        return menu;
    }

    private RestaurantDto restaurantToDto(Restaurant restaurant) {
        RestaurantDto restaurantDto=new RestaurantDto();
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setLocation(restaurant.getLocation());
        restaurantDto.setEmail(restaurant.getEmail());
        restaurantDto.setPassword(restaurantDto.getPassword());
        return restaurantDto;
    }

}
