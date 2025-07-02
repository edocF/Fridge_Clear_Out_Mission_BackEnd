package com.example.backend.promptStrategy;

import com.example.backend.pojo.FoodConsumption;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * GenerateNutritionReportStrategy
 * 用于生成膳食营养报告的AI提示词（Prompt）
 * 实现 PromptStrategy 接口，负责将消耗记录和营养统计转为结构化分析指令
 */
@Component
public class GenerateNutritionReportStrategy implements PromptStrategy {
    @Override
    public String buildPrompt(Object... params) {
        if (params.length == 0 || !(params[0] instanceof List)) {
            throw new IllegalArgumentException("请传入消耗记录 List<FoodConsumption> 作为参数");
        }
        @SuppressWarnings("unchecked")
        List<FoodConsumption> consumptions = (List<FoodConsumption>) params[0];
        // Object nutritionSummary = params.length > 1 ? params[1] : null;
        StringBuilder sb = new StringBuilder();
        sb.append("你是一名专业的营养师和健康顾问。请根据用户最近的食物消耗记录，生成一份详细的中文膳食营养报告，帮助用户了解自己的饮食习惯是否健康、营养是否均衡，并给出科学建议。\n");
        sb.append("请严格按照如下格式输出：\n\n");
        sb.append("【分析报告】\n");
        sb.append("2. 【营养摄入分析】：分析热量、蛋白质、碳水、脂肪等摄入是否均衡，是否有过量或不足。\n");
        sb.append("3. 【饮食习惯建议】：针对发现的问题，给出1-2条具体、科学的饮食建议。\n");
        sb.append("4. 风格应亲切、鼓励，避免批评。\n");
        sb.append("【chartData】\n");
        sb.append("请输出如下JSON（仅chartData部分）：\n");
        sb.append("{\n");
        sb.append("  \"蛋白质\": 总蛋白质摄入量,\n");
        sb.append("  \"脂肪\": 总脂肪摄入量,\n");
        sb.append("  \"碳水\": 总碳水摄入量,\n");
        sb.append("  \"热量\": 总热量摄入量\n");
        sb.append("}\n");
        sb.append("chartData部分为各项营养素的摄入量（单位：克或千卡），便于前端生成饼图。\n");
        sb.append("以下是用户的食物消耗数据，数据格式为：\n");
        sb.append("[\n");
        for (FoodConsumption fc : consumptions) {
            sb.append(String.format("  {\"foodName\": \"%s\", ", fc.getFoodName()));
            sb.append(String.format("\"consumedAmount\": %.2f, ", fc.getConsumedAmount()));
            sb.append(String.format("\"kcal\": %.2f, ", fc.getKcal()));
            sb.append(String.format("\"carb\": %.2f, ", fc.getCarb()));
            sb.append(String.format("\"protein\": %.2f, ", fc.getProtein()));
            sb.append(String.format("\"fat\": %.2f, ", fc.getFat()));
            sb.append(String.format("\"consumedTime\": \"%s\"", fc.getConsumedTime() != null ? fc.getConsumedTime().toLocalDate() : "未知"));
            sb.append("},\n");
        }
        sb.append("]\n\n");
        sb.append("请开始分析并生成报告。\n");
        return sb.toString();
    }

    @Override
    public Object parseContent(String content, Object... params) {
        return content;
    }

    @Override
    public Object buildMessage(Object... params) {
        return null;
    }
}