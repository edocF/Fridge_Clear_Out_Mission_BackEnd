package com.example.backend.service;

import com.example.backend.exception.AIException;
import com.example.backend.pojo.Food;

import java.util.List;

public interface AICallService {

    /**
     * 获取指定食物名称的营养信息。
     * @param foodName 食物名称
     * @return Food 食物的营养信息
     * @throws AIException AI调用异常
     */
    Food getFoodNutrition(String foodName) throws AIException;

    /**
     * 生成食物保鲜报告。
     * 该方法会将用户冰箱中的所有食物信息传递给AI，
     * 由AI生成一份详细的保鲜分析报告。
     * @param foods 食物列表
     * @return String 由AI生成的报告文本
     */
    String generateFreshnessReport(List<Food> foods);
}
