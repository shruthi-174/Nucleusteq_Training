package com.shruthi.food.service;

import com.shruthi.food.entity.User;

public interface UserService {

	public User findUserByJwtToken(String jwt) throws Exception;
	
}
