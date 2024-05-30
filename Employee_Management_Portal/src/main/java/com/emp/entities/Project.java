package com.emp.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "manager_user_id")
    @JsonBackReference(value = "project-manager")
    private User manager;

    @OneToMany(mappedBy = "project")
    private List<Assignment> projectAssignments;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = {"manager", "employee"})
    private List<RequestResource> projectRequestResources;

	public Project() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Project(Long projectId, String name, String description, User manager, List<Assignment> projectAssignments,
			List<RequestResource> projectRequestResources) {
		super();
		this.projectId = projectId;
		this.name = name;
		this.description = description;
		this.manager = manager;
		this.projectAssignments = projectAssignments;
		this.projectRequestResources = projectRequestResources;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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

	public List<Assignment> getProjectAssignments() {
		return projectAssignments;
	}

	public void setProjectAssignments(List<Assignment> projectAssignments) {
		this.projectAssignments = projectAssignments;
	}

	public List<RequestResource> getProjectRequestResources() {
		return projectRequestResources;
	}

	public void setProjectRequestResources(List<RequestResource> projectRequestResources) {
		this.projectRequestResources = projectRequestResources;
	}
	}
    