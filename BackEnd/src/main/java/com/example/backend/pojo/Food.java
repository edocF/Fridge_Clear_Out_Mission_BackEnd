package com.example.backend.pojo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    private Integer id;
    private String name;
    private String emoji;
    private Double kcal;
    private Double carb;
    private Double protein;
    private Double fat;
    private Integer number;
    private Boolean expired;
    private Boolean expSoon;
    private Integer expiryDuration;
    private LocalDateTime expiryDate;
    private Integer fridgeId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}