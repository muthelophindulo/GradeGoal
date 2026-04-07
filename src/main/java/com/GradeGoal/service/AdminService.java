package com.GradeGoal.service;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.Mapper.AdminMapper;
import com.GradeGoal.model.Admin;
import com.GradeGoal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AssessmentService assessmentService;

    public AdminDto saveAdmin(Admin admin){
        LocalDate date = LocalDate.now();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        System.out.println();
        admin.setAdminNo("ADM" + Integer.toString(date.getYear()).substring(2,4) +"0"+ date.getMonthValue());
        adminRepository.save(admin);

        Admin savedAdmin = adminRepository.findByName(admin.getName());
        savedAdmin.setAdminNo(savedAdmin.getAdminNo() + (savedAdmin.getId() < 10 ? "0" + savedAdmin.getId() : savedAdmin.getId()));
        Admin saved2 = adminRepository.save(savedAdmin);
        return AdminMapper.mapToDto(saved2);
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

    public List<AdminDto> getAdmins(){
        return AdminMapper.maptoDtos(adminRepository.findAll());
    }

    public int CountUsers(){
        return userService.getUsers().size();
    }
}
