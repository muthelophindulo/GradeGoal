package com.GradeGoal.Mapper;

import com.GradeGoal.Dto.LogDto;
import com.GradeGoal.model.Log;
import com.GradeGoal.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LogMapper {
    @Autowired
    private static LogRepository logRepository;

    public static LogDto mapToDto(Log log){
        if(log == null)
            return null;

        return new LogDto(
                log.getId(),
                log.getAction(),
                log.getCreatedAt()
        );
    }

    public static Log mapToLog(LogDto logDto){
        if(logDto == null)
            return null;
        return logRepository.getReferenceById(logDto.getId());
    }

    public static List<LogDto> mapToDtos(List<Log> logList){
        List<LogDto> logDtoList = new ArrayList<>();

        logList.forEach(Log -> logDtoList.add(new LogDto(Log.getId(), Log.getAction(), Log.getCreatedAt())));

        return logDtoList;
    }

    public static List<Log> mapToLogs(List<LogDto> logDtoList){
        List<Log> logList = new ArrayList<>();

        logDtoList.forEach(LogDto -> logList.add(logRepository.getReferenceById(LogDto.getId())));
        return logList;
    }
}
