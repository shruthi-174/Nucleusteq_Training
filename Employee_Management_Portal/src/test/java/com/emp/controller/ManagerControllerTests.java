package com.emp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;

import com.emp.dto.RequestResourceRequest;
import com.emp.dto.UserRequest;
import com.emp.entities.Project;
import com.emp.entities.RequestResource;
import com.emp.entities.User;
import com.emp.repository.ProjectRepository;
import com.emp.repository.UserRepository;
import com.emp.service.ManagerService;

public class ManagerControllerTests {
	private MockMvc mockMvc;

    @Mock
    private ManagerService managerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ManagerController managerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }

    @AfterEach
    public void tearDown() {
        // Clean up resources if needed
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        UserRequest userRequest = new UserRequest("Employee", "emp", "emp@nucleusteq.com", "password123", "EMPLOYEE");
        when(managerService.updateUserProfile(anyLong(), any(UserRequest.class))).thenReturn(true);

        mockMvc.perform(put("/api/manager/update-profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\":\"Employee1\",\"lastname\":\"emp\",\"email\":\"emp@nucleusteq.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User profile updated successfully"));
    }

    
    
    @Test
    public void testFilterEmployeesBySkills() throws Exception {
        List<User> users = Arrays.asList(new User("Employee", "emp", "emp@nucleusteq.com", "password123", User.Role.EMPLOYEE));
        when(managerService.filterEmployeesBySkills(any())).thenReturn(users);

        mockMvc.perform(get("/api/manager/employees/filter")
                .param("skills", "Java,Spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));
    }

    @Test
    public void testGetUnassignedEmployees() throws Exception {
        List<User> users = Arrays.asList(new User("Employee", "emp", "emp@nucleusteq.com", "password123", User.Role.EMPLOYEE));
        when(managerService.getUnassignedEmployees()).thenReturn(users);

        mockMvc.perform(get("/api/manager/filter/unassigned-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));
    }

    @Test
    public void testGetEmployees() throws Exception {
        List<User> users = Arrays.asList(new User("Employee", "emp", "emp@nucleusteq.com", "password123", User.Role.EMPLOYEE));
        when(managerService.getAllEmployees()).thenReturn(users);

        mockMvc.perform(get("/api/manager/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));
    }
    @Test
    public void testGetManagers() throws Exception {
        List<User> users = Arrays.asList(new User("Manager", "Mgr", "mgr@nucleusteq.com", "password123", User.Role.MANAGER));
        when(managerService.getAllManagers()).thenReturn(users);

        mockMvc.perform(get("/api/manager/managers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));
    }

    @Test 
    public void testGetProjects() throws Exception {
        List<Project> projects = Arrays.asList(new Project(1L, "New Project", "Description of new Project", null, null, null));
        when(managerService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/api/manager/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(projects.size()));
    }

    @Test
    public void testCreateRequestResources() throws Exception {
        List<RequestResource> resources = Collections.singletonList(new RequestResource());

        when(managerService.createRequestResources(any(RequestResourceRequest.class))).thenReturn(resources);
        mockMvc.perform(post("/api/manager/request-resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"emails\":[\"emp@nucleusteq.com\"],\"managerId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(resources.size()));
    }
}
