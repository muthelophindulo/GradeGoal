package com.GradeGoal.service;

import com.GradeGoal.model.Course;
import com.GradeGoal.model.User;
import com.GradeGoal.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getById(Long id){
        return userRepository.getReferenceById(id);
    }

    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(User user){
        userRepository.deleteById(user.getId());
    }

    public User getUser(String studNo){
        return userRepository.findByStudentNo(studNo);
    }

    public double AverageGrade(String studNo){
        List<Course> courses = userRepository.findByStudentNo(studNo).getCourses();
        double average = 0;

        if(courses.isEmpty()){
            return average;
        }

        for(Course x : courses){
            average += x.getActualMark();
        }

        return average / courses.size();

    }

    public double GPA(String studNo){
        List<Course> courses = userRepository.findByStudentNo(studNo).getCourses();
        double gpa =0;

        for(Course x : courses){
            if(x.getActualMark() >= 70.0){
                gpa += 7;
            } else if (x.getActualMark() >= 60 && x.getActualMark() <= 69) {
                gpa+= 6;
            } else if (x.getActualMark() >= 50 && x.getActualMark() <= 59) {
                gpa+=5;
            }
            else {
                gpa+=0;
            }
        }

        return gpa / courses.size();
    }
}
