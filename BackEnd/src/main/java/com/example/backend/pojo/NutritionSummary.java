package com.example.backend.pojo;

import lombok.Data;

@Data
public class NutritionSummary {
    private double totalKcal;
    private double totalCarb;
    private double totalProtein;
    private double totalFat;
    // 可扩展更多字段
}