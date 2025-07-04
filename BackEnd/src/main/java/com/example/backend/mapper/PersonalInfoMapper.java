package com.example.backend.mapper;

import com.example.backend.pojo.TaboosAndPreferenceInfo;
import com.example.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;

@Mapper
public interface PersonalInfoMapper {
    @Select("select * from user where id = #{id}")
    @Results({
        @Result(column = "dietary_taboos", property = "dietaryTaboos", typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    })
    User getAll(Integer id);
    // 使用 JSON 函数处理 dietary_taboos 字段（MySQL 示例）
    @Update({
        "UPDATE user SET dietary_taboos = #{taboosAndPreferenceInfo.dietaryTaboos, typeHandler=com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler}, dietary_preferences = #{taboosAndPreferenceInfo.dietaryPreferences}, seasoning_preferences = #{taboosAndPreferenceInfo.seasoningPreferences} WHERE id = #{currentUserId}"
    })
    void updatePersonalInfo(
        @Param("currentUserId") Integer currentUserId,
        @Param("taboosAndPreferenceInfo") TaboosAndPreferenceInfo taboosAndPreferenceInfo
    );
}
