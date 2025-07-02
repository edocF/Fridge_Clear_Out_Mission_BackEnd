package com.example.backend.mapper;

import com.example.backend.pojo.FoodConsumption;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FoodConsumptionMapper {
    @Insert("insert into food_consumptions (user_id, food_id, food_name, food_emoji, consumed_amount, kcal, carb, protein, fat, consumed_time) values (#{userId}, #{foodId}, #{foodName}, #{foodEmoji}, #{consumedAmount}, #{kcal}, #{carb}, #{protein}, #{fat}, #{consumedTime})")
    void insertConsumption(FoodConsumption consumption);

    @Select("select * from food_consumptions where user_id = #{userId} and consumed_time between #{start} and #{end} order by consumed_time desc")
    List<FoodConsumption> getConsumptionsByUserAndTime(@Param("userId") Integer userId,
                                                       @Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end);
}