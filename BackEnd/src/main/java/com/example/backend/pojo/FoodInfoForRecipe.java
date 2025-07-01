package com.example.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodInfoForRecipe {
    Integer id;
    String name;
    Boolean expired;
    Boolean expSoon;
    Integer fridgeId;
}
