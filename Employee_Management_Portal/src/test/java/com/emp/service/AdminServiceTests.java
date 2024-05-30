package com.emp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.emp.dto.ProjectRequest;
import com.emp.dto.UserRequest;
import com.emp.entities.Assignment;
import com.emp.entities.Project;
import com.emp.entities.RequestResource;
import com.emp.entities.RequestResource.RequestStatus;
import com.emp.entities.User;
import com.emp.repository.AssignmentRepository;
import com.emp.repository.ProjectRepository;
import com.emp.repository.RequestResourceRepository;
import com.emp.repository.UserRepository;

public class AdminServiceTests {
	    @InjectMocks
	    private AdminService adminService;

	    @Mock
	    private UserRepository userRepository;

	    @Mock
	    private ProjectRepository projectRepository;

	    @Mock
	    private AssignmentRepository assignmentRepository;

	    @Mock
	    private RequestResourceRepository requestResourceRepository;

	    @Mock
	    private PasswordEncoder passwordEncoder;
	    
	    @Mock
	    private  UserRequest userRequest;
	    
	    @BeforeEach
	    public void setUp() {
	     MockitoAnnotations.openMocks(this);
	   	  userRequest = new UserRequest("Employee1", "Emp", "emp1@nucleusteq.com", "Password123", "EMPLOYEE");
	    }

	    @AfterEach
	    public void tearDown() {
	    }
	    
