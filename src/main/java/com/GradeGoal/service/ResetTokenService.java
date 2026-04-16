package com.GradeGoal.service;

import com.GradeGoal.Dto.AdminDto;
import com.GradeGoal.model.Admin;
import com.GradeGoal.model.ResetPasswordToken;
import com.GradeGoal.model.User;
import com.GradeGoal.repository.ResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
public class ResetTokenService {
    @Autowired
    private ResetTokenRepository resetTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    public ResetPasswordToken save(ResetPasswordToken token){
        return resetTokenRepository.save(token);
    }

    public ResetPasswordToken getToken(String token) throws Exception {
        return resetTokenRepository.findByToken(token);
    }

    private ResetPasswordToken isValid(String token) throws Exception {
        ResetPasswordToken resetPasswordToken = resetTokenRepository.findByToken(hashToken(token));

        if(LocalDateTime.now().isAfter(resetPasswordToken.getExpiry_date())){
            resetPasswordToken.setUsed(true);
            save(resetPasswordToken);
        }

        return resetPasswordToken;
    }

    public boolean isUsed(String token) throws Exception {
        //hash the raw token given by the user
        String hashedInput = hashToken(token);

        //get the hashed token from the database
        ResetPasswordToken resetPasswordToken = resetTokenRepository.findByToken(hashedInput);

        if(resetPasswordToken != null){ //if resetPasswordToken is null it means it does not exist or the user entered an invalid token
            if(LocalDateTime.now().isAfter(resetPasswordToken.getExpiry_date())){
                resetPasswordToken.setUsed(true);
                save(resetPasswordToken);
            }
            return resetPasswordToken.isUsed();
        }
        return false;

    }

    public String hashToken(String rawToken) throws Exception{
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /*
    * this method will be used to check if an email exists in both admin and user's table
    * */
    public boolean emailExists(String email){
        AdminDto admin_email = adminService.getByEmail(email);
        User user_email = userService.getByEmail(email);

        if(admin_email != null || user_email != null){ // this means the email exists in one of the two tables
            return true; //to show that the email exists
        }

        return false; // if it doesnt
    }
}
