package com.example.backend.promptStrategy;

import com.example.backend.pojo.FoodInfoForRecipe;
import com.example.backend.pojo.RecipeOption;
import com.example.backend.pojo.User;

import java.util.List;

public class GenerateRecipeStrategy implements PromptStrategy {

    @Override
    public String buildPrompt(Object... params) {
        RecipeOption option = (RecipeOption) params[0];
        List<FoodInfoForRecipe> foodList = (List<FoodInfoForRecipe>) params[1];
        User userInfo = (User) params[2];
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

        prompt.append("]");
        
        // 添加用户饮食禁忌
        if (userInfo != null && userInfo.getDietaryTaboos() != null && !userInfo.getDietaryTaboos().isEmpty()) {
            prompt.append(", \"dietaryTaboos\": [");
            for (int i = 0; i < userInfo.getDietaryTaboos().size(); i++) {
                prompt.append("\"").append(userInfo.getDietaryTaboos().get(i)).append("\"");
                if (i < userInfo.getDietaryTaboos().size() - 1) {
                    prompt.append(", ");
                }
            }
            prompt.append("]");
        }
        
        // 添加用户口味偏好
        if (userInfo != null && userInfo.getDietaryPreferences() != null) {
            prompt.append(", \"dietaryPreferences\": \"").append(userInfo.getDietaryPreferences()).append("\"");
        }
        
        // 添加用户调味偏好
        if (userInfo != null && userInfo.getSeasoningPreferences() != null) {
            prompt.append(", \"seasoningPreferences\": \"").append(userInfo.getSeasoningPreferences()).append("\"");
        }

        prompt.append("}\n\n");

        // 添加固定格式要求
        prompt.append("请根据上述信息，生成3份不同的完整食谱，每份食谱需使用部分或全部食材，以不同组合方式呈现,但在最后的三份食谱中最好覆盖所有食材。");
        
        // 添加饮食禁忌提示
        if (userInfo != null && userInfo.getDietaryTaboos() != null && !userInfo.getDietaryTaboos().isEmpty()) {
            prompt.append("注意：用户有以下饮食禁忌，请严格避免使用相关食材：");
            for (String taboo : userInfo.getDietaryTaboos()) {
                prompt.append(taboo).append("、");
            }
            prompt.append("。");
        }
        
        prompt.append("每个食谱需包含以下固定格式：\n\n");
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
