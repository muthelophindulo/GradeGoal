package com.GradeGoal.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    private Long id;

    private String action;

    private LocalDateTime createdAt;
}
