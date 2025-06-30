package com.example.backend.mapper;

import com.example.backend.pojo.Food;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FoodMapper {
    @Insert("insert into foods (id, name, emoji, kcal, carb, protein, fat, number, expired, expSoon, expiryDuration, expiryDate, fridge_id, create_time, update_time) values (#{id},#{name},#{emoji},#{kcal},#{carb},#{protein},#{fat},#{number},#{expired},#{expSoon},#{expiryDuration},#{expiryDate},#{fridgeId},#{createTime},#{updateTime});")
    void add(Food food);

    @Select("select * from foods where fridge_id = #{fridgeId};")
    List<Food> getFoodList(Integer fridgeId);

    void deleteFood(Integer fridgeId, List<Integer> ids);
}
