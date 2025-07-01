package com.example.backend.controller;

import com.example.backend.pojo.FoodInfoForRecipe;
import com.example.backend.pojo.RecipeOption;
import com.example.backend.pojo.RecipeRequest;
import com.example.backend.pojo.Result;
import com.example.backend.service.AICallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    private AICallService callService;
    @GetMapping()
    public Result generateRecipe(@RequestBody RecipeRequest recipeRequest) {
        try {
            // 参数校验
            if (recipeRequest.foodInfoForRecipes == null || recipeRequest.foodInfoForRecipes.isEmpty()) {
                return Result.error("食材列表不能为空");
            }
            log.info("foodInfoForRecipes: {}", recipeRequest.foodInfoForRecipes);
            String recipe = callService.generateRecipe(recipeRequest.recipeOption, recipeRequest.foodInfoForRecipes);
            return Result.success(recipe);
        } catch (Exception e) {
            // 记录日志
            e.printStackTrace();
            return Result.error("生成食谱失败: " + e.getMessage());
        }
    }
}
