package com.example.backend.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsumptionReportRequest {
    private LocalDateTime start;
    private LocalDateTime end;   
} 