package com.emp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectRequestTests {

    private ProjectRequest projectRequest;

    @BeforeEach
    public void setUp() {
        projectRequest = new ProjectRequest();
    }

    @AfterEach
    public void tearDown() {
        projectRequest = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test projectId
        projectRequest.setProjectId(1L);
        assertEquals(Long.valueOf(1L), projectRequest.getProjectId());

        // Test name
        projectRequest.setName("Project 1");
        assertEquals("Project 1", projectRequest.getName());

        // Test description
        projectRequest.setDescription("Description 1");
        assertEquals("Description 1", projectRequest.getDescription());

        // Test managerId
        projectRequest.setManagerId(1L);
        assertEquals(Long.valueOf(1L), projectRequest.getManagerId());
    }

    @Test
    public void testConstructor() {
        ProjectRequest projectRequest = new ProjectRequest(1L, "Project 1", "Description 1", 1L);

        assertEquals(Long.valueOf(1L), projectRequest.getProjectId());
        assertEquals("Project 1", projectRequest.getName());
        assertEquals("Description 1", projectRequest.getDescription());
        assertEquals(Long.valueOf(1L), projectRequest.getManagerId());
    }
}
