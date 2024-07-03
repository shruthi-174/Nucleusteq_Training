package com.shruthi.food.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shruthi.food.config.JwtProvider;
import com.shruthi.food.entity.Cart;
import com.shruthi.food.entity.USER_ROLE;
import com.shruthi.food.entity.User;
import com.shruthi.food.repository.CartRepository;
import com.shruthi.food.repository.UserRepository;
import com.shruthi.food.request.LoginRequest;
import com.shruthi.food.response.AuthResponse;
import com.shruthi.food.service.CustomerUserDetailsService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;

	@Autowired
	private CartRepository cartRepository;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user) throws Exception{
		User isEmailExits=userRepository.findByEmail(user.getEmail());
		
		//if user exists throw Exception
		if(isEmailExits!=null) {
			throw new Exception("Email is already exists");
		}
		
		//create new user
		User newUser=new User();
		newUser.setEmail(user.getEmail());
		newUser.setFullName(user.getFullName());
		newUser.setRole(user.getRole());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		
		//save user 
		User savedUser=userRepository.save(newUser);
		
		//Creating cart for new User
		Cart cart=new Cart();
		cart.setCustomer(savedUser);
		cartRepository.save(cart);
		
		Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt=jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Register Successfully");
		authResponse.setRole(savedUser.getRole());
		
		return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req) throws Exception{
		String username=req.getEmail();
		String password=req.getPassword();
		
		Authentication authentication=authenticate(username,password);
		
		Collection<? extends GrantedAuthority>authorities=authentication.getAuthorities();
		String role=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
		
		String jwt=jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse=new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Login Successfully");
		authResponse.setRole(USER_ROLE.valueOf(role));
		
		return new ResponseEntity<>(authResponse,HttpStatus.OK);

	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails=customerUserDetailsService.loadUserByUsername(username);
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid username");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
