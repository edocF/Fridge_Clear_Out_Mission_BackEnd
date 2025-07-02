package com.example.backend.service.impl;

import com.example.backend.pojo.Food;
import com.example.backend.service.FoodService;
import com.example.backend.service.ReportService;
import com.example.backend.service.AICallService;
import com.example.backend.service.FoodConsumptionService;
import com.example.backend.pojo.FoodConsumption;
import com.example.backend.pojo.NutritionReportRequest;
import com.example.backend.pojo.NutritionSummary;
import com.example.backend.utils.NutritionSummaryUtil;
import com.example.backend.utils.CurrentHold;
import com.example.backend.pojo.ConsumptionReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ReportServiceImpl
 * 负责实现 ReportService 接口，生成各类报告。
 * 目前实现生成食物保鲜报告的功能。
 */
@Service
public class ReportServiceImpl implements ReportService {

    // 注入 FoodService，用于获取当前用户冰箱中的所有食物信息
    @Autowired
    private FoodService foodService;

    // 注入 AI 服务，用于调用 AI 生成报告
    @Autowired
    private AICallService aiCallService;

    // 注入 FoodConsumptionService，用于获取食物消耗记录
    @Autowired
    private FoodConsumptionService foodConsumptionService;

    /**
     * 生成当前用户的食物保鲜报告。
     * 1. 获取所有食物信息
     * 2. 调用 AI 服务生成报告
     * 3. 返回报告文本
     *
     * @return String 由AI生成的报告文本
     */
    @Override
    public String generateFreshnessReport() {
        // 第一步：获取当前用户冰箱中的所有食物信息
        List<Food> foodList = foodService.getFoodList();

        // 第二步：调用 AI 服务生成报告
        // 这里假设 AICallService 有 generateFreshnessReport 方法，实际项目中需补充实现
        // 你可以在 AICallServiceImpl 里实现该方法，调用对应的 PromptStrategy
        String report = aiCallService.generateFreshnessReport(foodList);

        // 第三步：返回报告文本
        return report;
    }

    /**
     * 生成当前用户的营养报告。
     * 1. 获取当前用户的消耗记录（如最近一周，可后续扩展为参数）
     * 2. 汇总营养数据（可用NutritionSummary类，后续可扩展）
     * 3. 调用AI服务生成报告
     * 4. 返回AI报告
     *
     * @return String 由AI生成的营养报告文本
     */
    @Override
    public String generateNutritionReport(NutritionReportRequest request) {
        // 1. 获取当前用户的消耗记录（如最近一周
        Integer userId = getCurrentUserId();
        List<FoodConsumption> consumptions = foodConsumptionService.getConsumptionsByUserAndTime(userId, request.getStart(), request.getEnd());
        // 2. 汇总营养数据（可用NutritionSummary类，后续可扩展）
         NutritionSummary summary = NutritionSummaryUtil.fromConsumptions(consumptions);//NutritionSummaryUtil.fromConsumptions(consumptions);
        // 3. 调用AI服务生成报告
        String report = aiCallService.generateNutritionReport(consumptions, summary);
        // 4. 返回报告
        return report;
    }

    @Override
    public String generateConsumptionReport(ConsumptionReportRequest request) {
        // 1. 获取当前用户ID（如有需要）
        Integer userId = getCurrentUserId();
        // 2. 查询指定时间范围内的消耗记录
        List<FoodConsumption> consumptions = foodConsumptionService.getConsumptionsByUserAndTime(userId, request.getStart(), request.getEnd());
        // 3. 调用AI服务生成消耗报告（需在AICallService中实现对应方法）
        String report = aiCallService.generateConsumptionReport(consumptions);
        // 4. 返回报告
        return report;
    }

    private Integer getCurrentUserId() {
        return CurrentHold.getCurrentUserId();
    }
}