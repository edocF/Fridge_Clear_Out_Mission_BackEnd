package com.example.backend.promptStrategy;

import com.example.backend.pojo.FoodInfoForRecipe;
import com.example.backend.pojo.RecipeOption;

import java.util.List;

public class GenerateRecipeStrategy implements PromptStrategy {

    @Override
    public String buildPrompt(Object... params) {
        RecipeOption option = (RecipeOption) params[0];
        List<FoodInfoForRecipe> foodList = (List<FoodInfoForRecipe>) params[1];
        StringBuilder prompt = new StringBuilder();

        // 添加参数信息
        prompt.append("{\"dietOptions\": \"").append(option.getDietOptions()).append("\", ");
        prompt.append("\"cuisineOptions\": \"").append(option.getCuisineOptions()).append("\", ");
        prompt.append("\"timeOptions\": \"").append(option.getTimeOptions()).append("\", ");
        prompt.append("\"foodNames\": [");

        for (int i = 0; i < foodList.size(); i++) {
            prompt.append("\"").append(foodList.get(i).getName()).append("\"");
            if (i < foodList.size() - 1) {
                prompt.append(", ");
            }
        }

        prompt.append("]}\n\n");

        // 添加固定格式要求
        prompt.append("请根据上述信息，生成3份不同的完整食谱，每份食谱需使用部分或全部食材，以不同组合方式呈现,但在最后的三份食谱中最好覆盖所有食材。每个食谱需包含以下固定格式：\n\n");
        prompt.append("【食谱名称】（必须是具体菜名，例如：宫保鸡丁、番茄炒蛋）\n");
        prompt.append("【适用场景】：[").append(option.getDietOptions()).append("]\n");
        prompt.append("【菜系风格】：[").append(option.getCuisineOptions()).append("]\n");
        prompt.append("【烹饪时长】：[").append(option.getTimeOptions()).append("]\n\n");
        prompt.append("【食材清单】：\n");
        prompt.append("- 列出具体食材及用量\n\n");
        prompt.append("【烹饪步骤】：\n");
        prompt.append("1. 详细描述每一步操作\n\n");
        prompt.append("【营养分析】：\n");
        prompt.append("- 热量：约XX-XX大卡\n");
        prompt.append("- 蛋白质：约XX-XX克\n");
        prompt.append("- 碳水：约XX-XX克\n");
        prompt.append("- 脂肪：约XX-XX克\n\n");
        prompt.append("【小贴士】：\n");
        prompt.append("1. 提供实用的烹饪建议\n");

        return prompt.toString();
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
