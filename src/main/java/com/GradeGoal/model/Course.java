package com.GradeGoal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Column
    private int semester;

    @Column
    private int year;

    @Column(nullable = false)
    private double passMark;

    @Column(nullable = true)
    private double TargetMark;

    @Column(nullable = true)
    private double ActualMark;

    @ColumnDefault("40")
    @Column(nullable = false)
    private double dp;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Assessment> assessments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "student_no", referencedColumnName = "studentNo", nullable = false)
    private User user;
}
