package com.emp.controller;

import static org.hamcrest.CoreMatchers.is;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.emp.dto.ProjectDetails;
import com.emp.dto.UserRequest;
import com.emp.entities.User;
import com.emp.repository.UserRepository;
import com.emp.service.EmployeeService;

public class EmployeeControllerTests {
	  @Autowired
	  private MockMvc mockMvc;

	   @Mock
	    private EmployeeService employeeService;

	    @Mock
	    private UserRepository userRepository;

	    @InjectMocks
	    private EmployeeController employeeController;

	  private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @AfterEach 
    void tearDown() throws Exception {
        closeable.close();
    }
    
    @Test
    public void testUpdateUserProfile() throws Exception {
        when(employeeService.updateUserProfile(anyLong(), any(UserRequest.class))).thenReturn(true);

        mockMvc.perform(put("/api/employee/update-profile/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\":\"Employee1\",\"lastname\":\"emp\",\"email\":\"emp@nucleusteq.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User profile updated successfully"));
    }
    
    @Test
    public void testUpdateUserProfileFailure() throws Exception {
        when(employeeService.updateUserProfile(anyLong(), any())).thenReturn(false);

        mockMvc.perform(put("/api/employee/update-profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\":\"Employee1\",\"lastname\":\"emp\",\"email\":\"emp@nucleusteq.com\",\"password\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to update profile."));
    
       //Exception Handling
        when(employeeService.updateUserProfile(anyLong(), any())).thenThrow(new IllegalArgumentException("Invalid input"));
        mockMvc.perform(put("/api/employee/update-profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\":\"Employee1\",\"lastname\":\"emp\",\"email\":\"emp@nucleusteq.com\",\"password\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input"));
    }
    

    @Test
    public void testAddSkill() throws Exception {
        mockMvc.perform(post("/api/employee/add-skills/{employeeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"skillName\":\"Java\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddSkill_Empty() throws Exception {
        mockMvc.perform(post("/api/employee/add-skills/{employeeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"skillName\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Skill name cannot be empty"));
    }

    @Test
    public void testGetSkillsForEmployee() throws Exception {
        when(employeeService.getSkillsForEmployee(anyLong())).thenReturn(Arrays.asList("Java", "Spring"));

        mockMvc.perform(get("/api/employee/skills/{employeeId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0]").value("Java"))
                .andExpect(jsonPath("$[1]").value("Spring"));
    }

    @Test
    public void testGetEmployeeProjectDetails() throws Exception {
        List<ProjectDetails> projectDetails = new ArrayList<>();
        projectDetails.add(new ProjectDetails(1L, "Project 1", "Manager 1", Arrays.asList("Employee 1", "Employee 2")));
        projectDetails.add(new ProjectDetails(2L, "Project 2", "Manager 2", Arrays.asList("Employee 3", "Employee 4")));
        when(employeeService.getEmployeeProjectDetails(anyLong())).thenReturn(projectDetails);

        mockMvc.perform(get("/api/employee/projects/{employeeId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].projectId", is(1)))
                .andExpect(jsonPath("$[0].projectName", is("Project 1")))
                .andExpect(jsonPath("$[0].managerName", is("Manager 1")))
                .andExpect(jsonPath("$[0].employeeNames.length()", is(2)))
                .andExpect(jsonPath("$[0].employeeNames[0]", is("Employee 1")))
                .andExpect(jsonPath("$[0].employeeNames[1]", is("Employee 2")));
    }

    @Test
    public void testGetEmployees() throws Exception {
        List<User> employees = new ArrayList<>();
        employees.add(new User("Employee 1", "Lastname 1", "emp1@nucleusteq.com", "password", User.Role.EMPLOYEE));
        employees.add(new User("Employee 2", "Lastname 2", "emp2@nucleusteq.com", "password", User.Role.EMPLOYEE));
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employee/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].firstname", is("Employee 1")))
                .andExpect(jsonPath("$[1].firstname", is("Employee 2")));
    }
    @Test
    public void testGetEmployees_Failure() throws Exception {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(get("/api/employee/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: Internal Server Error"));
    }
    
 
    @Test
    public void testGetManagers() throws Exception {
        List<User> managers = new ArrayList<>();
        managers.add(new User("Manager 1", "Lastname 1", "emp1@nucleusteq.com", "password", User.Role.MANAGER));
        managers.add(new User("Manager 2", "Lastname 2", "emp2@nucleusteq.com", "password", User.Role.MANAGER));
        when(employeeService.getAllManagers()).thenReturn(managers);

        mockMvc.perform(get("/api/employee/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].firstname", is("Manager 1")))
                .andExpect(jsonPath("$[1].firstname", is("Manager 2")));
    }
    @Test
    public void testGetManagers_Failure() throws Exception {
        when(employeeService.getAllManagers()).thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(get("/api/employee/managers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: Internal Server Error"));
    }
}