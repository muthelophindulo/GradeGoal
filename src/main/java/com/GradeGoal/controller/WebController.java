package com.GradeGoal.controller;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.Course;
import com.GradeGoal.model.ResetPasswordToken;
import com.GradeGoal.model.User;
import com.GradeGoal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
public class WebController {

    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private GoalService goalService;
    @Autowired
    private ResetTokenService resetTokenService;
    @Autowired
    private ResendService resendService;

    @GetMapping("/dashboard")
    public String dashboard(Model model,Principal principal){


        String loggedInUser = principal.getName();


        if(userService.getUser(loggedInUser).getCourses().isEmpty() ){
            return "redirect:/course/new";
        }else {

            model.addAttribute("user", userService.getUser(loggedInUser));
            model.addAttribute("courses",courseService.topCourses(loggedInUser));
            model.addAttribute("assessments",assessmentService.topAssessments(loggedInUser));
            model.addAttribute("goals",goalService.getGoals(loggedInUser));


            //check if the user has assessments and courses
            List<Course> courses = courseService.getCourses(loggedInUser);
            model.addAttribute("courseCount", courses.size());

            List<Assessment> assessments = assessmentService.getAssessments(loggedInUser);
            model.addAttribute("assessmentCount", assessments.size());

            model.addAttribute("pendingCount", assessmentService.pending(loggedInUser));

            //model.addAttribute("averageGrade",userService.AverageGrade(loggedInUser));
            model.addAttribute("averageGrade", courseService.AverageGrade(loggedInUser));

            model.addAttribute("gpa", courseService.GPA(loggedInUser));

            return "dashboard";
        }


    }

    @GetMapping("/add-course")
    public String AddCourse(Model model,Principal principal){
        String loggedInUser = principal.getName();
        model.addAttribute("user",userService.getUser(loggedInUser));

        return "course/form";
    }

    @GetMapping("/add-assessment")
    public String AddAssessment(Model model,Principal principal
    ){
        model.addAttribute("assessment",new Assessment());

        return "assessment/form";
    }

    @GetMapping("/forgot-password")
    public String resetForm(){
        return "resetPassword/resetPassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            RedirectAttributes redirectAttributes
    ){
        try{
            boolean emailExists = userService.getByEmail(email) != null;

            if(!emailExists){
                redirectAttributes.addFlashAttribute("passwordResetSuccess", "If an account exists with this email, you will receive a password reset link shortly.");
                return "redirect:/forgot-password";
            }else{
                ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
                resetPasswordToken.setToken(UUID.randomUUID().toString());
                resetPasswordToken.setUser(userService.getByEmail(email));
                resetPasswordToken.setExpiry_date(LocalDateTime.now().plusHours(24));

                resetTokenService.save(resetPasswordToken);

                redirectAttributes.addFlashAttribute("passwordResetSuccess", "If an account exists with this email, you will receive a password reset link shortly.");

                String url = "www.gradegoal.co.za/reset/" + resetPasswordToken.getToken();
                resendService.sendResetEmail(url,email);

                return "redirect:/forgot-password";

            }
        }catch (Exception e){

            redirectAttributes.addFlashAttribute("passwordResetError",
                    "An error occurred. Please try again later." + e);
            redirectAttributes.addFlashAttribute("email", email);
        }

        return "redirect:/forgot-password";
    }


}
