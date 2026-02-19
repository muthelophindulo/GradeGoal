package com.GradeGoal.service;

import com.GradeGoal.model.Goal;
import com.GradeGoal.repository.GoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoalService {
    @Mock
    private GoalRepository goalRepository;

    @Test
    void getGoalsTest(){
        Goal mockgoal = new Goal();

        mockgoal.setStartDate(LocalDate.now());
        when(goalRepository.findAll().stream().filter(goal -> goal.getStartDate().getYear() == 2026)).equals(Optional.of(mockgoal));
    }

}
