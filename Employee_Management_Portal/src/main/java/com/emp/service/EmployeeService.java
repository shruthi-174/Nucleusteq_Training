package com.emp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emp.dto.UserRequest;
import com.emp.entities.User;
import com.emp.repository.UserRepository;

@Service
public class EmployeeService {

	   @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    public boolean updateUserProfile(Long userId, UserRequest userRequest) {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
	        if (!user.getEmail().equals(userRequest.getEmail()) && userRepository.findByEmail(userRequest.getEmail()) != null) {
	            throw new IllegalArgumentException("Email already exists");
	        }
	        user.setFirstname(userRequest.getFirstname());
	        user.setLastname(userRequest.getLastname());
	        user.setEmail(userRequest.getEmail());
	        if (userRequest.getPassword() != null) {
	            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
	        }
	        userRepository.save(user);
	        return true;
	    }
}
