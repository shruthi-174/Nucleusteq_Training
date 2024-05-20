package com.emp.entities;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long assignmentId;

    @OneToOne
    @JoinColumn(name = "employee_user_id")
    @JsonBackReference(value = "assignment-employee")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "assignment-project")
    private Project project;

	public Assignment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Assignment(Long assignmentId, User employee, Project project) {
		super();
		this.assignmentId = assignmentId;
		this.employee = employee;
		this.project = project;
	}

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
    
    
	
}