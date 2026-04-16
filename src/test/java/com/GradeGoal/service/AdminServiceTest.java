package com.GradeGoal.service;

import com.GradeGoal.model.Admin;
import com.GradeGoal.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;
    private String name = "muthelo phindulo";
    private String email = "muthelophindulo223@gmail.com";
    private String adminNo = "ADM260104";

    @BeforeEach
    public void setup(){
        admin = new Admin();

        admin.setAdminNo(adminNo);
        admin.setName(name);
        admin.setEmail(email);

        //when(adminRepository.save(admin)).thenReturn(admin);
    }

    @Test
    void saveAdmin() {
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Admin admin1 = adminRepository.save(admin);

        assertNotNull(admin1);
        assertEquals(adminNo,admin1.getAdminNo());
    }

    @Test
    void deleteAdmin() {

    }

    @Test
    void getAdminByAdminNo() {
        when(adminRepository.findByAdminNo(adminNo)).thenReturn(admin);

        assertEquals(admin,adminRepository.findByAdminNo(adminNo));
    }

    @Test
    void getAdminByAdminName() {
        when(adminRepository.findByName(name)).thenReturn(admin);

        assertEquals(admin,adminRepository.findByName(name));
    }

    @Test
    void getAdmin() {
    }

    @Test
    void getAdmins() {
    }

    @Test
    void countUsers() {
    }

    @Test
    void getByEmail() {

    }
    @Test
    void createUser(){
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

    }
}