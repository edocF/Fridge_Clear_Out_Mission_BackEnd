package com.example.backend.service.impl;

import com.example.backend.mapper.FoodConsumptionMapper;
import com.example.backend.mapper.FoodMapper;
import com.example.backend.mapper.FridgeMapper;
import com.example.backend.pojo.Food;
import com.example.backend.pojo.FoodConsumption;
import com.example.backend.pojo.FoodInfoForUpdate;
import com.example.backend.service.FoodConsumptionService;
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
    @Autowired
    private FoodConsumptionService foodConsumptionService;
    @Override
    public void add(Food food) {
        food.setCreateTime(LocalDateTime.now());
        food.setExpiryDate(LocalDateTime.now().plusDays(food.getExpiryDuration()));
        food.setUpdateTime(LocalDateTime.now());
        food.setManufactureDate(LocalDateTime.now());
        food.setExpSoon(false);
        food.setExpired(false);
        Integer fridgeId = fridgeMapper.selectByUserId(getCurrentUserId());
        food.setFridgeId(fridgeId);
        food.setNumber(5);
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
        Integer foodId = foodInfoForUpdate.getId();
        Food oldFood = foodMapper.getFoodById(foodId);
        if (oldFood == null) return;
        int oldNumber = oldFood.getNumber() == null ? 0 : oldFood.getNumber();
        int newNumber = foodInfoForUpdate.getNumber() == null ? 0 : foodInfoForUpdate.getNumber();
        int consumed = oldNumber - newNumber;
        if (consumed > 0) {
            // 记录消耗
            FoodConsumption consumption = FoodConsumption.builder()
                    .userId(getCurrentUserId())
                    .foodId(foodId)
                    .foodName(oldFood.getName())
                    .foodEmoji(oldFood.getEmoji())
                    .consumedAmount((double) consumed)
                    .kcal(oldFood.getKcal() == null ? 0 : oldFood.getKcal() * consumed / (oldFood.getNumber() == null ? 1 : oldFood.getNumber()))
                    .carb(oldFood.getCarb() == null ? 0 : oldFood.getCarb() * consumed / (oldFood.getNumber() == null ? 1 : oldFood.getNumber()))
                    .protein(oldFood.getProtein() == null ? 0 : oldFood.getProtein() * consumed / (oldFood.getNumber() == null ? 1 : oldFood.getNumber()))
                    .fat(oldFood.getFat() == null ? 0 : oldFood.getFat() * consumed / (oldFood.getNumber() == null ? 1 : oldFood.getNumber()))
                    .consumedTime(LocalDateTime.now())
                    .build();
            foodConsumptionService.recordConsumption(consumption);
        }
        if(newNumber == 0) {
            List<Integer> ids = new ArrayList<>();
            ids.add(foodInfoForUpdate.getId());
            deleteFood(ids);
        } else {
            Integer fridgeId = fridgeMapper.selectByUserId(getCurrentUserId());
            log.info("fridgeId: " + fridgeId);
            foodMapper.updateFood(foodInfoForUpdate.getId(), foodInfoForUpdate.getNumber(), fridgeId);
        }
    }

    @Override
    public List<Food> getValidFood() {
        Integer fridgeId = fridgeMapper.selectByUserId(getCurrentUserId());
        List<Food> foods = foodMapper.getValidFoodList(fridgeId);
        return List.of();
    }

    private Integer getCurrentUserId() {
        return CurrentHold.getCurrentUserId();
    }
}
