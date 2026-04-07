package com.GradeGoal.Mapper;

import com.GradeGoal.Dto.UserDto;
import com.GradeGoal.model.User;
import com.GradeGoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    @Autowired
    private static UserRepository userRepository;

    public static UserDto mapToDto(User user){
        return new UserDto(
                user.getStudentNo(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User mapToUser(UserDto userDto){
        return userRepository.findByStudentNo(userDto.getStudentNo());
    }

    public static List<UserDto> mapToDtos(List<User> userList){
        List<UserDto> userDtoList = new ArrayList<>();

        userList.forEach(
                user -> userDtoList.add(new UserDto(
                        user.getStudentNo(),
                        user.getName(),
                        user.getEmail()
                ))
        );

        return userDtoList;
    }

    public static List<User> mapToUsers(List<UserDto> userDtoList){
        List<User> userList = new ArrayList<>();

        userDtoList.forEach(userDto -> userList.add(userRepository.findByStudentNo(userDto.getStudentNo())));

        return userList;
    }
}
