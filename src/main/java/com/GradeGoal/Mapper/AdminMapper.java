package com.GradeGoal.Mapper;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.model.Admin;
import com.GradeGoal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AdminMapper {
    @Autowired
    private static AdminRepository adminRepository;

    public static AdminDto mapToDto(Admin admin){
        return new AdminDto(
                admin.getAdminNo(),
                admin.getName(),
                admin.getEmail(),
                admin.getCellNo()
        );
    }

    public static Admin maptoAdmin(AdminDto adminDto){
        return adminRepository.findByAdminNo(adminDto.getAdminNo());
    }

    public static List<AdminDto> maptoDtos(List<Admin> admins){
        List<AdminDto> adminDtoList = new ArrayList<>();

        admins.forEach(admin -> adminDtoList.add(new AdminDto(
                admin.getAdminNo(),
                admin.getName(),
                admin.getEmail(),
                admin.getCellNo()
        )));

        return adminDtoList;
    }

    public static List<Admin> mapToAdmins(List<AdminDto> adminDtoList){
        List<Admin> adminList = new ArrayList<>();

        adminDtoList.forEach(adminDto -> adminList.add(adminRepository.findByName(adminDto.getName())));

        return adminList;
    }
}
