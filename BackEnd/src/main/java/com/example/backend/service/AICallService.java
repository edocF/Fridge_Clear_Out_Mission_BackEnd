package com.example.backend.service;

import com.example.backend.exception.AIException;
import com.example.backend.pojo.Food;

public interface AICallService {

    Food getFoodNutrition(String foodName) throws AIException;
}
