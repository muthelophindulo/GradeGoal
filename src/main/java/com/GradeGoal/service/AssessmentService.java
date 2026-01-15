package com.GradeGoal.service;

import com.GradeGoal.model.Assessment;
import com.GradeGoal.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private UserService userService;

    public List<Assessment> getAssessments(String studNo){
        return getYearlyAssessments(studNo);
    }

    public Assessment getAssessment(Long id){
        return assessmentRepository.getReferenceById(id);
    }

    public Assessment saveAssessment(Assessment assessment){
        return assessmentRepository.save(assessment);
    }

    public void deleteAssessment(Assessment assessment){
        assessmentRepository.delete(assessment);
    }

    /* count pending assessments */
    public int pending(String studNo){

        List<Assessment> userAssessments = getYearlyAssessments(studNo);

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
        List<Assessment> userAssessments =getYearlyAssessments(studNo);


        int completed = 0;

        for(Assessment x : userAssessments){
            if(x.getDate().isBefore(LocalDate.now()) && x.getActualMark()!=0){
                completed++;
            }
        }

        return completed;
    }

    public double AverageGrade(String studNo){
        List<Assessment> assessments = getYearlyAssessments(studNo);
        double average = 0;

        if(assessments.isEmpty())
            return Math.round(average);

        double total = assessments.stream().mapToDouble(Assessment::getActualMark).sum();

        /*for(Assessment x : assessments){
            average += x.getActualMark();
        }*/

        return Math.round(total / assessments.size());

    }

    /* get the top assessments */
    public List<Assessment> topAssessments(String studNo){
        List<Assessment> userAssessments = getYearlyAssessments(studNo)
                .stream()
                .sorted(Comparator.comparing(Assessment::getActualMark).reversed())
                .toList();

        List<Assessment> top5 = new ArrayList<>();
        double largest = -1;
        int count =0;

        for(Assessment x : userAssessments){
            if(x.getActualMark() >= largest){
                top5.add(x);
                count++;
            }
            if(count == 5){
                break;
            }
        }

        return top5;
    }

    /* used to load assessments of the specefic year the user selected */
    public List<Assessment> getYearlyAssessments(String user){
        return userService.getUser(user).getAssessments()
                .stream()
                .filter(assessment -> assessment
                        .getDate()
                        .getYear() == userService.getUser(user).getSelectedYear())
                .toList();
    }

}
