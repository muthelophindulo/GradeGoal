package com.GradeGoal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String adminNo;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String cellNo;

    @Column
    private String password;

    @Column
    private String role;

    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, optional = false,fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(referencedColumnName = "id", name = "image_id")
    @ToString.Exclude
    private Image image;

    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    @ToString.Exclude
    private List<Log> log;
}
