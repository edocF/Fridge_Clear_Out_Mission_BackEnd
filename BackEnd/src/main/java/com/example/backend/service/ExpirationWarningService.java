package com.example.backend.service;

import com.example.backend.exception.ExpirationWarningException;
import com.example.backend.pojo.Food;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface ExpirationWarningService {
    /**
     * 检查食物过期状态并更新对象
     * @param food 待检查的食物对象
     * @throws ExpirationWarningException 当检查失败时抛出
     */
    void checkAndWarnExpiration(Food food) throws ExpirationWarningException;

    /**
     * 计算剩余保质期天数
     * @param food 食物对象
     * @return 剩余天数（负数表示已过期）
     * @throws ExpirationWarningException 当计算失败时抛出
     */
    long calculateRemainingDays(Food food) throws ExpirationWarningException;

    /**
     * 批量检查食物过期状态
     */
    //每晚12点更新食物状态
    @Scheduled(cron = "0 0 0 * * ?")
    void batchUpdateFoodState();

    /**
     * 获取已过期的食物列表
     *
     * @return 已过期的食物列表
     */
    List<Food> getExpiringSoonAndExpiredFoods();

}