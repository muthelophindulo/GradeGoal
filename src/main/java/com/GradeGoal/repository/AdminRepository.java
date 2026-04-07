package com.GradeGoal.repository;

import com.GradeGoal.model.Admin;
import com.GradeGoal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByAdminNumber(String adminNumber);
    Admin findByAdminEmail(String email);
}
