package com.example.backend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TaboosAndPreferenceInfo {
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("dietaryTaboos")
    private List<String> dietaryTaboos;

    @JsonProperty("dietaryPreferences")
    private String dietaryPreferences;

    @JsonProperty("seasoningPreferences")
    private String seasoningPreferences;
    
    @JsonProperty("image")
    private String image;
}