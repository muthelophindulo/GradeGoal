package com.GradeGoal.repository;

import com.GradeGoal.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    Image findByName(String name);
}
