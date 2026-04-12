package com.GradeGoal.controller;

import com.GradeGoal.model.Action;
import com.GradeGoal.model.Log;
import com.GradeGoal.model.ResetPasswordToken;
import com.GradeGoal.model.User;
import com.GradeGoal.service.LogService;
import com.GradeGoal.service.ResetTokenService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {
    @Autowired
    private ResetTokenService resetTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;

    @GetMapping("/{token}")
    public String resetPasswordForm(@PathVariable String token, Model model){
        //verify if the token exists in the database
        try{
            if(!resetTokenService.isUsed(token)){
                model.addAttribute("token",token);
                model.addAttribute("user",resetTokenService.getToken(resetTokenService.hashToken(token)).getUser());
                return "resetPassword/form";
            }else{
                return "resetPasswordError";
            }
        } catch (Exception e) {
            return "resetPasswordError";
        }
    }

    @PostMapping("/update-password")
    public String updatePassword(
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @ModelAttribute User user,
            Principal principal,
            Model model) {
        String username = user.getStudentNo();

        if(!password.equals(confirmPassword)){
            model.addAttribute("error","new password does not equal confirm password");
            model.addAttribute("user",userService.getUser(username));
            return "resetPassword/form";
        }

        try {
            User currentuser = userService.getUser(username);
            currentuser.setPassword(passwordEncoder.encode(password));
            List<ResetPasswordToken> userTokens = currentuser.getTokens();
            userTokens.forEach(resetPasswordToken -> resetPasswordToken.setUsed(true));
            currentuser.setTokens(userTokens);
            userService.saveUser(currentuser);

            Log log = new Log();
            log.setUser(user);
            log.setAction(Action.EDITED.toString());
            log.setDescription("updated password");
            logService.saveLog(log);

            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error","something went wrong" + e.getMessage());
            model.addAttribute("user",userService.getUser(username));
            return "resetPassword/form";
        }

    }


}
