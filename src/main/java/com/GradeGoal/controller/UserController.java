package com.GradeGoal.controller;

import com.GradeGoal.service.AssessmentService;
import com.GradeGoal.service.CourseService;
import com.GradeGoal.service.UserService;
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
        model.addAttribute("yearOfStudy",1); //TODO calculate the year of study
        model.addAttribute("gpa",userService.GPA(loggedinuser));
        model.addAttribute("graduationYear","may 2028"); //TODO calculation of graduation year

        model.addAttribute("courseCompletionRate",courseService.completed(loggedinuser));
        model.addAttribute("assessmentCompletionRate",assessmentService.completed(loggedinuser));
        model.addAttribute("targetAchievementRate",courseService.targetArchieved(loggedinuser));

        return "user/profile2";
    }

    @PostMapping("/verify-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyPassword(
            @RequestParam String currentPassword,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();
        String username = principal.getName();

        // Call service to verify password
        boolean isValid = passwordEncoder.matches(currentPassword,userService.getUser(username).getPassword());

        if (isValid) {
            response.put("success", true);
            response.put("message", "Password verified successfully");
        } else {
            response.put("success", false);
            response.put("message", "Incorrect password");
        }

        return ResponseEntity.ok(response);
    }

    // Update password
    @PostMapping("/update-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();
        String username = principal.getName();

        // Validate new passwords match
        if (!newPassword.equals(confirmPassword)) {
            response.put("success", false);
            response.put("message", "Passwords do not match");
            return ResponseEntity.ok(response);
        }

        // Validate password strength
        if (newPassword.length() < 8) {
            response.put("success", false);
            response.put("message", "Password must be at least 8 characters long");
            return ResponseEntity.ok(response);
        }

        // Update password
        userService.getUser(username).setPassword(passwordEncoder.encode(newPassword));

        boolean updated = passwordEncoder.matches(newPassword, userService.getUser(username).getPassword());

        if (updated) {
            response.put("success", true);
            response.put("message", "Password updated successfully");
        } else {
            response.put("success", false);
            response.put("message", "Failed to update password. Current password may be incorrect.");
        }

        return ResponseEntity.ok(response);
    }

    // Delete account
    @PostMapping("/delete-account")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteAccount(
            @RequestParam String password,
            Principal principal,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        String username = principal.getName();

        // Verify password first
        boolean isValid = passwordEncoder.matches(password,userService.getUser(username).getPassword());

        if (!isValid) {
            response.put("success", false);
            response.put("message", "Incorrect password");
            return ResponseEntity.ok(response);
        }

        if (isValid) {
            response.put("success", true);
            response.put("message", "Account deleted successfully");
            // Invalidate session
            session.invalidate();
        } else {
            response.put("success", false);
            response.put("message", "Failed to delete account");
        }

        return ResponseEntity.ok(response);
    }

    /*
    @GetMapping("change-password")
    public String changePass(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Principal principal,
            RedirectAttributes redirectAttributes
    ){

        if(passwordEncoder.matches(currentPassword,userService.getUser(principal.getName()).getPassword())){
            if(newPassword.equals(confirmPassword)){
                userService.getUser(principal.getName()).setPassword(passwordEncoder.encode(newPassword));
            }
        }

        redirectAttributes.addAttribute("message","password changed successfully");
        return "redirect/user/profile";
    }

    @GetMapping("settings")
    public String settings(Model model,Principal principal){
        return "user/settings";
    }

    @GetMapping("delete-account")
    public String delete(@RequestParam String password,
                         Principal principal,
                         HttpSession session){
        String loogedinuser = principal.getName();

        if(passwordEncoder.matches(password,userService.getUser(loogedinuser).getPassword())){
            userService.deleteUser(userService.getUser(loogedinuser));
            session.invalidate();
        }
        return "redirect:/login";
    }*/

}
