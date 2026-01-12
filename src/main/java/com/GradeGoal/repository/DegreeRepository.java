package com.GradeGoal.repository;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends JpaRepository<Degree,Long> {
}
