package com.GradeGoal.service;

import com.GradeGoal.model.TimeTable;
import com.GradeGoal.model.User;
import com.GradeGoal.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimetableService {

    @Autowired
    private TimetableRepository repository;

    public Map<String, Map<String, TimeTable>> getGrid(User user, Integer year, Integer semester) {
        List<TimeTable> entries = repository.findByUserAndAcademicYearAndSemester(user, year, semester);

        Map<String, Map<String, TimeTable>> grid = new HashMap<>();
        for (TimeTable entry : entries) {
            grid.putIfAbsent(entry.getDayOfWeek(), new HashMap<>());
            grid.get(entry.getDayOfWeek()).put(entry.getTimeSlot(), entry);
        }
        return grid;
    }
}

