package com.example.backend.promptStrategy;

import com.example.backend.pojo.Food;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j

public class GetFoodNutritionByNameStrategy implements PromptStrategy{

    @Override
    public String buildPrompt(Object... params) {
        String foodName = (String) params[0];
        return "请作为专业营养师，严格按照以下格式返回食物「" + foodName + "」的标准化营养信息与冰箱保质期。需注意：\n" +
                "所有营养数值需精确到小数点后两位，以 100g 可食用部分为基准（若数据来源为 100g，需统一换算）；\n" +
                "保质期仅保留冰箱冷藏条件下（4℃左右）的天数，必须为大于1的整数，避免区间值；\n " +
                "表情符号需匹配食物属性（如水果用🍎、肉类用🥩、谷物用🌾等）；\n " +
                "数据来源需优先参考《中国食物成分表》或权威营养数据库，确保不同输入下的同类食物数据口径一致。\n " +
                "输出格式（严格遵循，x 为数字，表情符号需匹配食物类型）：\n" +
                "名称:" + foodName + "\n" +
                "热量(kcal/100g):xx.xx\n" +
                "碳水(g/100g):xx.xx\n" +
                "蛋白质(g/100g):xx.xx\n" +
                "脂肪(g/100g):xx.xx\n" +
                "表情符号:🍎\n" +
                "保质期(天):xx";
    }

    @Override
    public Food parseContent(String content, Object... params) {
        Food food = new Food();
        food.setCreateTime(LocalDateTime.now());
        log.info("Content: " + content);
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith("名称:")) {
                // 名称不再包含emoji，无需提取
            } else if (line.startsWith("表情符号:")) {
                food.setEmoji(line.substring(5));
            } else if (line.startsWith("热量(kcal/500g):")) {
                food.setKcal(parseDouble(line, 14));
            } else if (line.startsWith("碳水(g/500g):")) {
                food.setCarb(parseDouble(line, 11));
            } else if (line.startsWith("蛋白质(g/500g):")) {
                food.setProtein(parseDouble(line, 12));
            } else if (line.startsWith("脂肪(g/500g):")) {
                food.setFat(parseDouble(line, 11));
            } else if (line.startsWith("保质期(天):")) {
                food.setExpiryDuration(parseInt(line, 7));
            }
        }
        // 设置默认值
        if (food.getEmoji() == null || food.getEmoji().isEmpty()) {
            food.setEmoji("❓");
        }
        if (food.getNumber() == null) {
            food.setNumber(1);
        }
        log.info("food: " + food);
        return food;
    }

    @Override
    public Object buildMessage(Object... params) {
        return null;
    }

    private Double parseDouble(String line, int offset) {
        try {
            return Double.parseDouble(line.substring(offset).trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * 解析Integer值
     */
    private Integer parseInt(String line, int offset) {
        try {
            return Integer.parseInt(line.substring(offset).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
