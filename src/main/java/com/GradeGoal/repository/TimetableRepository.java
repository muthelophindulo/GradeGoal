package com.GradeGoal.repository;

import com.GradeGoal.model.TimeTable;
import com.GradeGoal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimetableRepository extends JpaRepository<TimeTable, Long> {
    List<TimeTable> findByAcademicYearAndSemester(Integer academicYear, Integer semester);
    List<TimeTable> findByUserAndAcademicYearAndSemester(User user, Integer academicYear, Integer semester);
    TimeTable getById(Long id);
}

