package com.GradeGoal.service;

import com.GradeGoal.model.Course;
import com.GradeGoal.model.User;
import com.GradeGoal.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AssessmentService assessmentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CourseService courseService;

    private User testuser;
    private List<Course> testCourses;
    private String studNo;

    @BeforeEach
    void setup(){
        testuser = new User();
        testuser.setStudentNo("225004680");

        Course c1 = new Course();
        c1.setCode("1234");

        Course c2 = new Course();
        c2.setCode("12345");

        testCourses = Arrays.asList(c1,c2);

        testuser.setCourses(testCourses);

    }

    @Test
    void getCourses() {
        studNo = "225004680";
        when(userService.getUser(studNo)).thenReturn(testuser);

        List<Course> acctual = userService.getUser(studNo).getCourses();

        assertNotNull(acctual);
        assertEquals(2,acctual.size());
    }

    @Test
    void saveCourseTest() {
        Course newCourse = new Course();
        newCourse.setCode("123");

        when(courseRepository.save(any(Course.class))).thenReturn(newCourse);

        Course results = courseRepository.save(newCourse);

        assertNotNull(results);
        assertEquals("123",results.getCode());
    }

    @Test
    void getById() {
        Course test = new Course();
        test.setId(1L);
        when(courseRepository.getReferenceById(1L)).thenReturn(test);

        Course results = courseRepository.getReferenceById(1L);

        assertNotNull(results);
        assertEquals(test,results);
    }
}