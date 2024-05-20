package com.emp.dto;

import java.util.List;

public class ProjectDetails {
    private Long projectId;
    private String projectName;
    private String managerName;
    private List<String> employeeNames;

    
    public ProjectDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProjectDetails(Long projectId, String projectName, String managerName, List<String> employeeNames) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.managerName = managerName;
        this.employeeNames = employeeNames;
    }

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public List<String> getEmployeeNames() {
		return employeeNames;
	}

	public void setEmployeeNames(List<String> employeeNames) {
		this.employeeNames = employeeNames;
	}

 
}

