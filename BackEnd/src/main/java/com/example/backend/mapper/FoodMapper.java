package com.example.backend.mapper;

import com.example.backend.pojo.Food;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodMapper {
    @Insert("insert into foods (id, name, emoji, kcal, carb, protein, fat, number, expired, expSoon, expiryDuration, expiryDate, fridge_id, create_time, update_time) values (#{id},#{name},#{emoji},#{kcal},#{carb},#{protein},#{fat},#{number},#{expired},#{expSoon},#{expiryDuration},#{expiryDate},#{fridgeId},#{createTime},#{updateTime});")
    void add(Food food);
}
