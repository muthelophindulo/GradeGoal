package com.GradeGoal.service;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.Mapper.AdminMapper;
import com.GradeGoal.model.Admin;
import com.GradeGoal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public AdminDto saveAdmin(Admin admin){
        return AdminMapper.mapToDto(adminRepository.save(admin));
    }

    public void deleteAdmin(String adminNo){
        adminRepository.delete(adminRepository.findByAdminNo(adminNo));
    }

    public AdminDto getAdminByAdminNo(String adminNo){
        return AdminMapper.mapToDto(adminRepository.findByAdminNo(adminNo));
    }

    public AdminDto getAdminByAdminName(String name){
        return AdminMapper.mapToDto(adminRepository.findByName(name));
    }


}
