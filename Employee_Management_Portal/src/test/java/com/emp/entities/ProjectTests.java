package com.emp.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProjectTests {

    private Project project;
    private User userMock;
    private Assignment assignmentMock;
    private RequestResource requestResourceMock;

    @BeforeEach
    public void setUp() {
        project = new Project();
        userMock = mock(User.class);
        assignmentMock = mock(Assignment.class);
        requestResourceMock = mock(RequestResource.class);
    }

    @AfterEach
    public void tearDown() {
        project = null;
        userMock = null;
        assignmentMock = null;
        requestResourceMock = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test projectId
        project.setProjectId(1L);
        assertEquals(Long.valueOf(1L), project.getProjectId());

        // Test name
        project.setName("ProjectName");
        assertEquals("ProjectName", project.getName());

        // Test description
        project.setDescription("ProjectDescription");
        assertEquals("ProjectDescription", project.getDescription());

        // Test manager
        project.setManager(userMock);
        assertEquals(userMock, project.getManager());

        // Test projectAssignments
        List<Assignment> assignments = new ArrayList<>();
        assignments.add(assignmentMock);
        project.setProjectAssignments(assignments);
        assertEquals(assignments, project.getProjectAssignments());

        // Test projectRequestResources
        List<RequestResource> requestResources = new ArrayList<>();
        requestResources.add(requestResourceMock);
        project.setProjectRequestResources(requestResources);
        assertEquals(requestResources, project.getProjectRequestResources());
    }
}
