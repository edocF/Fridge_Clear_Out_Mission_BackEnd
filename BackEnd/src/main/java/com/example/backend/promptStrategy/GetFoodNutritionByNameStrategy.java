package com.example.backend.promptStrategy;

import com.example.backend.pojo.Food;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class GetFoodNutritionByNameStrategy implements PromptStrategy{

    @Override
    public String buildPrompt(Object... params) {
        String foodName = (String) params[0];
        return "你是一个营养师，请返回食物「" + foodName + "」的详细营养信息和保质期(请对信息进行充分考虑,保质期一定只给出一个数字以食物在冰箱内的保质期为准)，格式要求(x的部分是表示数字x的个数并不决定实际的数字 表情符号部分🍎只是示例请输出响应食物emoji,保鲜日期不能为0)：\n" +
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
        String foodName = (String) params[0];
        Food food = new Food();
        food.setName(foodName);
        food.setCreateTime(LocalDateTime.now());
        log.info("Content: " + content);
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.startsWith("名称:")) {
                // 名称不再包含emoji，无需提取
            } else if (line.startsWith("表情符号:")) {
                food.setEmoji(line.substring(5));
            } else if (line.startsWith("热量(kcal):")) {
                food.setKcal(parseDouble(line, 9));
            } else if (line.startsWith("碳水(g):")) {
                food.setCarb(parseDouble(line, 6));
            } else if (line.startsWith("蛋白质(g):")) {
                food.setProtein(parseDouble(line, 7));
            } else if (line.startsWith("脂肪(g):")) {
                food.setFat(parseDouble(line, 6));
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
