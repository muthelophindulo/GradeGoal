package com.GradeGoal.controller;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.Course;
import com.GradeGoal.service.AssessmentService;
import com.GradeGoal.service.CourseService;
import com.GradeGoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/grades")
public class GradesController {
    @Autowired
    private UserService userService;

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String grades(Model model, Principal principal){
        String loggedinuser = principal.getName();
        model.addAttribute("user",userService.getUser(loggedinuser));
        model.addAttribute("totalCourses",courseService.getCourses(loggedinuser).size());
        model.addAttribute("averageGrade",userService.AverageGrade(loggedinuser));
        model.addAttribute("completedAssessments",assessmentService.completed(loggedinuser));
        model.addAttribute("targetAchievement",courseService.targetArchieved(loggedinuser));

        List<Course> semester1courses = courseService.getCourses(loggedinuser)
                .stream()
                .filter(course -> course.getSemester() == 1)
                .toList();
        model.addAttribute("semester1Courses",semester1courses);
        model.addAttribute("sem1Completed",4);

        List<Course> semester2courses = courseService.getCourses(loggedinuser)
                .stream()
                .filter(course -> course.getSemester() == 2)
                .toList();
        model.addAttribute("semester2Courses",semester2courses);
        model.addAttribute("sem2Completed",4);

        HashMap<Long, List<Assessment>> courseAssessment = new HashMap<>();
        List<Course> courses = courseService.getCourses(loggedinuser);
        for(Course x : courses){
            courseAssessment.put(x.getId(),x.getAssessments());
        }

        model.addAttribute("assessmentsByCourseId", courseAssessment);

        model.addAttribute("overallAverage",50);
        model.addAttribute("completedPercentage",90);
        model.addAttribute("targetsMet",70);
        model.addAttribute("currentGPA",userService.GPA(loggedinuser));




        return "grades/dataAnalysis";
    }
}
