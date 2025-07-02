package com.example.backend.service;

import com.example.backend.exception.AIException;
import com.example.backend.pojo.Food;
import com.example.backend.pojo.FoodInfoForRecipe;
import com.example.backend.pojo.RecipeOption;
import com.example.backend.pojo.FoodConsumption;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AICallService {

    Food getFoodNutrition(String foodName) throws AIException;

    Food getFoodInfoByImage(MultipartFile file) throws Exception;

    String generateFreshnessReport(List<Food> foodList);

    String generateRecipe(RecipeOption recipeOption, List<FoodInfoForRecipe> foodInfoForRecipe) throws AIException;

    String generateNutritionReport(List<FoodConsumption> consumptions, Object nutritionSummary);

    String generateConsumptionReport(List<FoodConsumption> consumptions);
}
