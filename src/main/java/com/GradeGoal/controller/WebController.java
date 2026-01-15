package com.GradeGoal.controller;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.Course;
import com.GradeGoal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/dashboard")
    public String dashboard(Model model,Principal principal){


        String loggedInUser = principal.getName();


        if(userService.getUser(loggedInUser).getCourses().isEmpty() || userService.getUser(loggedInUser).getAssessments().isEmpty()){
            return "redirect:/assessment/new";
        }else {

            model.addAttribute("user", userService.getUser(loggedInUser));
            model.addAttribute("courses",courseService.topCourses(loggedInUser));
            model.addAttribute("assessments",assessmentService.topAssessments(loggedInUser));
            model.addAttribute("goals",goalService.getGoals());


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


}
