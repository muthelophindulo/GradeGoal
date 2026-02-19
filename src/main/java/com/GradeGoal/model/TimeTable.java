package com.GradeGoal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
        "dayOfWeek", "timeSlot", "academicYear", "semester", "user_id"
}))
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer academicYear;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private String colorCode;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private String timeSlot;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "studentNo", nullable = false)
    private User user;
}
