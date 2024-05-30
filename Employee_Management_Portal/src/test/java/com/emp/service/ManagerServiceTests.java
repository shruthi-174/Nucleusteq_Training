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
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.emp.dto.RequestResourceRequest;
import com.emp.dto.UserRequest;
import com.emp.entities.Project;
import com.emp.entities.RequestResource;
import com.emp.entities.Skill;
import com.emp.entities.User;
import com.emp.repository.AssignmentRepository;
import com.emp.repository.EmployeeSkillRepository;
import com.emp.repository.ProjectRepository;
import com.emp.repository.RequestResourceRepository;
import com.emp.repository.SkillRepository;
import com.emp.repository.UserRepository;

public class ManagerServiceTests {
	@Mock
    private UserRepository userRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private EmployeeSkillRepository employeeSkillRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private RequestResourceRepository requestResourceRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ManagerService managerService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testUpdateUserProfile() {
        Long userId = 1L;
        UserRequest userRequest = new UserRequest("Employee1", "Emp", "emp1@nucleusteq.com", "Password123", "EMPLOYEE");
        User user = new User();
        user.setEmail("emp1@nucleusteq.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");

        boolean result = managerService.updateUserProfile(userId, userRequest);

        assertTrue(result);
        assertEquals("Employee1", user.getFirstname());
        assertEquals("Emp", user.getLastname());
        assertEquals("emp1@nucleusteq.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    public void testFilterEmployeesBySkills() {
        List<String> skillNames = Arrays.asList("Java", "Spring");

        Skill javaSkill = new Skill();
        javaSkill.setName("Java");
        Skill springSkill = new Skill();
        springSkill.setName("Spring");

        when(skillRepository.findByNameIgnoreCase("Java")).thenReturn(Optional.of(javaSkill));
        when(skillRepository.findByNameIgnoreCase("Spring")).thenReturn(Optional.of(springSkill));
        when(employeeSkillRepository.findAll()).thenReturn(new ArrayList<>());

        List<User> users = managerService.filterEmployeesBySkills(skillNames);

        assertTrue(users.isEmpty());
        verify(skillRepository).findByNameIgnoreCase("Java");
        verify(skillRepository).findByNameIgnoreCase("Spring");
    }

    @Test
    public void testGetUnassignedEmployees() {
        when(assignmentRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.findByRoleAndUserIdNotIn(eq(User.Role.EMPLOYEE), anyList())).thenReturn(new ArrayList<>());

        List<User> users = managerService.getUnassignedEmployees();

        assertTrue(users.isEmpty());
        verify(assignmentRepository).findAll();
        verify(userRepository).findByRoleAndUserIdNotIn(eq(User.Role.EMPLOYEE), anyList());
    }

    @Test
    public void testCreateRequestResources() {
        Long projectId = 1L;
        Long managerId = 1L;
        List<String> employeeEmails = Arrays.asList("emp1@nucleusteq.com", "emp2@nucleusteq.com");

        RequestResourceRequest request = new RequestResourceRequest();
        request.setProjectId(projectId);
        request.setManagerUserId(managerId);
        request.setEmployeeEmails(employeeEmails);

        Project project = new Project();
        User manager = new User();
        User employee1 = new User();
        employee1.setEmail("employee1@example.com");
        employee1.setRole(User.Role.EMPLOYEE);
        User employee2 = new User();
        employee2.setEmail("employee2@example.com");
        employee2.setRole(User.Role.EMPLOYEE);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(userRepository.findByEmail("emp1@nucleusteq.com")).thenReturn(Optional.of(employee1));
        when(userRepository.findByEmail("emp2@nucleusteq.com")).thenReturn(Optional.of(employee2));
        when(requestResourceRepository.save(any(RequestResource.class))).thenReturn(new RequestResource());

        List<RequestResource> resources = managerService.createRequestResources(request);

        assertFalse(resources.isEmpty());
        verify(projectRepository).findById(projectId);
        verify(userRepository).findById(managerId);
        verify(userRepository).findByEmail("emp1@nucleusteq.com");
        verify(userRepository).findByEmail("emp2@nucleusteq.com");
        verify(requestResourceRepository, times(2)).save(any(RequestResource.class));
    }
}
