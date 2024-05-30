package com.emp.dto;

public class ProjectRequest {
    private Long projectId;
    private String name;
    private String description;
    private Long managerId;
    
    
	public ProjectRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProjectRequest(Long projectId, String name, String description, Long managerId) {
		super();
		this.projectId = projectId;
		this.name = name;
		this.description = description;
		this.managerId = managerId;
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
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
    
    
    
    
}