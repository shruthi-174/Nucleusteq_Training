package com.emp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestResourceRequestTests {

    private RequestResourceRequest requestResourceRequest;

    @BeforeEach
    public void setUp() {
        requestResourceRequest = new RequestResourceRequest();
    }

    @AfterEach
    public void tearDown() {
        requestResourceRequest = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test projectId
        requestResourceRequest.setProjectId(1L);
        assertEquals(Long.valueOf(1L), requestResourceRequest.getProjectId());

        // Test employeeEmails
        List<String> employeeEmails = new ArrayList<>();
        employeeEmails.add("employee1@nucleusteq.com");
        employeeEmails.add("employee2@nucleusteq.com");
        requestResourceRequest.setEmployeeEmails(employeeEmails);
        assertEquals(employeeEmails, requestResourceRequest.getEmployeeEmails());

        // Test managerId
        requestResourceRequest.setManagerUserId(1L);
        assertEquals(Long.valueOf(1L), requestResourceRequest.getManagerId());
    }

    @Test
    public void testConstructor() {
        List<String> employeeEmails = new ArrayList<>();
        employeeEmails.add("employee1@nucleusteq.com");
        employeeEmails.add("employee2@nucleusteq.com");

        RequestResourceRequest requestResourceRequest = new RequestResourceRequest(1L, employeeEmails, 1L);

        assertEquals(Long.valueOf(1L), requestResourceRequest.getProjectId());
        assertEquals(employeeEmails, requestResourceRequest.getEmployeeEmails());
        assertEquals(Long.valueOf(1L), requestResourceRequest.getManagerId());
    }
}

