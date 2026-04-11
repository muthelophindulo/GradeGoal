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
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        //logic for creating adminNo
        LocalDate date = LocalDate.now();
        String yy = String.valueOf(date.getYear()).substring(2,4);
        String mm = "0"+String.valueOf(date.getMonthValue());
        String adminNo = "ADM" + yy;

        Admin admin1 = adminRepository.save(admin);

        admin1.setAdminNo(adminNo + (admin1.getId() < 10 ? "0"+admin1.getId() : admin1.getId()) + mm);

        return AdminMapper.mapToDto(adminRepository.save(admin1));
    }

    public void deleteAdmin(String adminNo){
        adminRepository.delete(adminRepository.findByAdminNo(adminNo));
    }

    public AdminDto getAdminByAdminNo(String adminNo){
        if(adminRepository.findByAdminNo(adminNo) == null)
            return null;
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
