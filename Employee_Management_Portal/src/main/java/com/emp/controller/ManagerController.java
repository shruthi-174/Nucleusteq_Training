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
import com.emp.service.ManagerService;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    
    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/update-profile/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        try {
            boolean isUpdated = managerService.updateUserProfile(userId, userRequest);
            if (isUpdated) {
                return ResponseEntity.ok("User profile updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update profile.");
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
    @GetMapping("/projects")
    public ResponseEntity<?> getProjects() {
    	try {
            List<Project> projects = managerService.getAllProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/projects/{managerId}")
    public ResponseEntity<?> getProjectsByManagerId(@PathVariable Long managerId) {
        try {
            List<Project> projects = managerService.getProjectsByManagerId(managerId);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/employees")
    public ResponseEntity<?> getEmployees() {
    	try {
            List<User> employees = managerService.getAllEmployees();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
   }
    
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/managers")
    public ResponseEntity<?> getManagers() {
    	try {
            List<User> managers = managerService.getAllManagers();
            return ResponseEntity.ok(managers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    	}

    
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/request-resources")
    public ResponseEntity<?> createRequestResources(@RequestBody RequestResourceRequest requestResourceRequest) {
        try {
            List<RequestResource> requestResources = managerService.createRequestResources(requestResourceRequest);
            return ResponseEntity.ok(requestResources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid Request");
        }
    }

}