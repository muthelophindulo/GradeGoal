package com.GradeGoal.repository;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByStudentNo(String studentNo);
    User findByEmail(String email);
}
