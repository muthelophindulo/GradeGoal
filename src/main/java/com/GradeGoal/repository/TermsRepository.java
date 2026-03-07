package com.GradeGoal.repository;

import com.GradeGoal.model.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Long> {
    Terms findByStudentNo(String studentNo);
}
