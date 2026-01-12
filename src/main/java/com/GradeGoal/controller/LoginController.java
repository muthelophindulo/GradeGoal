package com.GradeGoal.controller;

import com.GradeGoal.repository.DegreeRepository;
import com.GradeGoal.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    private DegreeRepository degreeRepository;
    @Autowired
    private CourseService courseService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/")
    public String home(){
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        return "redirect:/login?logout=true";
    }
}
