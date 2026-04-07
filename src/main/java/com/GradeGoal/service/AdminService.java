package com.GradeGoal.service;

import com.GradeGoal.model.Admin;
import com.GradeGoal.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin getById(Long id){
        return adminRepository.getReferenceById(id);
    }

    public Admin getByEmail(String email){
        return adminRepository.findByAdminEmail(email);
    }

    @Transactional
    public Admin saveUser(Admin admin){
        return adminRepository.save(admin);
    }

    public void deleteUser(Admin admin){
        adminRepository.deleteById(admin.getId());
    }

    public Admin getAdmin(String adminNo){
        return adminRepository.findByAdminNumber(adminNo);
    }

}
