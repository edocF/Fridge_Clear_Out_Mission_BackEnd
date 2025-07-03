package com.example.backend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TaboosAndPreferenceInfo {
    @JsonProperty("dietaryTaboos")
    private List<String> dietaryTaboos;

    @JsonProperty("dietaryPreferences")
    private String dietaryPreferences;

    @JsonProperty("seasoningPreferences")
    private String seasoningPreferences;
}