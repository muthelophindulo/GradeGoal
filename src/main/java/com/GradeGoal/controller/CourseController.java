package com.GradeGoal.controller;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.Course;
import com.GradeGoal.service.AssessmentService;
import com.GradeGoal.service.CourseService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("course/")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("list")
    public String list(Model model, Principal principal){
        String loggedinuser = principal.getName();
        model.addAttribute("user",userService.getUser(loggedinuser));
        model.addAttribute("map",courseService.examMark(loggedinuser));
        model.addAttribute("courses",courseService.getCourses(loggedinuser));
        model.addAttribute("completedCourses",courseService.completed(loggedinuser));
        model.addAttribute("averageGrade",Math.round(userService.AverageGrade(loggedinuser)));
        model.addAttribute("targetAchievement",Math.round(courseService.targetArchieved(loggedinuser)));
        return "course/courses";
    }

    @GetMapping("new")
    public String add(Model model,Principal principal){
        String looginuser = principal.getName();
        model.addAttribute("user", userService.getUser(looginuser));
        model.addAttribute("course",new Course());
        return "course/form";
    }

    @PostMapping("save")
    public String save(@ModelAttribute Course course, Principal principal){
        String logginuser = principal.getName();
        course.setUser(userService.getUser(logginuser));
        courseService.saveCourse(course);

        return "redirect:/course/list";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable Long id, Principal principal, Model model){
        String looginuser = principal.getName();
        model.addAttribute("user", userService.getUser(looginuser));
        model.addAttribute("course",courseService.getById(id));
        return "course/form";
    }

    @GetMapping("view/{id}")
    public String view(@PathVariable Long id, Principal principal, Model model){
        String looginuser = principal.getName();

        List<Assessment> assessments = assessmentService.getAssessments(looginuser).stream()
                        .filter(Assessment -> Objects.equals(Assessment.getCourse().getId(), id))
                        .toList();

        model.addAttribute("user",userService.getUser(principal.getName()));
        model.addAttribute("title",courseService.getById(id).getCode() + " Assessments");
        model.addAttribute("courses",courseService.getCourses(principal.getName()));

        model.addAttribute("completedCount",assessments.stream().mapToDouble(Assessment::getActualMark).count());

        model.addAttribute("pendingCount",assessmentService.pending(principal.getName()));

        model.addAttribute("averageAchievement",Math.round(assessments.stream().mapToDouble(Assessment::getActualMark).sum() / assessments.size()));

        model.addAttribute("assessments",assessments);

        return "assessment/assessments";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id){
        courseService.delete(id);
        return "redirect:/course/list";
    }

    @PostMapping("update")
    public String update(@ModelAttribute Course course, Principal principal){
        String logginuser = principal.getName();
        course.setUser(userService.getUser(logginuser));
        courseService.saveCourse(course);

        return "redirect:/course/list";
    }
}
