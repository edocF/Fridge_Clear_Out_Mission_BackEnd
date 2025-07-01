package com.example.backend.pojo;

import com.example.backend.pojo.FoodInfoForRecipe;
import com.example.backend.pojo.RecipeOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequest {
     public RecipeOption recipeOption;
     public List<FoodInfoForRecipe> foodInfoForRecipes;
}