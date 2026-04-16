package com.GradeGoal.Mapper;

import com.GradeGoal.Dto.LogDto;
import com.GradeGoal.model.Log;
import com.GradeGoal.repository.LogRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LogMapper {
    LogDto toDto(Log log);
    Log toEntity(LogDto logDto);

    List<LogDto> toDtos(List<Log> logList);
    List<Log> toEntities(List<LogDto> logDtoList);
}
