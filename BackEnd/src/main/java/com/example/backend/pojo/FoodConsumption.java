package com.example.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodConsumption {
    private Integer id;
    private Integer userId;
    private Integer foodId;
    private String foodName;
    private String foodEmoji;
    private Double consumedAmount;
    private Double kcal;
    private Double carb;
    private Double protein;
    private Double fat;
    private LocalDateTime consumedTime;
} 