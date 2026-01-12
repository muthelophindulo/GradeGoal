package com.GradeGoal.repository;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
}
