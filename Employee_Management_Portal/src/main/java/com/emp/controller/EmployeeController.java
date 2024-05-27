package com.emp.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.emp.dto.ProjectDetails;
import com.emp.dto.UserRequest;
import com.emp.entities.User;
import com.emp.repository.UserRepository;
import com.emp.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("/update-profile/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        try {
            boolean isUpdated = employeeService.updateUserProfile(userId, userRequest);
            if (isUpdated) {
                return ResponseEntity.ok("User profile updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown error");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/add-skills/{employeeId}")
    public ResponseEntity<?> addSkill(@PathVariable Long employeeId, @RequestBody Map<String, String> requestBody) {
        String skillName = requestBody.get("skillName");
        if (skillName == null || skillName.isEmpty()) {
            return ResponseEntity.badRequest().body("Skill name cannot be empty");
        }
        
        employeeService.addSkill(employeeId, skillName);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/skills/{employeeId}")
    public ResponseEntity<List<String>> getSkillsForEmployee(@PathVariable Long employeeId) {
        List<String> skills = employeeService.getSkillsForEmployee(employeeId);
        return ResponseEntity.ok(skills);
    }
    
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/projects/{employeeId}")
    public ResponseEntity<List<ProjectDetails>> getEmployeeProjectDetails(@PathVariable Long employeeId) {
        List<ProjectDetails> projectDetails = employeeService.getEmployeeProjectDetails(employeeId);
        return ResponseEntity.ok(projectDetails);
    }
 
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employees")
    public ResponseEntity<List<User>> getEmployees() {
        List<User> employees = userRepository.findByRole(User.Role.EMPLOYEE);
        return ResponseEntity.ok(employees);
    }
    
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/managers")
    public ResponseEntity<List<User>> getManagers() {
        List<User> managers = userRepository.findByRole(User.Role.MANAGER);
        return ResponseEntity.ok(managers);
    }

}