package com.example.backend.promptStrategy;

import com.example.backend.pojo.FoodConsumption;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * GenerateConsumptionReportStrategy
 * 用于生成食物消耗报告的AI提示词（Prompt）
 * 实现 PromptStrategy 接口，负责将消耗记录转为结构化分析指令
 */
@Component
public class GenerateConsumptionReportStrategy implements PromptStrategy {
    @Override
    public String buildPrompt(Object... params) {
        if (params.length == 0 || !(params[0] instanceof List)) {
            throw new IllegalArgumentException("请传入消耗记录 List<FoodConsumption> 作为参数");
        }
        @SuppressWarnings("unchecked")
        List<FoodConsumption> consumptions = (List<FoodConsumption>) params[0];
        StringBuilder sb = new StringBuilder();
        sb.append("你是一名专业的健康顾问。请根据用户最近的食物消耗记录，生成一份详细的中文消耗报告，重点分析用户冰箱中已经消耗掉的主要食物类别，并给出补货建议。报告应帮助用户了解：1）近期消耗最多的食物大类（如蔬菜、肉类、海鲜、水果、乳制品、主食等）；2）冰箱中哪些类别的食物已经消耗殆尽或明显减少，建议及时补充；3）整体消耗习惯是否均衡，食物种类是否丰富。请避免与营养分析重合，突出消耗和补货视角。\n");
        sb.append("请严格按照如下格式输出：\n\n");
        sb.append("【消耗报告】\n");
        sb.append("1. 【消耗大类分析】：统计并分析用户在所选时间段内消耗最多的食物大类（如蔬菜、肉类等），并简要说明各类食物的消耗情况。\n");
        sb.append("2. 【补货建议】：根据消耗数据，指出冰箱中哪些类别的食物已经消耗殆尽或明显减少，建议用户补充哪些食物类别，并可举例推荐具体食材。\n");
        sb.append("3. 【消耗习惯分析】：分析用户的食物消耗种类是否丰富，是否存在某类食物消耗过多或过少的情况。例如：'您的食物种类较为丰富，涵盖了蔬菜、肉类、海鲜、水果等，这是一个健康的饮食习惯。'\n");
        sb.append("4. 风格应亲切、鼓励，避免批评。\n");
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