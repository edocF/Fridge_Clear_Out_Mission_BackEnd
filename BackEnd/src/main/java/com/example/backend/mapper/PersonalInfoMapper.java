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
            "UPDATE user SET " +
            "username = CASE WHEN #{taboosAndPreferenceInfo.username} IS NOT NULL AND #{taboosAndPreferenceInfo.username} != '' THEN #{taboosAndPreferenceInfo.username} ELSE username END, " +
            "password = CASE WHEN #{taboosAndPreferenceInfo.password} IS NOT NULL AND #{taboosAndPreferenceInfo.password} != '' THEN #{taboosAndPreferenceInfo.password} ELSE password END, " +
            "dietary_taboos = CASE WHEN #{taboosAndPreferenceInfo.dietaryTaboos} IS NOT NULL THEN #{taboosAndPreferenceInfo.dietaryTaboos, typeHandler=com.example.backend.mapper.JsonTypeHandler} ELSE dietary_taboos END, " +
            "dietary_preferences = CASE WHEN #{taboosAndPreferenceInfo.dietaryPreferences} IS NOT NULL THEN #{taboosAndPreferenceInfo.dietaryPreferences} ELSE dietary_preferences END, " +
            "seasoning_preferences = CASE WHEN #{taboosAndPreferenceInfo.seasoningPreferences} IS NOT NULL THEN #{taboosAndPreferenceInfo.seasoningPreferences} ELSE seasoning_preferences END, " +
            "image = CASE WHEN #{taboosAndPreferenceInfo.image} IS NOT NULL AND #{taboosAndPreferenceInfo.image} != '' THEN #{taboosAndPreferenceInfo.image} ELSE image END " +
            "WHERE id = #{currentUserId}"
    })
    void updatePersonalInfo(
            @Param("currentUserId") Integer currentUserId,
            @Param("taboosAndPreferenceInfo") TaboosAndPreferenceInfo taboosAndPreferenceInfo
    );
    
    @Update("UPDATE user SET image = #{imageUrl} WHERE id = #{currentUserId}")//头像更新方法
    void updateAvatar(@Param("currentUserId") Integer currentUserId, @Param("imageUrl") String imageUrl);
    
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND id != #{currentUserId}")
    int checkUsernameExists(@Param("username") String username, @Param("currentUserId") Integer currentUserId);
}
