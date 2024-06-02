package com.emp.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserTests {

    private User user;
    private Project projectMock;
    private RequestResource requestResourceMock;
    private Skill skillMock;

    @BeforeEach
    public void setUp() {
        user = new User();
        projectMock = mock(Project.class);
        requestResourceMock = mock(RequestResource.class);
        skillMock = mock(Skill.class);
    }

    @AfterEach
    public void tearDown() {
        user = null;
        projectMock = null;
        requestResourceMock = null;
        skillMock = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test userId
        user.setUserId(1L);
        assertEquals(Long.valueOf(1L), user.getUserId());

        // Test firstname
        user.setFirstname("Employee1");
        assertEquals("Employee1", user.getFirstname());

        // Test lastname
        user.setLastname("Emp");
        assertEquals("Emp", user.getLastname());

        // Test email
        user.setEmail("emp@nucleusteq.com");
        assertEquals("emp@nucleusteq.com", user.getEmail());

        // Test password
        user.setPassword("password");
        assertEquals("password", user.getPassword());

        // Test role
        user.setRole(User.Role.EMPLOYEE);
        assertEquals(User.Role.EMPLOYEE, user.getRole());

        // Test managerProjects
        List<Project> projects = new ArrayList<>();
        projects.add(projectMock);
        user.setManagerProjects(projects);
        assertEquals(projects, user.getManagerProjects());

        // Test managerRequestResources
        List<RequestResource> managerRequests = new ArrayList<>();
        managerRequests.add(requestResourceMock);
        user.setManagerRequestResources(managerRequests);
        assertEquals(managerRequests, user.getManagerRequestResources());

        // Test employeeRequestResource
        List<RequestResource> employeeRequests = new ArrayList<>();
        employeeRequests.add(requestResourceMock);
        user.setEmployeeRequestResource(employeeRequests);
        assertEquals(employeeRequests, user.getEmployeeRequestResource());

        // Test skills
        List<Skill> skills = new ArrayList<>();
        skills.add(skillMock);
        user.setSkills(skills);
        assertEquals(skills, user.getSkills());
    }
}
