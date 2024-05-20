package com.emp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emp.dto.UserRequest;
import com.emp.entities.Assignment;
import com.emp.entities.Project;
import com.emp.entities.User;
import com.emp.repository.AssignmentRepository;
import com.emp.repository.ProjectRepository;
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

    public boolean registerUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(User.Role.valueOf(userRequest.getRole()));

        userRepository.save(user);

        return true;
    }

    public boolean createProject(String name, String description, Long managerId) {
        User manager = userRepository.findByUserIdAndRole(managerId, User.Role.MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("This ID does not belong to a manager."));

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setManager(manager);

        projectRepository.save(project);
        return true;
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
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found."));

        assignmentRepository.delete(assignment);
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



}
