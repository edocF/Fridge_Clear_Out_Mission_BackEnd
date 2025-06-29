package com.example.backend.controller;

import com.example.backend.pojo.Food;
import com.example.backend.pojo.FoodInfo;
import com.example.backend.pojo.Result;
import com.example.backend.service.AICallService;
import com.example.backend.service.FoodService;
import com.example.backend.utils.CurrentHold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;
    @Autowired
    private AICallService callService;

    @PostMapping()
    public Result createFood(@RequestBody FoodInfo foodInfo) {
        Food food = new Food();
        Food foodNutrition = callService.getFoodNutrition(foodInfo.getName());
        foodNutrition.setNumber(foodInfo.getNumber());
        foodNutrition.setName(foodInfo.getName());
        log.info("foodInfoNumber: " + foodInfo.getNumber());
        log.info("foodNutrition: " + foodNutrition);
        foodService.add(foodNutrition);
        return Result.success();
    }


}
