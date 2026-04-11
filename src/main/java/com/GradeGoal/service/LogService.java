package com.GradeGoal.service;

import com.GradeGoal.Dto.LogDto;
import com.GradeGoal.Mapper.LogMapper;
import com.GradeGoal.model.Log;
import com.GradeGoal.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public LogDto getLog(Long id){
        return LogMapper.mapToDto(logRepository.getReferenceById(id));
    }

    public LogDto saveLog(Log log){
        return LogMapper.mapToDto(logRepository.save(log));
    }

    public void deleteLog(LogDto logDto){
        logRepository.deleteById(logDto.getId());
    }


}
