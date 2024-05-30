package com.emp.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

import com.emp.dto.ProjectDetails;
import com.emp.dto.UserRequest;
import com.emp.entities.Assignment;
import com.emp.entities.EmployeeSkill;
import com.emp.entities.EmployeeSkill.EmployeeSkillId;
import com.emp.entities.Project;
import com.emp.entities.Skill;
import com.emp.entities.User;
import com.emp.repository.AssignmentRepository;
import com.emp.repository.EmployeeSkillRepository;
import com.emp.repository.SkillRepository;
import com.emp.repository.UserRepository;

public class EmployeeServiceTests {

	@Mock
    private UserRepository userRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private EmployeeSkillRepository employeeSkillRepository;

    @Mock
    private AssignmentRepository assignmentRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

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
        User user = new User();
        user.setEmail("emp1@nucleusteq.com");
        UserRequest userRequest = new UserRequest("Employee1", "Emp", "emp1@nucleusteq.com", "Password123", "EMPLOYEE");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedpassword");

        boolean result = employeeService.updateUserProfile(userId, userRequest);
        assertTrue(result);
        assertEquals("Employee1", user.getFirstname());
        assertEquals("Emp", user.getLastname());
        assertEquals("emp1@nucleusteq.com", user.getEmail());
        assertEquals("encodedpassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    public void testAddSkill() {
        Long employeeId = 1L;
        String skillName = "Java";
        User user = new User();
        Skill skill = new Skill();
        skill.setSkillId(1L);
        skill.setName(skillName);

        when(userRepository.findById(employeeId)).thenReturn(Optional.of(user));
        when(skillRepository.findByNameIgnoreCase(skillName)).thenReturn(Optional.of(skill));
        when(employeeSkillRepository.save(any(EmployeeSkill.class))).thenReturn(new EmployeeSkill());

        employeeService.addSkill(employeeId, skillName);
        verify(employeeSkillRepository).save(any(EmployeeSkill.class));
    }

    @Test
    public void testGetSkillsForEmployee() {
        Long employeeId = 1L;
        EmployeeSkill employeeSkill = new EmployeeSkill(new EmployeeSkillId(employeeId, 1L));
        Skill skill = new Skill();
        skill.setSkillId(1L);
        skill.setName("Java");

        when(employeeSkillRepository.findByIdEmployeeUserId(employeeId)).thenReturn(Arrays.asList(employeeSkill));
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        List<String> skills = employeeService.getSkillsForEmployee(employeeId);

        assertEquals(1, skills.size());
        assertEquals("Java", skills.get(0));
    }

    @Test
    public void testGetEmployeeProjectDetails_Success() {
        Long employeeId = 1L;
        User user = new User();
        user.setUserId(employeeId);
        user.setFirstname("Employee1");
        user.setLastname("Emp");
        
        Project project = new Project();
        project.setProjectId(1L);
        project.setName("New Project");
        
        User manager = new User();
        manager.setFirstname("Manager1");
        manager.setLastname("Mgr");
        project.setManager(manager);
        
        Assignment assignment = new Assignment();
        assignment.setEmployee(user);
        assignment.setProject(project);
        project.setProjectAssignments(Arrays.asList(assignment));
        
        when(userRepository.findById(employeeId)).thenReturn(Optional.of(user));
        when(assignmentRepository.findByEmployeeUserId(employeeId)).thenReturn(Arrays.asList(assignment));
        
        List<ProjectDetails> projectDetails = employeeService.getEmployeeProjectDetails(employeeId);
        
        assertEquals(1, projectDetails.size());
        ProjectDetails details = projectDetails.get(0);
        assertEquals(1L, details.getProjectId());
        assertEquals("New Project", details.getProjectName());
        assertEquals("Manager1 Mgr", details.getManagerName());
        assertEquals(1, details.getEmployeeNames().size());
        assertEquals("Employee1 Emp", details.getEmployeeNames().get(0));
    }

}
