package com.example.backend.service.impl;

import com.example.backend.exception.ExpirationWarningException;
import com.example.backend.mapper.FoodMapper;
import com.example.backend.mapper.FridgeMapper;
import com.example.backend.pojo.Food;
import com.example.backend.service.ExpirationWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.backend.utils.CurrentHold;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class ExpirationWarningServiceImpl implements ExpirationWarningService {


    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private FridgeMapper fridgeMapper;

    public void checkAndWarnExpiration(Food food) throws ExpirationWarningException {
        if (food.getManufactureDate() == null || food.getExpiryDuration() == null) {
            throw new ExpirationWarningException("缺少生产日期或保质期信息");
        }
        long remainingDays = calculateRemainingDays(food);
        food.setExpired(remainingDays <= 0);
        food.setExpSoon(remainingDays > 0 && remainingDays <= 3);
    }

    @Override
    public long calculateRemainingDays(Food food) throws ExpirationWarningException {
        try {
            if (food.getManufactureDate() == null || food.getExpiryDuration() == null) {
                throw new ExpirationWarningException("缺少生产日期或保质期信息");
            }
            LocalDate manufactureDate = food.getManufactureDate().toLocalDate();
            LocalDate expiryDate = manufactureDate.plusDays(food.getExpiryDuration());
            return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
        } catch (Exception e) {
            throw new ExpirationWarningException("计算剩余天数失败: " + e.getMessage(), e);
        }
    }

    //每晚12点更新食物状态
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void batchUpdateFoodState() {
        List<Food> foods = foodMapper.getAllFoodList();
        for (Food food : foods) {
            try {
                checkAndWarnExpiration(food);
            } catch (ExpirationWarningException e) {
                log.error("更新食物状态失败: {}", e.getMessage());
            }
        }
        return;
    }

    @Override
    public List<Food> getExpiringSoonAndExpiredFoods() {
        Integer fridgeId = fridgeMapper.selectByUserId(getCurrentUserId());
         return  foodMapper.getExpiringSoonAndExpiredFoods(fridgeId);
    }

    public Integer getCurrentUserId() {
        return CurrentHold.getCurrentUserId();
    }

}