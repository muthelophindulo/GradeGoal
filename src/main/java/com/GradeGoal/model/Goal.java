package com.GradeGoal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goal {
    @Column(nullable = false,unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /* basic infomation */

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String priority;

    /* TimeLine */
    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Transient
    private Long daysRemaining;

    @Transient
    private Boolean isOverdue;

    @Column
    private double progress;

    /* Target Details */
    @Column
    private double target;

    @Column
    private double actual;

    @Column(nullable = false)
    private String gpaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_no", referencedColumnName = "studentNo", nullable = false)
    private User user;
}
