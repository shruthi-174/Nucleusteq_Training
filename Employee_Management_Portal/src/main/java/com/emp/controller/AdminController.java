package com.emp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.emp.entities.RequestResource;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-user")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        try {
            adminService.registerUser(userRequest);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
    @PostMapping("/assignProject/{projectId}/{employeeId}")
    public ResponseEntity<?> assignProject(@PathVariable Long projectId, @PathVariable Long employeeId) {
        try {
            adminService.assignProject(projectId, employeeId);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/unassignProject/{projectId}/{employeeId}")
    public ResponseEntity<?> unassignProject(@PathVariable Long projectId, @PathVariable Long employeeId) {
        try {
            adminService.unassignProject(projectId, employeeId);
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
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteEmployees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {  
            Optional<User> userOptional = userRepository.findById(id);
            if (!userOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            if (!assignmentRepository.findByEmployeeUserId(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot delete user with active assignments");
            }

            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateEmployee/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long employeeId, @RequestBody UserRequest userRequest) {
        try {
            adminService.updateEmployee(employeeId, userRequest);
            return ResponseEntity.ok("Employee details updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/requests")
    public ResponseEntity<List<RequestResource>> getAllResourceRequests() {
        List<RequestResource> requests = adminService.getAllResourceRequests();
        return ResponseEntity.ok(requests);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/requests/{requestId}/approve")
    public ResponseEntity<RequestResource> approveResourceRequest(@PathVariable Long requestId) {
        try {
            RequestResource approvedRequest = adminService.approveResourceRequest(requestId);
            return ResponseEntity.ok(approvedRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/requests/{requestId}/reject")
    public ResponseEntity<RequestResource> rejectResourceRequest(@PathVariable Long requestId) {
        try {
            RequestResource rejectedRequest = adminService.rejectResourceRequest(requestId);
            return ResponseEntity.ok(rejectedRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}


