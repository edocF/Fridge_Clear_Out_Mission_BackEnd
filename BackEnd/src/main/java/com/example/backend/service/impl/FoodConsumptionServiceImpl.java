package com.example.backend.service.impl;

import com.example.backend.mapper.FoodConsumptionMapper;
import com.example.backend.pojo.FoodConsumption;
import com.example.backend.service.FoodConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FoodConsumptionServiceImpl implements FoodConsumptionService {
    @Autowired
    private FoodConsumptionMapper foodConsumptionMapper;

    @Override
    public void recordConsumption(FoodConsumption consumption) {
        foodConsumptionMapper.insertConsumption(consumption);
    }

    @Override
    public List<FoodConsumption> getConsumptionsByUserAndTime(Integer userId, LocalDateTime start, LocalDateTime end) {
        return foodConsumptionMapper.getConsumptionsByUserAndTime(userId, start, end);
    }
} 