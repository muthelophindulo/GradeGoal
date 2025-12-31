package com.GradeGoal.service;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.model.Course;
import com.GradeGoal.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private final CourseRepository courseRepository;

    @Autowired
    private AssessmentService assessmentService;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCourses(String studentNo){
        List<Course> courses = courseRepository.findAll();
        List<Course> userCourses = new ArrayList<>();

        for(Course x : courses){
            if(x.getUser().getStudentNo().equals(studentNo)){
                userCourses.add(x);
            }
        }

        return userCourses;
    }

    public Course saveCourse(Course course){
        return courseRepository.save(course);
    }

    public Course getById(Long id){
        return courseRepository.getReferenceById(id);
    }

    public void delete(Long id){
        courseRepository.delete(courseRepository.getReferenceById(id));
    }

    public int completed(String studNo){
        List<Course> courses = courseRepository.findAll().stream()
                .filter(course -> course.getUser().getStudentNo().equals(studNo))
                .toList();
        int completed = 0;

        if(courses.isEmpty()){
            return completed;
        }

        for(Course x : courses){
            if(x.getActualMark() != 0){
                completed++;
            }
        }
        return completed;
    }

    public double targetArchieved(String studNo){
        List<Course> courses = courseRepository.findAll()
                .stream()
                .filter(course -> course.getUser().getStudentNo().equals(studNo))
                .toList();
        double target = 0;

        if (courses.isEmpty())
            return 0;

        for(Course x : courses){
            if(x.getActualMark() >= x.getTargetMark()){
                target++;
            }
        }

        return target / courses.size() * 100;
    }

    public List<Course> topCourses(String studNo){
        List<Course> top3 = new ArrayList<>();
        List<Course> courses = courseRepository.findAll().stream()
                .filter(course -> course.getUser().getStudentNo().equals(studNo))
                .toList()
                .stream()
                .sorted(Comparator.comparing(Course::getActualMark).reversed())
                .collect(Collectors.toList());
        double largest = courses.getFirst().getActualMark();
        int count =0;
        for(Course x : courses){
            top3.add(x);
            count++;
            if(count ==3){
                return top3;
            }
        }
        return top3;
    }

    //used to calculate exam mark based from course mark and the wighs of assessments
    public Map<String, Double> examMark(String studNo){
        Map<String, Double> weigh = new HashMap<>();

        List<Course> courses = courseRepository.findAll().stream()
                .filter(course -> course.getUser().getStudentNo().equals(studNo))
                .toList(); //courses belonging to a specefic user

        List<Assessment> assessments = assessmentService.getAssessments(studNo);

        for(Course x : courses){
            double v = x.getAssessments().stream()
                    .mapToDouble(Assessment::getWeigh)
                    .sum();
            double d = x.getAssessments().stream()
                    .mapToDouble(Assessment::getActualMark)
                    .sum() / x.getAssessments().size();
            //EM = FM - CM / %
            double s = (v / 100.0) * d;
            double f = 1 - (v / 100.0);
            double EM = Math.round((x.getActualMark() - s )/f );
            weigh.put(x.getCode(),EM);
        }

        System.out.println(weigh.values());
        System.out.println(weigh.keySet());

        return weigh;
    }
}
