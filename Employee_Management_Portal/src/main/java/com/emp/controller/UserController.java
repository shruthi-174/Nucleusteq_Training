package com.emp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.emp.config.JwtUtil;
import com.emp.dto.UserRequest;
import com.emp.entities.User;
import com.emp.repository.UserRepository;
import com.emp.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserRequest userRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect email or password");
        }

        final UserDetails userDetails = userService.loadUserByUsername(userRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt); // Return JWT token
    }

//    @GetMapping("/api/users/{userId}")
//    public ResponseEntity<?> getUser(@PathVariable Long userId) {
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
//        return ResponseEntity.ok(user);
//    }
    
    @GetMapping("/api/users/role")
    public ResponseEntity<?> getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
        UserDetails userDetails = userService.loadUserByUsername(userEmail);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        
        return ResponseEntity.ok(role);
    }
    @GetMapping("/api/users/email")
    public ResponseEntity<?> getUserByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }
        return ResponseEntity.ok(user);
    }
}
