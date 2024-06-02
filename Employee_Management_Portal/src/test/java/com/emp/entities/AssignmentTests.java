package com.emp.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AssignmentTests {

    private Assignment assignment;
    private User userMock;
    private Project projectMock;

    @BeforeEach
    public void setUp() {
        assignment = new Assignment();
        userMock = mock(User.class);
        projectMock = mock(Project.class);
    }

    @AfterEach
    public void tearDown() {
        assignment = null;
        userMock = null;
        projectMock = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test assignmentId
        assignment.setAssignmentId(1L);
        assertEquals(Long.valueOf(1L), assignment.getAssignmentId());

        // Test employee
        assignment.setEmployee(userMock);
        assertEquals(userMock, assignment.getEmployee());

        // Test project
        assignment.setProject(projectMock);
        assertEquals(projectMock, assignment.getProject());
    }
    
    @Test
    public void testConstructor() {
        Long assignmentId = 1L;

        Assignment assignment = new Assignment(assignmentId, userMock, projectMock);
        assertEquals(assignmentId, assignment.getAssignmentId());
        assertEquals(userMock, assignment.getEmployee());
        assertEquals(projectMock, assignment.getProject());
    }

}
