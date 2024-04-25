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
@Table(name= "request_resources")
public class RequestResource {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long request_id;
	
    @ManyToMany
    @JoinTable(	name= "manager_requests",
    joinColumns = @JoinColumn (name="request_id"),
    inverseJoinColumns= @JoinColumn(name="manager_id"))
	private List <User> managers;
    
    @ManyToMany
    @JoinTable(	name= "project_requests",
    joinColumns = @JoinColumn (name="request_id"),
    inverseJoinColumns= @JoinColumn(name="project_id"))
	private List <Project> projects;
    
    @ManyToMany
    @JoinTable(	name= "skill_requests",
    joinColumns = @JoinColumn (name="request_id"),
    inverseJoinColumns= @JoinColumn(name="skill_id"))
	private List <EmployeeSkill> skills;

	public RequestResource() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestResource(Long request_id, List<User> managers, List<Project> projects, List<EmployeeSkill> skills) {
		super();
		this.request_id = request_id;
		this.managers = managers;
		this.projects = projects;
		this.skills = skills;
	}

	public Long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

	public List<User> getManagers() {
		return managers;
	}

	public void setManagers(List<User> managers) {
		this.managers = managers;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<EmployeeSkill> getSkills() {
		return skills;
	}

	public void setSkills(List<EmployeeSkill> skills) {
		this.skills = skills;
	}
    
    
    
}