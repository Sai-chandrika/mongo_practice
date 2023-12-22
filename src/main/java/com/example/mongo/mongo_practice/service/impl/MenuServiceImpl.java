package com.example.mongo.mongo_practice.service.impl;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.MenuDto;
import com.example.mongo.mongo_practice.dto.RestaurantDto;
import com.example.mongo.mongo_practice.dto.Validation;
import com.example.mongo.mongo_practice.entity.Menu;
import com.example.mongo.mongo_practice.entity.Restaurant;
import com.example.mongo.mongo_practice.exception.DuplicateDataException;
import com.example.mongo.mongo_practice.exception.NotFoundException;
import com.example.mongo.mongo_practice.repo.MenuRepo;
import com.example.mongo.mongo_practice.repo.RestaurantRepo;
import com.example.mongo.mongo_practice.response.MenuResponse;
import com.example.mongo.mongo_practice.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepo menuRepo;
    @Autowired
    RestaurantRepo restaurantRepo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ApiResponse menuAdd(MenuDto menuDto) {
        if(menuDto.getId()!=null){
            return updateMenu(menuDto);
        }else{
            Menu menu=menuDtoToMenu(menuDto);
            menuRepo.save(menu);
            return new ApiResponse(HttpStatus.OK.value(), "menu save successfully :)", menuToResponce(menu));
        }
    }

    public ApiResponse updateMenu(MenuDto menuDto){
        Optional<Menu> optional=menuRepo.findById(menuDto.getId());
        if(optional.isEmpty()) throw new NotFoundException(" menu is not found with this given id :(");
        Menu menu=optional.get();
        if(menuDto.getType()!=null){
            menu.setType(menuDto.getType());
        }
        if(menuDto.getName()!=null){
            menu.setName(menuDto.getName());
        }
        if(menuDto.getCost()==0){
            menu.setCost(menuDto.getCost());
        }
        if(menuDto.getRestaurantDto()!=null) {
            menu.setRestaurant(restaurantRepo.findById(menuDto.getId()).orElseThrow(() -> new NotFoundException("restaurant is not found with given this id")));
        }
        menuRepo.save(menu);
        return new ApiResponse(HttpStatus.OK.value(), "menu Updated successfully :)", menuToResponce(menu));
    }



    private Menu menuDtoToMenu(MenuDto menuDto) {
        Menu menu=new Menu();
        menu.setName(menuDto.getName());
        menu.setType(menuDto.getType());
        menu.setCost(menuDto.getCost());
        menu.setRestaurant(restaurantRepo.findById(menuDto.getRestaurantDto()).orElseThrow(()->new NotFoundException("restaurant is not found with given this id")));
        return menu;
    }


    @Override
    public ApiResponse getAll(String restaurantId){
        List<Menu> menus=menuRepo.findAllByRestaurantId(restaurantId);
        return new ApiResponse(HttpStatus.OK.value(), " " + restaurantId + " restaurant  menu list ",menus);
    }
    private MenuResponse menuToResponce(Menu menu){
        MenuResponse menuDto=new MenuResponse();
        menuDto.setId(menu.getId());
        menuDto.setName(menu.getName());
        menuDto.setType(menu.getType());
        menuDto.setCost(menu.getCost());
        menuDto.setRestaurantDto(restaurantToDto(menu.getRestaurant()));
        return menuDto;
    }

    private RestaurantDto restaurantToDto(Restaurant restaurant) {
        RestaurantDto restaurantDto=new RestaurantDto();
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setLocation(restaurant.getLocation());
        restaurantDto.setEmail(restaurant.getEmail());
        restaurantDto.setPassword(bCryptPasswordEncoder.encode(restaurant.getPassword()));
        return restaurantDto;
    }

}
