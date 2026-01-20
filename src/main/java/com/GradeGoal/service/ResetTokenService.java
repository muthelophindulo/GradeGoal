package com.GradeGoal.service;

import com.GradeGoal.model.ResetPasswordToken;
import com.GradeGoal.repository.ResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ResetTokenService {
    @Autowired
    private ResetTokenRepository resetTokenRepository;

    public ResetPasswordToken save(ResetPasswordToken token){
        return resetTokenRepository.save(token);
    }

    public ResetPasswordToken getToken(String token){
        return isValid(token);
    }

    public void Delete(ResetPasswordToken token){
        resetTokenRepository.delete(token);
    }

    private ResetPasswordToken isValid(String token){
        ResetPasswordToken resetPasswordToken = resetTokenRepository.findByToken(token);

        if(resetPasswordToken.getExpiry_date().isAfter(LocalDateTime.now())){
            resetPasswordToken.setUsed(true);
        }

        return resetPasswordToken;
    }
}
