package com.emp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import com.emp.dto.ProjectRequest;
import com.emp.dto.UserRequest;
import com.emp.entities.Project;
import com.emp.entities.RequestResource;
import com.emp.entities.User;
import com.emp.service.AdminService;

public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @AfterEach 
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    void testRegisterUser() throws Exception {
        UserRequest userRequest = new UserRequest("Employee1", "Emp", "emp@nucleusteq.com", "password", "EMPLOYEE");
        doNothing().when(adminService).registerUser(any(UserRequest.class));

        mockMvc.perform(post("/api/admin/add-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }
    
    @Test
    void testAddProject() throws Exception {
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setName("New Project");
        projectRequest.setDescription("New Project Description");
        projectRequest.setManagerId(1L);

        Project project = new Project();
        project.setName("New Project");
        project.setDescription("New Project Description");

        when(adminService.createProject(any(ProjectRequest.class))).thenReturn(project);

        mockMvc.perform(post("/api/admin/createProject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(projectRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(adminService, times(1)).createProject(any(ProjectRequest.class));
    }
    
    @Test
    void testAssignProject() throws Exception {
        doNothing().when(adminService).assignProject(anyLong(), anyLong());

        mockMvc.perform(post("/api/admin/assignProject/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testUnassignProject() throws Exception {
        doNothing().when(adminService).unassignProject(anyLong(), anyLong());

        mockMvc.perform(post("/api/admin/unassignProject/1/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetProjects() throws Exception {
        Project project = new Project();
        project.setProjectId(1L);
        project.setName("New Project");
        when(adminService.getAllProjects()).thenReturn(Arrays.asList(project));

        mockMvc.perform(get("/api/admin/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("New Project"));
    }

    @Test
    void testGetEmployees() throws Exception {
        User employee = new User();
        employee.setUserId(1L);
        employee.setFirstname("Employee1");
        employee.setRole(User.Role.EMPLOYEE);
        when(adminService.getAllEmployees()).thenReturn(Arrays.asList(employee));

        mockMvc.perform(get("/api/admin/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("Employee1"));
    }

    @Test
    void testGetManagers() throws Exception {
        User manager = new User();
        manager.setUserId(1L);
        manager.setFirstname("Manager1");
        manager.setRole(User.Role.MANAGER);
        when(adminService.getAllManagers()).thenReturn(Arrays.asList(manager));

        mockMvc.perform(get("/api/admin/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("Manager1"));
    }

    @Test
    void testGetUsers() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setFirstname("Admin");
        when(adminService.getAllUsers()).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("Admin"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(adminService).deleteEmployee(anyLong());

        mockMvc.perform(delete("/api/admin/deleteEmployees/1"))
               .andDo(print());

    }

    @Test
    void testUpdateEmployee() throws Exception {
        UserRequest userRequest = new UserRequest("Employee1", "Emp", "emp@nucleusteq.com", "password", "EMPLOYEE");
        doNothing().when(adminService).updateEmployee(anyLong(), any(UserRequest.class));

        mockMvc.perform(put("/api/admin/updateEmployee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee details updated successfully"));
    }
    
    @Test
    void testGetAllResourceRequests() throws Exception {
        RequestResource requestResource = new RequestResource();
        requestResource.setRequestId(1L);
        requestResource.setStatus(RequestResource.RequestStatus.PENDING);
        when(adminService.getAllResourceRequests()).thenReturn(Arrays.asList(requestResource));

        mockMvc.perform(get("/api/admin/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void testApproveResourceRequest() throws Exception {
        RequestResource requestResource = new RequestResource();
        requestResource.setRequestId(1L);
        requestResource.setStatus(RequestResource.RequestStatus.APPROVED);
        when(adminService.approveResourceRequest(anyLong())).thenReturn(requestResource);

        mockMvc.perform(put("/api/admin/requests/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testRejectResourceRequest() throws Exception {
        RequestResource requestResource = new RequestResource();
        requestResource.setRequestId(1L);
        requestResource.setStatus(RequestResource.RequestStatus.REJECTED);
        when(adminService.rejectResourceRequest(anyLong())).thenReturn(requestResource);

        mockMvc.perform(put("/api/admin/requests/1/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }
}
