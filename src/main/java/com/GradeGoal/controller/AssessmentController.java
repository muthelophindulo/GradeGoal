package com.GradeGoal.controller;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.service.AssessmentService;
import com.GradeGoal.service.CourseService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("assessment/")
public class AssessmentController {
    @Autowired
    private UserService userService;
    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("list")
    public String List(Model model, Principal principal){
        model.addAttribute("user",userService.getUser(principal.getName()));
        model.addAttribute("title","All Assessments");
        model.addAttribute("courses",courseService.getCourses(principal.getName()));

        model.addAttribute("completedCount",assessmentService.completed(principal.getName()));

        model.addAttribute("pendingCount",assessmentService.pending(principal.getName()));

        model.addAttribute("averageAchievement",Math.round(assessmentService.AverageGrade(principal.getName())));

        model.addAttribute("assessments",assessmentService.getAssessments(principal.getName()));

        return "assessment/assessments";
    }

    @GetMapping("/new")
    public String AddAssessment(Model model,Principal principal
    ){
        model.addAttribute("user", userService.getUser(principal.getName()));
        model.addAttribute("assessment",new Assessment());
        model.addAttribute("courses",courseService.getCourses(principal.getName()));

        return "assessment/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute Assessment assessment,
            Principal principal
    ){
        try {
            if(assessment.getDate() == null){
                assessment.setDate(LocalDate.now());
            }
            assessment.setUser(userService.getUser(principal.getName()));
            assessmentService.saveAssessment(assessment);

            return "redirect:/assessment/list";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model, Principal principal){
        model.addAttribute("user", userService.getUser(principal.getName()));
        model.addAttribute("assessment",assessmentService.getAssessment(id));
        model.addAttribute("courses",courseService.getCourses(principal.getName()));

        return "assessment/form";
    }

    @PostMapping("/update")
    public String update(
            @ModelAttribute Assessment assessment,
            Principal principal
    ){
        try {
            assessment.setUser(userService.getUser(principal.getName()));
            assessmentService.saveAssessment(assessment);

            return "redirect:/assessment/list";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("delete/{id}")
    public String deleteAssessment(@PathVariable Long id){
        assessmentService.deleteAssessment(assessmentService.getAssessment(id));

        return "redirect:/assessment/list";
    }
}
