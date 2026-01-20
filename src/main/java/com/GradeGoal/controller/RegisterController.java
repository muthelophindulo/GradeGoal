package com.GradeGoal.controller;

import com.GradeGoal.model.Degree;
import com.GradeGoal.model.User;
import com.GradeGoal.service.DegreeService;
import com.GradeGoal.service.ResendService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RegisterController {
    @Autowired
    private DegreeService degreeService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private ResendService resendService;

    @GetMapping("/register")
    public String register(Model model){

        model.addAttribute("user",new User());

        List<Degree> degrees = degreeService.getDegrees();

        model.addAttribute("degrees", degrees);
        model.addAttribute("editable","false");
        return "register/register";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("user") User user,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            RedirectAttributes redirectAttributes
    ){

        try {

            if (confirmPassword != null && !user.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Passwords do not match");
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/register";
            }

            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("STUDENT");
            }

            // Save user
            user.setPassword(passwordEncoder.encode(user.getPassword()) );
            System.out.println(user.getDegree().getCode());
            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! You can now login.");
            resendService.sendWelcomeEmail(user.getEmail());
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Registration failed");
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }
    }
}
