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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String grades(Model model, Principal principal) {
        String loggedinuser = principal.getName();
        
        // User info
        model.addAttribute("user", userService.getUser(loggedinuser));
        
        // Get count of all courses for the user
        List<Course> allCourses = courseService.getCourses(loggedinuser);
        model.addAttribute("totalCourses", allCourses.size());
        
        // Calculate average grade
        double avgGrade = userService.AverageGrade(loggedinuser);
        model.addAttribute("averageGrade", String.format("%.1f", avgGrade));
        
        // Completed assessments count
        int completedAssessments = assessmentService.completed(loggedinuser);
        model.addAttribute("completedAssessments", completedAssessments);
        
        // Target achievement percentage
        double targetAchievement = courseService.targetArchieved(loggedinuser);
        model.addAttribute("targetAchievement", String.format("%.1f", targetAchievement));
        
        // Semester 1 courses
        List<Course> semester1courses = allCourses.stream()
                .filter(course -> course.getSemester() == 1)
                .collect(Collectors.toList());
        model.addAttribute("semester1Courses", semester1courses);
        
        // Semester 1 completed count
        long sem1Completed = semester1courses.stream()
                .filter(course -> course.getActualMark() >= course.getPassMark())
                .count();
        model.addAttribute("sem1Completed", sem1Completed);
        
        // Semester 2 courses
        List<Course> semester2courses = allCourses.stream()
                .filter(course -> course.getSemester() == 2)
                .collect(Collectors.toList());
        model.addAttribute("semester2Courses", semester2courses);
        
        // Semester 2 completed count
        long sem2Completed = semester2courses.stream()
                .filter(course -> course.getActualMark() >= course.getPassMark())
                .count();
        model.addAttribute("sem2Completed", sem2Completed);
        

        Map<String, Object> allChartData = new HashMap<>();
        
        for (Course course : allCourses) {
            if (course.getAssessments() != null && !course.getAssessments().isEmpty()) {
                Map<String, Object> courseData = new HashMap<>();
                
                // Get assessment data
                List<String> names = new ArrayList<>();
                List<Double> marks = new ArrayList<>();
                List<Double> weights = new ArrayList<>();
                
                for (Assessment assessment : course.getAssessments()) {
                    names.add(assessment.getName());
                    marks.add(assessment.getActualMark());
                    weights.add(assessment.getWeigh());
                }
                
                courseData.put("names", names);
                courseData.put("marks", marks);
                courseData.put("weights", weights);
                courseData.put("target", course.getTargetMark());
                courseData.put("actual", course.getActualMark());
                courseData.put("pass", course.getPassMark());
                
                allChartData.put(course.getId().toString(), courseData);
            }
        }
        

        model.addAttribute("allChartData", allChartData);
        
        // Calculate overall
        double overallAverage = allCourses.stream()
                .filter(c -> c.getActualMark() > 0)
                .mapToDouble(Course::getActualMark)
                .average()
                .orElse(0.0);
        model.addAttribute("overallAverage", String.format("%.0f",overallAverage));
        model.addAttribute("completedPercentage",courseService.completed(loggedinuser));
        model.addAttribute("targetsMet",courseService.targetArchieved(loggedinuser));
        model.addAttribute("currentGPA", String.format("%.2f", userService.GPA(loggedinuser)));
        
        return "grades/dataAnalysis";
    }
}