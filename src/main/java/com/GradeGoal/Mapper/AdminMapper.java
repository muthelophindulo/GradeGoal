package com.GradeGoal.Mapper;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.model.Admin;
import com.GradeGoal.repository.AdminRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminDto toDto(Admin admin);
    Admin toEntity(AdminDto adminDto);
    List<AdminDto> toDtos(List<Admin> admins);
}
