package com.GradeGoal.config;

import com.GradeGoal.model.User;
import com.GradeGoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User db_user = userRepository.findByStudentNo(username);

        if(db_user == null)
            throw  new UsernameNotFoundException("username not found");
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(db_user.getPassword())
                .roles(db_user.getRole())
                .build();
    }
}
