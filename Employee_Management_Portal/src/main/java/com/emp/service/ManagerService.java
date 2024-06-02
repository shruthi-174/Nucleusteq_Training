package com.emp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emp.dto.RequestResourceRequest;
import com.emp.dto.UserRequest;
import com.emp.entities.Assignment;
import com.emp.entities.EmployeeSkill;
import com.emp.entities.Project;
import com.emp.entities.RequestResource;
import com.emp.entities.RequestResource.RequestStatus;
import com.emp.entities.Skill;
import com.emp.entities.User;
import com.emp.repository.AssignmentRepository;
import com.emp.repository.EmployeeSkillRepository;
import com.emp.repository.ProjectRepository;
import com.emp.repository.RequestResourceRepository;
import com.emp.repository.SkillRepository;
import com.emp.repository.UserRepository;

@Service
public class ManagerService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;
    
    @Autowired
    private  ProjectRepository projectRepository;
    
    @Autowired
    private RequestResourceRepository requestResourceRepository;
    

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public boolean updateUserProfile(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with id: " + userId));

        if (!user.getEmail().equals(userRequest.getEmail()) && userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setEmail(userRequest.getEmail());
        if (userRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        userRepository.save(user);
        return true;
    }
    
    public List<User> filterEmployeesBySkills(List<String> skillNames) {
        Set<Skill> skill_list = new HashSet<>();
        for (String skillName : skillNames) {
            Skill skill = skillRepository.findByNameIgnoreCase(skillName)
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found: " + skillName));
            skill_list.add(skill);
        }

        List<Long> employeeIds = employeeSkillRepository.findAll().stream()
                .filter(es -> skill_list.contains(skillRepository.findById(es.getId().getSkillId()).orElse(null)))
                .map(EmployeeSkill::getId)
                .map(EmployeeSkill.EmployeeSkillId::getEmployeeUserId)
                .distinct()
                .collect(Collectors.toList());

        return userRepository.findByRoleAndUserIdIn(User.Role.EMPLOYEE, employeeIds);
    }

    public List<User> getUnassignedEmployees() {
        List<Long> assignedEmployeeIds = assignmentRepository.findAll().stream()
                .map(Assignment::getEmployee)
                .map(User::getUserId)
                .collect(Collectors.toList());

        return userRepository.findByRoleAndUserIdNotIn(User.Role.EMPLOYEE, assignedEmployeeIds);
    }
    
    public List<RequestResource> createRequestResources(RequestResourceRequest requestResourceRequest) {
        Project project = projectRepository.findById(requestResourceRequest.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User manager = userRepository.findById(requestResourceRequest.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        List<User> employees = new ArrayList<>();
        for (String employeeEmail : requestResourceRequest.getEmployeeEmails()) {
            Optional<User> optionalUser = userRepository.findByEmail(employeeEmail.trim());
            if (optionalUser.isPresent() && optionalUser.get().getRole() == User.Role.EMPLOYEE) {
                employees.add(optionalUser.get());
            }
        }

        if (employees.isEmpty()) {
            throw new IllegalArgumentException("No valid employees found");
        }

        List<RequestResource> requestResourcesList = new ArrayList<>();
        for (User employee : employees) {
            RequestResource requestResource = new RequestResource();
            requestResource.setManager(manager);
            requestResource.setProject(project);
            requestResource.setEmployee(employee);
            requestResource.setStatus(RequestStatus.PENDING);
            requestResourcesList.add(requestResourceRepository.save(requestResource));
        }

        return requestResourcesList;
    }
    
    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects == null || projects.isEmpty()) {
            throw new IllegalStateException("Project not found.");
        }
        return projects;
    }
    
    public List<Project> getProjectsByManagerId(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with id: " + managerId));

        if (manager.getRole() != User.Role.MANAGER) {
            throw new IllegalArgumentException("User with id " + managerId + " is not a manager");
        }

        List<Project> projects = projectRepository.findAll().stream()
                .filter(project -> project.getManager().getUserId().equals(managerId))
                .collect(Collectors.toList());

        if (projects.isEmpty()) {
            throw new IllegalStateException("Projects not found for manager with id: " + managerId);
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

    public List<EmployeeSkill> getAllEmployeeSkills() {
        return employeeSkillRepository.findAll();
    }
}
