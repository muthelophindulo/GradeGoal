package com.GradeGoal.repository;

import com.GradeGoal.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetPasswordToken , Long> {
    ResetPasswordToken findByToken(String token);
}
