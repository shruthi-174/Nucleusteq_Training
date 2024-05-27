package com.emp.dto;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RequestResourceRequest {
	 private Long projectId;
	 private List<String> employeeEmails;
	 private Long managerId;
	 
	public RequestResourceRequest() {
		super();
		// TODO Auto-generated constructor stub
	}


	public RequestResourceRequest(Long projectId, List<String> employeeEmails, Long managerUserId) {
		super();
		this.projectId = projectId;
		this.employeeEmails = employeeEmails;
		this.managerId = managerUserId;
	}


	public Long getProjectId() {
		return projectId;
	}


	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}


	public List<String> getEmployeeEmails() {
		return employeeEmails;
	}


	public void setEmployeeEmails(List<String> employeeEmails) {
		this.employeeEmails = employeeEmails;
	}


	public Long getManagerId() {
		return managerId;
	}


	public void setManagerUserId(Long managerId) {
		this.managerId = managerId;
	}    
	
}
