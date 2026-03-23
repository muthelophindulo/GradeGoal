package com.GradeGoal.service;

import com.GradeGoal.model.ResetPasswordToken;
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

    public ResetPasswordToken save(ResetPasswordToken token){
        return resetTokenRepository.save(token);
    }

    public ResetPasswordToken getToken(String token) throws Exception {
        return isValid(hashToken(token));
    }

    private ResetPasswordToken isValid(String token) throws Exception {
        ResetPasswordToken resetPasswordToken = resetTokenRepository.findByToken(hashToken(token));

        if(resetPasswordToken.getExpiry_date().isBefore(LocalDateTime.now())){
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

        if(resetPasswordToken!= null){ //if resetPasswordToken is null it means it does not exist or the user entered an invalid token
            if(resetPasswordToken.getExpiry_date().isBefore(LocalDateTime.now())){
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
}
