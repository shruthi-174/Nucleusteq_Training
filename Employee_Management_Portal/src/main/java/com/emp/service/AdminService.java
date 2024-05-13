package com.emp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emp.dto.UserRequest;
import com.emp.entities.Project;
import com.emp.entities.User;
import com.emp.repository.ProjectRepository;
import com.emp.repository.UserRepository;

@Service
public class AdminService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean registerUser(UserRequest userRequest) {
		String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
		if (userRepository.findByEmail(userRequest.getEmail()) != null) {
			throw new IllegalArgumentException("Email already exists");
		}
		User user = new User();
		user.setFirstname(userRequest.getFirstname());
		user.setLastname(userRequest.getLastname());
		user.setEmail(userRequest.getEmail());
		user.setPassword(encodedPassword);
		user.setRole(User.Role.valueOf(userRequest.getRole()));
		userRepository.save(user);
		return true;
	}

}
