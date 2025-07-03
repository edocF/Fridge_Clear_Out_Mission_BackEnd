package com.example.backend.mapper;

import com.example.backend.pojo.TaboosAndPreferenceInfo;
import com.example.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PersonalInfoMapper {
    @Select("select * from user where id = #{id};")
    User getAll(Integer id);
    // 使用 JSON 函数处理 dietary_taboos 字段（MySQL 示例）
    @Update({
            "UPDATE user SET dietary_taboos = CAST(#{taboosAndPreferenceInfo.dietaryTaboos} AS JSON),dietary_preferences = #{taboosAndPreferenceInfo.dietaryPreferences},seasoning_preferences = #{taboosAndPreferenceInfo.seasoningPreferences} WHERE id = #{currentUserId}"
    })
    void updatePersonalInfo(
            @Param("currentUserId") Integer currentUserId,
            @Param("taboosAndPreferenceInfo") TaboosAndPreferenceInfo taboosAndPreferenceInfo
    );
}
