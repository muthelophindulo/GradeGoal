package com.GradeGoal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "assessments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column

    private LocalDate date;

    @Column
    private double weigh;

    @Column
    private double TargetMark;

    @Column
    private double ActualMark;

    @ManyToOne
    @JoinColumn(name = "course_code", referencedColumnName = "code", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_no", referencedColumnName = "studentNo", nullable = false)
    @ToString.Exclude
    private User user;
}
