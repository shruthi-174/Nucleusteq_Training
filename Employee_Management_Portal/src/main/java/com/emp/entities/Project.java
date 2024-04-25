package com.emp.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name= "projects")
public class Project {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long project_id;
	
	private String name;
	private String description;
	
    @ManyToOne
    @JoinColumn(name = "manager_id")
	private User manager;

	public Project() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Project(Long project_id, String name, String description, User manager) {
		super();
		this.project_id = project_id;
		this.name = name;
		this.description = description;
		this.manager = manager;
	}

	public Long getProject_id() {
		return project_id;
	}

	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}
    
    
}