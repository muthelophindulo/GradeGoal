package com.GradeGoal.repository;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment,Long> {

}
