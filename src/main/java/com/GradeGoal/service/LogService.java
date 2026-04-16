package com.GradeGoal.service;

import com.GradeGoal.Dto.LogDto;
import com.GradeGoal.Mapper.LogMapper;
import com.GradeGoal.model.Log;
import com.GradeGoal.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final LogMapper logMapper;

    public LogDto getLog(Long id){
        return logMapper.toDto(logRepository.getReferenceById(id));
    }

    public LogDto saveLog(Log log){
        return logMapper.toDto(logRepository.save(log));
    }

    public void deleteLog(LogDto logDto){
        logRepository.deleteById(logDto.getId());
    }


}
