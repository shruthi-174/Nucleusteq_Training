package com.shruthi.food.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shruthi.food.config.JwtProvider;
import com.shruthi.food.entity.User;
import com.shruthi.food.repository.UserRepository;
import com.shruthi.food.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Override
	public User findUserByJwtToken(String jwt) throws Exception {
		String email=jwtProvider.getEmailFromJwtToken(jwt);
		User user=userRepository.findByEmail(email);
		
		if(user==null)
			throw new Exception("User not found");
		
		return user;
	}

}
