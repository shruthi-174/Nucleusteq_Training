package com.emp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.emp.dto.ProjectDetails;
import com.emp.dto.UserRequest;
import com.emp.entities.Assignment;
import com.emp.entities.EmployeeSkill;
import com.emp.entities.Project;
import com.emp.entities.Skill;
import com.emp.entities.User;
import com.emp.repository.AssignmentRepository;
import com.emp.repository.EmployeeSkillRepository;
import com.emp.repository.SkillRepository;
import com.emp.repository.UserRepository;

@Service
public class EmployeeService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private EmployeeSkillRepository employeeSkillRepository;

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean updateUserProfile(Long userId, UserRequest userRequest) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
		if (!user.getEmail().equals(userRequest.getEmail())
				&& userRepository.findByEmail(userRequest.getEmail()) != null) {
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

	public void addSkill(Long employeeId, String skillName) {
		User employee = userRepository.findById(employeeId)
				.orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));

		String cleanedSkillName = skillName.replaceAll("\\s+", " ").trim();

		Skill skill = skillRepository.findByNameIgnoreCase(cleanedSkillName)
				.orElseGet(() -> {
					Skill newSkill = new Skill();
					newSkill.setName(cleanedSkillName);
					return skillRepository.save(newSkill);
				});

		EmployeeSkill employeeSkill = new EmployeeSkill(
				new EmployeeSkill.EmployeeSkillId(employeeId, skill.getSkillId()));
		employeeSkillRepository.save(employeeSkill);
	}

	public List<String> getSkillsForEmployee(Long employeeId) {
		return employeeSkillRepository.findByIdEmployeeUserId(employeeId).stream()
				.map(es -> skillRepository.findById(es.getId().getSkillId()))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(Skill::getCleanedName)
				.collect(Collectors.toList());
	}

	public List<ProjectDetails> getEmployeeProjectDetails(Long employeeId) {
		User employee = userRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));
		List<Assignment> assignments = assignmentRepository.findByEmployeeUserId(employee.getUserId());

		List<ProjectDetails> projectDetails = new ArrayList<>();

		for (Assignment assignment : assignments) {
			Project project = assignment.getProject();
			List<String> employeeNames = project.getProjectAssignments().stream()
					.map(a -> a.getEmployee().getFirstname() + " " + a.getEmployee().getLastname())
					.collect(Collectors.toList());

			ProjectDetails projectDetail = new ProjectDetails(
					project.getProjectId(),
					project.getName(),
					project.getManager().getFirstname() + " " + project.getManager().getLastname(),
					employeeNames);

			projectDetails.add(projectDetail);
		}

		return projectDetails;
	}
}
