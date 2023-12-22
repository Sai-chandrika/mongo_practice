package com.example.mongo.mongo_practice.controller;

import com.example.mongo.mongo_practice.dto.ApiResponse;
import com.example.mongo.mongo_practice.dto.MenuDto;
import com.example.mongo.mongo_practice.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 15-12-2023
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    MenuService menuService;

    @PostMapping("/menu-add")
    public ApiResponse menuAdd(@RequestBody MenuDto menuDto){
        return menuService.menuAdd(menuDto);

    }

    @GetMapping("/get-all-menu/{restaurantId}")
    public ApiResponse getAll(@PathVariable String restaurantId){
        return menuService.getAll(restaurantId);
    }
}
