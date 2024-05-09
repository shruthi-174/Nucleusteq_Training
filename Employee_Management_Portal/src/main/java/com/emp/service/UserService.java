package com.emp.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.emp.dto.UserRequest;
import com.emp.entities.User;

public interface UserService extends UserDetailsService {

    UserDetails loadUserByUsername(String email);

    User authenticateUser(UserRequest userRequest);

    boolean registerUser(UserRequest userRequest);

    boolean updateUserProfile(Long userId, UserRequest userRequest);

}