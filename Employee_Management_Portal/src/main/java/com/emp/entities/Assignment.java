package com.emp.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name= "assignments")
public class Assignment {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long assignment_id;
	
    @ManyToMany
    @JoinTable(	name= "project_assignments",
    joinColumns = @JoinColumn (name="assignment_id"),
    inverseJoinColumns= @JoinColumn(name="project_id"))
	private List <Project> projects;
    
    @ManyToMany
    @JoinTable(	name= "employee_assignments",
    joinColumns = @JoinColumn (name="assignment_id"),
    inverseJoinColumns= @JoinColumn(name="employee_id"))
	private List <User> employees;

	public Assignment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Assignment(Long assignment_id, List<Project> projects, List<User> employees) {
		super();
		this.assignment_id = assignment_id;
		this.projects = projects;
		this.employees = employees;
	}

	public Long getAssignment_id() {
		return assignment_id;
	}

	public void setAssignment_id(Long assignment_id) {
		this.assignment_id = assignment_id;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<User> getEmployees() {
		return employees;
	}

	public void setEmployees(List<User> employees) {
		this.employees = employees;
	}
    
    
}