package com.example.backend.mapper;

import com.example.backend.pojo.Food;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FoodMapper {
    @Insert("insert into foods (id, name, emoji, kcal, carb, protein, fat, number, expired, expSoon, expiryDuration, expiryDate, fridge_id, create_time, update_time) values (#{id},#{name},#{emoji},#{kcal},#{carb},#{protein},#{fat},#{number},#{expired},#{expSoon},#{expiryDuration},#{expiryDate},#{fridgeId},#{createTime},#{updateTime});")
    void add(Food food);

    @Select("select * from foods where fridge_id = #{fridgeId};")
    List<Food> getFoodList(Integer fridgeId);

    void deleteFood(Integer fridgeId, List<Integer> ids);
    @Update("update foods set number = #{number} where fridge_id = #{fridgeId} AND id = #{id};")
    void updateFood(Integer id, Integer number, Integer fridgeId);

    @Select("select * from foods where fridge_id = #{fridgeId} AND expired = 0;")
    List<Food> getValidFoodList(Integer fridgeId);

    @Select("select * from foods where id = #{id};")
    Food getFoodById(Integer id);
}
