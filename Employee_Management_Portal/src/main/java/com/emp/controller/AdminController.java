package com.emp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.dto.ProjectDetails;
import com.emp.dto.UserRequest;
import com.emp.entities.Project;
import com.emp.entities.User;
import com.emp.repository.AssignmentRepository;
import com.emp.repository.ProjectRepository;
import com.emp.repository.UserRepository;
import com.emp.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        try {
            boolean registered = adminService.registerUser(userRequest);
            if (registered) {
                return ResponseEntity.ok("User registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createProject")
    public ResponseEntity<?> addProject(@RequestBody Project project) {
        try {
            if (project.getManager() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Manager cannot be null");
            }

            Long managerId = project.getManager().getUserId();
            User manager = userRepository.findByUserIdAndRole(managerId, User.Role.MANAGER)
                    .orElseThrow(() -> new IllegalArgumentException("This ID does not belong to a manager."));

            project.setManager(manager);
            projectRepository.save(project);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/employees")
    public ResponseEntity<List<User>> getEmployees() {
        List<User> employees = userRepository.findByRole(User.Role.EMPLOYEE);
        return ResponseEntity.ok(employees);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/managers")
    public ResponseEntity<List<User>> getManagers() {
        List<User> managers = userRepository.findByRole(User.Role.MANAGER);
        return ResponseEntity.ok(managers);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projectDetails")
    public ResponseEntity<List<ProjectDetails>> getProjectDetails() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDetails> projectDetailsList = projects.stream().map(project -> {
            List<String> employeeNames = project.getProjectAssignments().stream()
                    .map(assignment -> assignment.getEmployee().getFirstname() + " " + assignment.getEmployee().getLastname())
                    .collect(Collectors.toList());
            return new ProjectDetails(project.getProjectId(), project.getName(),
                    project.getManager().getFirstname() + " " + project.getManager().getLastname(),
                    employeeNames);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(projectDetailsList);
    }

}


