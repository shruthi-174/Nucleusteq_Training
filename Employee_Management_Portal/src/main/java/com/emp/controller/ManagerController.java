package com.emp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emp.dto.RequestResourceRequest;
import com.emp.dto.UserRequest;
import com.emp.entities.Project;
import com.emp.entities.RequestResource;
import com.emp.entities.User;
import com.emp.repository.ProjectRepository;
import com.emp.repository.UserRepository;
import com.emp.service.ManagerService;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;
    
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/update-profile/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        try {
            boolean isUpdated = managerService.updateUserProfile(userId, userRequest);
            if (isUpdated) {
                return ResponseEntity.ok("User profile updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown error");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/employees/filter")
    public ResponseEntity<List<User>> filterEmployeesBySkills(@RequestParam("skills") List<String> skills) {
        List<User> filteredEmployees = managerService.filterEmployeesBySkills(skills);
        return ResponseEntity.ok(filteredEmployees);
    }
    
    
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/filter/unassigned-employees")
    public ResponseEntity<List<User>> getUnassignedEmployees() {
        List<User> unassignedEmployees = managerService.getUnassignedEmployees();
        return ResponseEntity.ok(unassignedEmployees);
    }
    
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/employees")
    public ResponseEntity<List<User>> getEmployees() {
        List<User> employees = userRepository.findByRole(User.Role.EMPLOYEE);
        return ResponseEntity.ok(employees);
    }
    
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/managers")
    public ResponseEntity<List<User>> getManagers() {
        List<User> managers = userRepository.findByRole(User.Role.MANAGER);
        return ResponseEntity.ok(managers);
    }
    
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }
    
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/request-resources")
    public ResponseEntity<List<RequestResource>> createRequestResources(@RequestBody RequestResourceRequest requestResourceRequest) {
        try {
            System.out.println("Received request: " + requestResourceRequest);
            List<RequestResource> requestResources = managerService.createRequestResources(requestResourceRequest);
            return ResponseEntity.ok(requestResources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}