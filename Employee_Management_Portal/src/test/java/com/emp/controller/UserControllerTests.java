package com.emp.controller;
import com.emp.config.JwtUtil;
import com.emp.dto.UserRequest;
import com.emp.repository.UserRepository;
import com.emp.service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
	  @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private UserService userService;

	    @MockBean
	    private UserRepository userRepository;

	    @MockBean
	    private AuthenticationManager authenticationManager;

	    @MockBean
	    private JwtUtil jwtUtil;

	    @InjectMocks
	    private UserController userController;

	    private AutoCloseable closeable;

	    private UserRequest userRequest;

	    @BeforeEach
	    void setUp() {
	        closeable = MockitoAnnotations.openMocks(this);
	        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

	        userRequest = new UserRequest();
	        userRequest.setEmail("test@nucleusteq.com");
	        userRequest.setPassword("password");
	    }

	    @AfterEach
	    void tearDown() throws Exception {
	        closeable.close();
	    }


	    @Test
	    void testLogout_Success() throws Exception {
	        mockMvc.perform(post("/api/logout"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("Successfully logged out"));
	    }
	}