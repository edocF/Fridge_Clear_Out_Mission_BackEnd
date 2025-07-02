package com.example.backend.service;

import com.example.backend.pojo.FoodConsumption;

import java.time.LocalDateTime;
import java.util.List;

public interface FoodConsumptionService {
    // 插入一条消耗记录
    void recordConsumption(FoodConsumption consumption);

    // 查询某用户在某时间段内的所有消耗记录
    List<FoodConsumption> getConsumptionsByUserAndTime(Integer userId, LocalDateTime start, LocalDateTime end);
} 