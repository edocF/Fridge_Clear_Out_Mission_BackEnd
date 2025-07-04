package com.example.backend.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TaboosAndPreferenceInfo {
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> dietaryTaboos;

    @JsonProperty("dietaryPreferences")
    private String dietaryPreferences;

    @JsonProperty("seasoningPreferences")
    private String seasoningPreferences;
}