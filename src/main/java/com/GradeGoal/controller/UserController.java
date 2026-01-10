package com.GradeGoal.controller;

import com.GradeGoal.service.AssessmentService;
import com.GradeGoal.service.CourseService;
import com.GradeGoal.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("profile")
    public String profile(Model model, Principal principal){
        String loggedinuser = principal.getName();
        model.addAttribute("user",userService.getUser(loggedinuser));
        model.addAttribute("totalCourses",userService.getUser(loggedinuser).getCourses().size());
        model.addAttribute("completedCourses",courseService.completed(loggedinuser));
        model.addAttribute("averageGrade",userService.AverageGrade(loggedinuser));
        model.addAttribute("totalAssessments",userService.getUser(loggedinuser).getAssessments().size());
        model.addAttribute("yearOfStudy",userService.yearOfStudy(loggedinuser));
        model.addAttribute("gpa",userService.GPA(loggedinuser));
        model.addAttribute("graduationYear","may " + userService.gradYear(loggedinuser));

        model.addAttribute("courseCompletionRate",courseService.completed(loggedinuser));
        model.addAttribute("assessmentCompletionRate",assessmentService.completed(loggedinuser));
        model.addAttribute("targetAchievementRate",courseService.targetArchieved(loggedinuser));

        return "user/profile2";
    }

    @PostMapping("/verify-password")
    @ResponseBody
    public String verifyPassword(
            @RequestParam String currentPassword,
            Principal principal,
            HttpServletRequest request) {

        String username = principal.getName();
        if(passwordEncoder.matches(currentPassword,userService.getUser(username).getPassword())){
            if(request.getHeader("Referer").contains("change")){
                return "redirect:/user/update-password";
            } else if (request.getHeader("Referer").contains("delete")) {
                return "redirect:/user/delete-account";
            }
        }else{
            return "redirect:/user/verify-password";
        }
        return request.getHeader("Referer");
    }

    // Update password
    @GetMapping("/change")
    public String showUpdate(Model model,Principal principal){
        String username = principal.getName();
        model.addAttribute("user",userService.getUser(username));

        return "user/changePass";
    }
    @PostMapping("/update-password")
    public String updatePassword(
            @RequestParam String newPassword,
            @RequestParam String currentPassword,
            @RequestParam String confirmPassword,
            Principal principal,
            Model model) {
        String username = principal.getName();

        if(!newPassword.equals(confirmPassword)){
            model.addAttribute("error","new password does not equal confirm password");
            model.addAttribute("user",userService.getUser(username));
            return "user/changePass";
        }
        //check if the current password is the same
        if(!passwordEncoder.matches(currentPassword,userService.getUser(username).getPassword())){
            model.addAttribute("error","entered password do not match current password");
            model.addAttribute("user",userService.getUser(username));
            return "user/changePass";
        }

        try {
            userService.getUser(username).setPassword(passwordEncoder.encode(newPassword));
            System.out.println("pass changed");
            model.addAttribute("success","password changed");
            return "redirect:/user/profile";
        } catch (Exception e) {
            model.addAttribute("error","something went wrong" + e.getMessage());
            model.addAttribute("user",userService.getUser(username));
            return "user/changePass";
        }

    }

    // Delete account
    //show delete page
    @GetMapping("/delete-account")
    public String showdelete(Model model,Principal principal){
        String username = principal.getName();
        model.addAttribute("user",userService.getUser(username));
        model.addAttribute("courseCount",courseService.getCourses(username).size());
        model.addAttribute("assessmentCount",assessmentService.getAssessments(username).size());

        return "user/deleteAcct";
    }
    @PostMapping("/delete")
    public String deleteAccount(
            @RequestParam String password,
            Principal principal,
            HttpSession session) {

        String username = principal.getName();

        // Verify password first
        boolean isValid = passwordEncoder.matches(password,userService.getUser(username).getPassword());

        if(isValid){
            userService.deleteUser(userService.getUser(username));
            return "redirect:/logout";
        }else{
            return "redirect:/user/profile";
        }
    }

}