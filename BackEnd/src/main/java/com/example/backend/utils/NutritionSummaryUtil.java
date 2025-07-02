package com.example.backend.utils;

import com.example.backend.pojo.FoodConsumption;
import com.example.backend.pojo.NutritionSummary;
import java.util.List;

public class NutritionSummaryUtil {
    public static NutritionSummary fromConsumptions(List<FoodConsumption> list) {
        NutritionSummary summary = new NutritionSummary();
        for (FoodConsumption fc : list) {
            summary.setTotalKcal(summary.getTotalKcal() + (fc.getKcal() == null ? 0 : fc.getKcal()));
            summary.setTotalCarb(summary.getTotalCarb() + (fc.getCarb() == null ? 0 : fc.getCarb()));
            summary.setTotalProtein(summary.getTotalProtein() + (fc.getProtein() == null ? 0 : fc.getProtein()));
            summary.setTotalFat(summary.getTotalFat() + (fc.getFat() == null ? 0 : fc.getFat()));
        }
        return summary;
    }
} 