package com.example.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String image;
    private LocalDateTime createTime;
    private Integer familyId;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> dietaryTaboos;
    private String dietaryPreferences;
    private String  seasoningPreferences;
}
