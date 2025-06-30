package com.example.backend.service;

import com.example.backend.pojo.Food;

import java.util.List;

public interface FoodService {

    void add(Food food);

    List<Food> getFoodList();

    void deleteFood(List<Integer> ids);
}
