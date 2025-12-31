package com.GradeGoal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "degrees")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Degree {
   // @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

    @Id
    @Column(nullable = false,unique = true)
    private String name;

    @Column
    private String faculty;

    @Column(nullable = false,unique = true)
    private String code;

    @Column
    private double passMark;

    @Column
    private int TotalCredits;

    @Column
    private int duration;

    @OneToMany(mappedBy = "degree")
    @ToString.Exclude
    private List<User> users;
}
