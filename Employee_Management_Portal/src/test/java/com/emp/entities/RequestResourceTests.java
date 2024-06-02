package com.emp.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RequestResourceTests {

	private RequestResource requestResource;
    private User managerMock;
    private Project projectMock;
    private User employeeMock;
    private RequestResource.RequestStatus status;

    @BeforeEach
    public void setUp() {
        requestResource = new RequestResource();
        managerMock = mock(User.class);
        projectMock = mock(Project.class);
        employeeMock = mock(User.class);
        status = RequestResource.RequestStatus.PENDING;
    }

    @AfterEach
    public void tearDown() {
        requestResource = null;
        managerMock = null;
        projectMock = null;
        employeeMock = null;
        status = null;
    }
    @Test
    public void testGettersAndSetters() {
        // Test requestId
        requestResource.setRequestId(1L);
        assertEquals(Long.valueOf(1L), requestResource.getRequestId());

        // Test manager
        requestResource.setManager(managerMock);
        assertEquals(managerMock, requestResource.getManager());

        // Test project
        requestResource.setProject(projectMock);
        assertEquals(projectMock, requestResource.getProject());

        // Test employee
        requestResource.setEmployee(employeeMock);
        assertEquals(employeeMock, requestResource.getEmployee());

        // Test status
        requestResource.setStatus(status);
        assertEquals(status, requestResource.getStatus());
    }
    
    @Test
    public void testConstructor() {
        Long requestId = 1L;

        RequestResource requestResource = new RequestResource(requestId, managerMock, projectMock, employeeMock, status);
        assertEquals(requestId, requestResource.getRequestId());
        assertEquals(managerMock, requestResource.getManager());
        assertEquals(projectMock, requestResource.getProject());
        assertEquals(employeeMock, requestResource.getEmployee());
        assertEquals(status, requestResource.getStatus());
    }
}
