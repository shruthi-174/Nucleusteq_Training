package com.emp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRequestTests {

    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        userRequest = new UserRequest();
    }

    @AfterEach
    public void tearDown() {
        userRequest = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test firstname
        userRequest.setFirstname("Employee1");
        assertEquals("Employee1", userRequest.getFirstname());

        // Test lastname
        userRequest.setLastname("emp");
        assertEquals("emp", userRequest.getLastname());

        // Test email
        userRequest.setEmail("employee1@nucleusteq.com");
        assertEquals("employee1@nucleusteq.com", userRequest.getEmail());

        // Test password
        userRequest.setPassword("password123");
        assertEquals("password123", userRequest.getPassword());

        // Test role
        userRequest.setRole("EMPLOYEE");
        assertEquals("EMPLOYEE", userRequest.getRole());
    }

}
