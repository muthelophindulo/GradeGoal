package com.GradeGoal.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private String adminNo;

    private String name;

    private String email;

    private String cellNo;

    private String role;
}