	    @Test
	    void testRegisterUser() throws Exception {
	         // Email already exists Exception
	         when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new User()));
	         Exception exception = assertThrows(Exception.class, () -> adminService.registerUser(userRequest));
	         assertEquals("Email is already registered", exception.getMessage());

	         // Successful Registration
	         when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
	         when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
	         adminService.registerUser(userRequest);
	         verify(userRepository, times(1)).save(Mockito.any());
	    }
	    
	    @Test
	    void testCreateProject() {
	        Long managerId = 1L;
	        ProjectRequest projectRequest = new ProjectRequest();
	        projectRequest.setName("New Project");
	        projectRequest.setDescription("New Project Description");
	        projectRequest.setManagerId(managerId);

	        // Manager not found
	        when(userRepository.findByUserIdAndRole(managerId, User.Role.MANAGER)).thenReturn(Optional.empty());
	        assertThrows(IllegalArgumentException.class, () -> adminService.createProject(projectRequest));

	        // Successful project creation
	        User manager = new User();
	        manager.setUserId(managerId);
	        manager.setRole(User.Role.MANAGER);
	        when(userRepository.findByUserIdAndRole(managerId, User.Role.MANAGER)).thenReturn(Optional.of(manager));

	        Project project = new Project();
	        project.setName("New Project");
	        project.setDescription("New Project Description");
	        project.setManager(manager);

	        when(projectRepository.save(any(Project.class))).thenReturn(project);

	        Project createdProject = adminService.createProject(projectRequest);

	        assertNotNull(createdProject);
	        assertEquals("New Project", createdProject.getName());
	        verify(projectRepository, times(1)).save(any(Project.class));
	    }
	    
	    
	    @Test
	    void testAssignProject() throws Exception{
	    	 Long projectId=1L;
	    	 Long employeeId = 2L;
   	
	    	 // Employee already assigned Exception
	    	 Assignment assignment = new Assignment();
	    	 when(assignmentRepository.findByEmployeeUserId(employeeId)).thenReturn(Arrays.asList(assignment));
	    	 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.assignProject(projectId, employeeId));
	    	 assertEquals("This employee is already assigned to a project.", exception.getMessage());

	    	 // Project not found Exception
	    	 when(assignmentRepository.findByEmployeeUserId(employeeId)).thenReturn(new ArrayList<>());
	    	 when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
	    	 exception = assertThrows(IllegalArgumentException.class, () -> adminService.assignProject(projectId, employeeId));
	    	 assertEquals("Project not found.", exception.getMessage());

	    	 // Employee not found Exception
	    	 Project project = new Project();
	    	 when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
	    	 when(userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)).thenReturn(Optional.empty());
	    	 exception = assertThrows(IllegalArgumentException.class, () -> adminService.assignProject(projectId, employeeId));
	    	 assertEquals("This ID does not belong to an employee.", exception.getMessage());

	    	 // Successful assignment
	    	 User employee = new User();
	    	 employee.setRole(User.Role.EMPLOYEE);
	    	 when(userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)).thenReturn(Optional.of(employee));
	    	 adminService.assignProject(projectId, employeeId);
	    	 verify(assignmentRepository, times(1)).save(any());	 
	    }
	    
	    @Test
	    void testUnassignProject() throws Exception{
	        Long projectId = 1L;
	        Long employeeId = 2L;

	        // Assignment not found exception
	        when(assignmentRepository.findByProjectProjectIdAndEmployeeUserId(projectId, employeeId)).thenReturn(Optional.empty());
	        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.unassignProject(projectId, employeeId));
	        assertEquals("Assignment not found.", exception.getMessage());

	        // Successful unassignment
	        Assignment assignment = new Assignment();
	        when(assignmentRepository.findByProjectProjectIdAndEmployeeUserId(projectId, employeeId)).thenReturn(Optional.of(assignment));
	        adminService.unassignProject(projectId, employeeId);
	        verify(assignmentRepository, times(1)).delete(assignment);
	    }
	    
	    @Test
	    void testGetAllProjects() {
	        List<Project> projects = Arrays.asList(new Project(), new Project());
	        when(projectRepository.findAll()).thenReturn(projects);

	        List<Project> Projects = adminService.getAllProjects();
	        assertEquals(2, Projects.size());
	        verify(projectRepository, times(1)).findAll();
	    }

	    @Test
	    void testGetAllEmployees() {
	        List<User> employees = Arrays.asList(new User(), new User());
	        when(userRepository.findByRole(User.Role.EMPLOYEE)).thenReturn(employees);

	        List<User> Employees = adminService.getAllEmployees();
	        assertEquals(2, Employees.size());
	        verify(userRepository, times(1)).findByRole(User.Role.EMPLOYEE);
	    }

	    @Test
	    void testGetAllManagers() {
	        List<User> managers = Arrays.asList(new User(), new User());
	        when(userRepository.findByRole(User.Role.MANAGER)).thenReturn(managers);

	        List<User> Managers = adminService.getAllManagers();
	        assertEquals(2, Managers.size());
	        verify(userRepository, times(1)).findByRole(User.Role.MANAGER);
	    }

	    @Test
	    void testGetAllUsers() {
	        List<User> users = Arrays.asList(new User(), new User());
	        when(userRepository.findAll()).thenReturn(users);

	        List<User> Users = adminService.getAllUsers();
	        assertEquals(2, Users.size());
	        verify(userRepository, times(1)).findAll();
	    }
	    
	    @Test
	    void testDeleteEmployee() throws Exception{
	    	Long employeeId = 2L;

	        // Employee not found Exception
	        when(userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)).thenReturn(Optional.empty());
	        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.deleteEmployee(employeeId));
	        assertEquals("Employee not found.", exception.getMessage());

	        // Successful deletion
	        User employee = new User();
	        employee.setUserId(employeeId);
	        employee.setRole(User.Role.EMPLOYEE);
	        when(userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)).thenReturn(Optional.of(employee));
	        
	        adminService.deleteEmployee(employeeId);
	        verify(userRepository, times(1)).delete(employee);
	    } 
	   
	    @Test
	    void testUpdateEmployee1() throws Exception{
	    	 Long employeeId = 2L;

	         // Employee not found Exception
	         when(userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)).thenReturn(Optional.empty());
	         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.updateEmployee(employeeId, userRequest));
	         assertEquals("Employee not found.", exception.getMessage());

	         // Successful update
	         User employee = new User();
	         employee.setUserId(employeeId);
	         employee.setRole(User.Role.EMPLOYEE);
	         when(userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)).thenReturn(Optional.of(employee));
	         
	         adminService.updateEmployee(employeeId, userRequest);
	         verify(userRepository, times(1)).save(employee);
	 	    }
	    @Test
	    void testApproveResourceRequests() throws Exception{
	    	Long requestId = 1L;
	    	
	    	 // Request not found Exception
	    	  when(requestResourceRepository.findById(requestId)).thenReturn(Optional.empty());
	    	    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.approveResourceRequest(requestId));
	    	    assertEquals("Request not found", exception.getMessage());
	    	
	    	    // Successful approval
	    	    RequestResource request = new RequestResource();
	    	    when(requestResourceRepository.findById(requestId)).thenReturn(Optional.of(request));
	    	    when(requestResourceRepository.save(any(RequestResource.class))).thenReturn(request);
	    	    
	    	    RequestResource approvedRequest = adminService.approveResourceRequest(requestId);
	    	    assertEquals(RequestStatus.APPROVED, approvedRequest.getStatus());
	    	    verify(requestResourceRepository, times(1)).save(request);
	    }
	    @Test
	    void testRejectResourceRequests() throws Exception{
	    	Long requestId = 1L;

	        // Request not found Exception
	        when(requestResourceRepository.findById(requestId)).thenReturn(Optional.empty());
	        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> adminService.rejectResourceRequest(requestId));
	        assertEquals("Request not found", exception.getMessage());

	        // Successful rejection
	        RequestResource request = new RequestResource();
	        when(requestResourceRepository.findById(requestId)).thenReturn(Optional.of(request));
	        when(requestResourceRepository.save(any(RequestResource.class))).thenReturn(request);

	        RequestResource rejectedRequest = adminService.rejectResourceRequest(requestId);
	        assertEquals(RequestStatus.REJECTED, rejectedRequest.getStatus());
	        verify(requestResourceRepository, times(1)).save(request);
	    	
	    }
	    
	    
	    @Test
	    void getAllResourceRequests() {
	    	   List<RequestResource> requests = Arrays.asList(new RequestResource(), new RequestResource());
	    	    when(requestResourceRepository.findAll()).thenReturn(requests);

	    	    List<RequestResource> result = adminService.getAllResourceRequests();
	    	    assertEquals(2, result.size());
	    	    verify(requestResourceRepository, times(1)).findAll();
	    }
	    
	    
	    
	    }
	   

