package com.GradeGoal.config;

import com.GradeGoal.model.Admin;
import com.GradeGoal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin db_admin = adminRepository.findByAdminNo(username);

        if(db_admin == null)
            throw new UsernameNotFoundException("username not found");

        return User
                .withUsername(username)
                .password(db_admin.getPassword())
                .roles(db_admin.getRole())
                .build();

    }
}
