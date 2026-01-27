package com.GradeGoal.service;

import com.GradeGoal.model.Course;
import com.GradeGoal.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CourseService {
    private CourseRepository courseRepository;
    @Test
    public void saveCourse(){
        courseRepository.save(new Course());
    }

}
