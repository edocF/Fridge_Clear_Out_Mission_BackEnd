package com.example.backend.service.impl;

import com.example.backend.mapper.FoodMapper;
import com.example.backend.mapper.FridgeMapper;
import com.example.backend.pojo.Food;
import com.example.backend.pojo.FoodInfoForUpdate;
import com.example.backend.service.FoodService;
import com.example.backend.utils.CurrentHold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private FridgeMapper fridgeMapper;
    @Override
    public void add(Food food) {
        food.setCreateTime(LocalDateTime.now());
        food.setExpiryDate(LocalDateTime.now().plusDays(food.getExpiryDuration()));
        food.setUpdateTime(LocalDateTime.now());
        food.setExpSoon(false);
        food.setExpired(false);
        Integer fridgeId = fridgeMapper.selectByUserId(getCurrentUserId());
        food.setFridgeId(fridgeId);
        log.info("Food added: {}", food);
        foodMapper.add(food);
    return;
    }

    @Override
    public List<Food> getFoodList() {
        Integer fridgeId = fridgeMapper.selectByUserId(getCurrentUserId());
        return foodMapper.getFoodList(fridgeId);
    }

    @Override
    public void deleteFood(List<Integer> ids) {
        Integer fridgeId = fridgeMapper.selectByUserId(getCurrentUserId());
        foodMapper.deleteFood(fridgeId,ids);
    }

    @Override
    public void updateFood(FoodInfoForUpdate foodInfoForUpdate) {
         if(foodInfoForUpdate.getNumber() == 0) {
             List<Integer> ids = new ArrayList<>();
             ids.add(foodInfoForUpdate.getId());
             deleteFood(ids);
         } else {
             Integer fridgeId = fridgeMapper.selectByUserId(getCurrentUserId());
             log.info("fridgeId: " + fridgeId);
             foodMapper.updateFood(foodInfoForUpdate.getId(), foodInfoForUpdate.getNumber(), fridgeId);
         }
    }

    private Integer getCurrentUserId() {
        return CurrentHold.getCurrentUserId();
    }
}
