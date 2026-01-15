package com.GradeGoal.controller;

import com.GradeGoal.model.User;
import com.GradeGoal.service.AssessmentService;
import com.GradeGoal.service.CourseService;
import com.GradeGoal.service.DegreeService;
import com.GradeGoal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    @Autowired
    private DegreeService degreeService;

    @GetMapping("profile")
    public String profile(Model model, Principal principal){
        String loggedinuser = principal.getName();
        model.addAttribute("user",userService.getUser(loggedinuser));
        model.addAttribute("totalCourses",courseService.getYearlyAssessments(loggedinuser).size());
        model.addAttribute("completedCourses",courseService.completed(loggedinuser));
        model.addAttribute("averageGrade",courseService.AverageGrade(loggedinuser));
        model.addAttribute("totalAssessments",assessmentService.getAssessments(loggedinuser).size());
        model.addAttribute("yearOfStudy",userService.yearOfStudy(loggedinuser));
        model.addAttribute("gpa",courseService.GPA(loggedinuser));
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
    @GetMapping("/update")
    public String showUpdateForm(Model model, Principal principal){
        model.addAttribute("user",userService.getUser(principal.getName()));
        model.addAttribute("degrees",degreeService.getDegrees());

        return "user/updateProfile";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user, Principal principal,Model model){
        String username = principal.getName();
        try{
            User currentUser = userService.getUser(username);
            currentUser.setDegree(user.getDegree());
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setSelectedYear(user.getSelectedYear());

            userService.saveUser(currentUser);

            return "redirect:/user/profile";
        } catch (Exception e) {
            model.addAttribute("error",e.getMessage());
            return "redirect:/user/update";
        }
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
            @RequestParam String password,
            @RequestParam String currentPassword,
            @RequestParam String confirmPassword,
            @ModelAttribute User user,
            Principal principal,
            Model model) {
        String username = principal.getName();

        if(!password.equals(confirmPassword)){
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
            User currentuser = userService.getUser(username);
            currentuser.setPassword(passwordEncoder.encode(password));
            userService.saveUser(currentuser);
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