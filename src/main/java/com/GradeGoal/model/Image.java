package com.GradeGoal.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    @Column(nullable = false,unique = true)
    private String name;

    @Column
    private String type;

    @OneToOne
    @JoinColumn(referencedColumnName = "studentNo",name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToOne
    @ToString.Exclude
    private Admin admin;
}
