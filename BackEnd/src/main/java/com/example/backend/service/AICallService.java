package com.example.backend.service;

import com.example.backend.exception.AIException;
import com.example.backend.pojo.Food;
import org.springframework.web.multipart.MultipartFile;

public interface AICallService {

    Food getFoodNutrition(String foodName) throws AIException;

    Food getFoodInfoByImage(MultipartFile file) throws Exception;
}
