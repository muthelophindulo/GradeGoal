package com.GradeGoal.service;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.Dto.UserDto;
import com.GradeGoal.Mapper.AdminMapper;
import com.GradeGoal.Mapper.UserMapper;
import com.GradeGoal.model.Admin;
import com.GradeGoal.model.User;
import com.GradeGoal.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final UserService userService;
    private final UserMapper userMapper;
    private final ResendService resendService;
    private final AdminMapper adminMapper;

    public AdminDto saveAdmin(Admin admin){
        String rawPassword = generatePassword();
        admin.setPassword(passwordEncoder.encode(rawPassword));

        Admin admin1 = adminRepository.save(admin);
        String mm = "0"+String.valueOf(LocalDate.now().getMonthValue());

        String adminNo = generateAdminNo();
        admin1.setAdminNo(adminNo + (admin1.getId() < 10 ? "0"+admin1.getId() : admin1.getId()) + mm);

        Admin savedAdmin = adminRepository.save(admin1);
        resendService.sendLoginDetailsEmail(savedAdmin.getEmail(),rawPassword,savedAdmin.getAdminNo());


        return adminMapper.toDto(savedAdmin);
    }

    public void deleteAdmin(String adminNo){
        adminRepository.delete(adminRepository.findByAdminNo(adminNo));
    }

    public AdminDto getAdminByAdminNo(String adminNo){
        if(adminRepository.findByAdminNo(adminNo) == null)
            return null;
        return adminMapper.toDto(adminRepository.findByAdminNo(adminNo));
    }

    public AdminDto getAdminByAdminName(String name){
        return adminMapper.toDto(adminRepository.findByName(name));
    }

    public Admin getAdmin(String adminNo){
        return adminRepository.findByAdminNo(adminNo);
    }

    public List<AdminDto> getAdmins(){
        return adminMapper.toDtos(adminRepository.findAll());
    }

    public AdminDto getByEmail(String email){
        return adminMapper.toDto(adminRepository.findByEmail(email));
    }

    //this creates passwords for those that have been created by the admin
    private String generatePassword(){
        String alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        int length = 8;
        Random rand = new Random();
        StringBuilder password = new StringBuilder();

        for(int i =0; i < length ;i++){
            password.append(alphaNum.charAt(rand.nextInt(alphaNum.length())));
        }

        return password.toString();
    }

    private String generateAdminNo(){
        LocalDate date = LocalDate.now();
        String yy = String.valueOf(date.getYear()).substring(2,4);

        return "ADM" + yy;
    }

    public UserDto createUser(User user){
        String rawPassword = generatePassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("STUDENT");

        User savedUser = userService.saveUser(user);


        resendService.sendLoginDetailsEmail(savedUser.getEmail(),rawPassword,savedUser.getStudentNo());


        return userMapper.toDto(user);
    }
}
