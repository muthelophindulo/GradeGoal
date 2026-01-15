package com.GradeGoal.service;

import com.GradeGoal.model.Goal;
import com.GradeGoal.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private UserService userService;

    public List<Goal> getGoals(){
        List<Goal> goals = goalRepository.findAll().stream()
                .filter(goal -> goal.getEndDate().getYear() == goal.getUser().getSelectedYear())
                .toList();
        goals.forEach(goal -> {
            Long daysRemaining = ChronoUnit.DAYS.between(goal.getStartDate(),goal.getEndDate());
            goal.setDaysRemaining(daysRemaining);
            goal.setIsOverdue(LocalDate.now().isAfter(goal.getEndDate()));
            goal.setProgress(goal.getActual() > 0 ? goal.getActual() / goal.getTarget() * 100.0 : 0.0);
        });
        return goalRepository.findAll();
    }

    public Goal getGoal(Long id){
        return goalRepository.getReferenceById(id);
    }

    public Goal save(Goal goal){
        return goalRepository.save(goal);
    }


    public void delete(Long id){
        goalRepository.delete(goalRepository.getReferenceById(id));
    }
}
