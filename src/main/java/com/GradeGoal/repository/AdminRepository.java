package com.GradeGoal.repository;

import com.GradeGoal.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByAdminNo(String adminNo);
    Admin findByName(String name);
    Admin findByEmail(String email);
}
