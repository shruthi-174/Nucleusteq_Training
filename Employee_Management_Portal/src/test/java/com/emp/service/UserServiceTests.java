package com.emp.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.emp.entities.User;
import com.emp.repository.UserRepository;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    
    @BeforeEach
    public void setUp() {
    	 MockitoAnnotations.openMocks(this);	
    }
    
    @AfterEach
    public void tearDown() {
    }
   
    @Test
    void testLoadUserByUsername() {
        String email = "emp@nuclesusteq.com";
        String password = "password";
        User.Role role = User.Role.ADMIN;

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        // User not exist
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(email));
        assertEquals("User not found with email: " + email, exception.getMessage());

        // User exists
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        List<GrantedAuthority> expectedRole = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
        List<GrantedAuthority> actualRole = userDetails.getAuthorities()
                .stream()
                .collect(Collectors.toList());
        assertEquals(expectedRole, actualRole);        
    }
    
    @Test
    void testGetUser() {
    	 Long userId = 1L;
         User user = new User();
         user.setUserId(userId);
         user.setEmail("emp@nucleusteq.com");
         
         //User does not exist
         when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
         IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, () -> userService.getUser(userId));
         assertEquals("User not found with ID: " + userId, exception.getMessage());

         //User exists
         when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
         User resultExists = userService.getUser(userId);
         assertEquals(userId, resultExists.getUserId());
         assertEquals("emp@nucleusteq.com", resultExists.getEmail());


    }
    
}
