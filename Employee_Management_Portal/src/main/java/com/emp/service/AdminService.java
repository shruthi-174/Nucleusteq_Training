package com.emp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emp.dto.ProjectDetails;
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

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private  RequestResourceRepository requestResourceRepository;

    
    public void registerUser(UserRequest userRequest) throws Exception {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new Exception("Email is already registered");
        }
 
        User user = new User();
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(User.Role.valueOf(userRequest.getRole().toUpperCase()));
        userRepository.save(user);
    }
    
    public Project createProject(ProjectRequest projectRequest) {
    	User manager = userRepository.findByUserIdAndRole(projectRequest.getManagerId(), User.Role.MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("This ID does not belong to a manager."));

        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());
        project.setManager(manager);

        return projectRepository.save(project);
    }
    public void assignProject(Long projectId, Long employeeId) {
        List<Assignment> existingAssignments = assignmentRepository.findByEmployeeUserId(employeeId);
        if (!existingAssignments.isEmpty()) {
            throw new IllegalArgumentException("This employee is already assigned to a project.");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found."));

        User employee = userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)
                .orElseThrow(() -> new IllegalArgumentException("This ID does not belong to an employee."));

        Assignment assignment = new Assignment();
        assignment.setProject(project);
        assignment.setEmployee(employee);

        assignmentRepository.save(assignment);
    }

    public void unassignProject(Long projectId, Long employeeId) {
        Assignment assignment = assignmentRepository.findByProjectProjectIdAndEmployeeUserId(projectId, employeeId)
                .orElse(null);

        if (assignment == null) {
            throw new IllegalArgumentException("Assignment not found.");
        }

        assignmentRepository.delete(assignment);
    }
    
    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects == null || projects.isEmpty()) {
            throw new IllegalStateException("Project not found.");
        }
        return projects;
    }
    
    
    public List<User> getAllEmployees() {
        List<User> employees = userRepository.findByRole(User.Role.EMPLOYEE);
        if (employees == null || employees.isEmpty()) {
            throw new IllegalStateException("Employees not found.");
        }
        return employees;
    }


    public List<User> getAllManagers() {
        List<User> managers = userRepository.findByRole(User.Role.MANAGER);
        if (managers == null || managers.isEmpty()) {
            throw new IllegalStateException("Manager not found.");
        }
        return managers;
    }
    
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            throw new IllegalStateException("User not found.");
        }
        return users;
    }
    
    public List<ProjectDetails> getAllProjectDetails() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(project -> {
            List<String> employeeNames = project.getProjectAssignments().stream()
                    .map(assignment -> assignment.getEmployee().getFirstname() + " " + assignment.getEmployee().getLastname())
                    .collect(Collectors.toList());
            return new ProjectDetails(project.getProjectId(), project.getName(),
                    project.getManager().getFirstname() + " " + project.getManager().getLastname(),
                    employeeNames);
        }).collect(Collectors.toList());
    }

    public void deleteEmployee(Long employeeId) {
        User employee = userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found."));
        userRepository.delete(employee);
    }

    public void updateEmployee(Long employeeId, UserRequest userRequest) {
        User employee = userRepository.findByUserIdAndRole(employeeId, User.Role.EMPLOYEE)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found."));

        employee.setFirstname(userRequest.getFirstname());
        employee.setLastname(userRequest.getLastname());
        employee.setEmail(userRequest.getEmail());

        userRepository.save(employee);
    }
    
    public List<RequestResource> getAllResourceRequests() {
        return requestResourceRepository.findAll();
    }
    public RequestResource approveResourceRequest(Long requestId) {
        RequestResource request = requestResourceRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus(RequestStatus.APPROVED);
        return requestResourceRepository.save(request);
    }

    public RequestResource rejectResourceRequest(Long requestId) {
        RequestResource request = requestResourceRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);
        return requestResourceRepository.save(request);
    }

}
