package com.GradeGoal.controller;

import com.GradeGoal.model.User;
import com.GradeGoal.service.ResetTokenService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {
    @Autowired
    private ResetTokenService resetTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping("/{token}")
    public String resetPasswordForm(@PathVariable String token, Model model){
        //verify if the token exists in the database
        try{
            boolean isValid = resetTokenService.getToken(token).isUsed();

            if(isValid){
                System.out.println("form called");
                model.addAttribute("token",token);
                System.out.println(resetTokenService.getToken(token).getUser());
                model.addAttribute("user",resetTokenService.getToken(token).getUser());
                return "resetPassword/form";
            }else if(resetTokenService.getToken(token) == null){
                return "resetPasswordError";
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
            return "user/changePass";
        }

        try {
            User currentuser = userService.getUser(username);
            currentuser.setPassword(passwordEncoder.encode(password));
            userService.saveUser(currentuser);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error","something went wrong" + e.getMessage());
            model.addAttribute("user",userService.getUser(username));
            return "user/changePass";
        }

    }


}
