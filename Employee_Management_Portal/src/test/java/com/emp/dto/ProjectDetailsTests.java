package com.emp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectDetailsTests {

    private ProjectDetails projectDetails;

    @BeforeEach
    public void setUp() {
        projectDetails = new ProjectDetails();
    }

    @AfterEach
    public void tearDown() {
        projectDetails = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test projectId
        projectDetails.setProjectId(1L);
        assertEquals(Long.valueOf(1L), projectDetails.getProjectId());

        // Test projectName
        projectDetails.setProjectName("Project A");
        assertEquals("Project A", projectDetails.getProjectName());

        // Test managerName
        projectDetails.setManagerName("Manager A");
        assertEquals("Manager A", projectDetails.getManagerName());

        // Test employeeNames
        List<String> employeeNames = new ArrayList<>();
        employeeNames.add("Employee1");
        employeeNames.add("Employee2");
        projectDetails.setEmployeeNames(employeeNames);
        assertEquals(employeeNames, projectDetails.getEmployeeNames());
    }
}
