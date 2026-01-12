package com.GradeGoal.service;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private UserService userService;

    public List<Assessment> getAssessments(String studentNo){
        List<Assessment> assessments = assessmentRepository.findAll();
        List<Assessment> userAssessments = new ArrayList<>();

        for(Assessment x : assessments){
            if(x.getUser().getStudentNo().equals(studentNo)){
                userAssessments.add(x);
            }
        }

        return userAssessments;
    }

    public Assessment getAssessment(Long id){
        return assessmentRepository.getReferenceById(id);
    }

    public Assessment saveAssessment(Assessment assessment){
        return assessmentRepository.save(assessment);
    }

    /* count pending assessments */
    public int pending(String studNo){
        List<Assessment> assessments = assessmentRepository.findAll();
        List<Assessment> userAssessments = new ArrayList<>();

        for(Assessment x : assessments){
            if(x.getUser().getStudentNo().equals(studNo)){
                userAssessments.add(x);
            }
        }
        int pending = 0;

        for(Assessment x : userAssessments){
            if(x.getDate().isAfter(LocalDate.now()) && x.getActualMark()==0){
                pending++;
            }
        }

        return pending;
    }

    /* count completed assessments */
    public int completed(String studNo){
        List<Assessment> assessments = assessmentRepository.findAll();
        List<Assessment> userAssessments = new ArrayList<>();

        for(Assessment x : assessments){
            if(x.getUser().getStudentNo().equals(studNo)){
                userAssessments.add(x);
            }
        }
        int completed = 0;

        for(Assessment x : userAssessments){
            if(x.getDate().isBefore(LocalDate.now()) && x.getActualMark()!=0){
                completed++;
            }
        }

        return completed;
    }

    public double AverageGrade(String studNo){
        List<Assessment> assessments = userService.getUser(studNo).getAssessments();
        double average = 0;

        if(assessments.isEmpty())
            return Math.round(average);

        double total = assessments.stream().mapToDouble(Assessment::getActualMark).sum();

        /*for(Assessment x : assessments){
            average += x.getActualMark();
        }*/

        return Math.round(total / assessments.size());

    }

    public void deleteAssessment(Assessment assessment){
        assessmentRepository.delete(assessment);
    }
}
