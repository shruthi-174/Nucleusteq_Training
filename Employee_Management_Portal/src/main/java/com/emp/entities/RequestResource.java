package com.emp.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "request_resources")
public class RequestResource {

	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long requestId;

	    @ManyToOne
	    @JoinColumn(name = "manager_user_id")
	    @JsonIgnoreProperties(value = {"managerRequestResources", "employeeRequestResources"})
	    private User manager;

	    @ManyToOne
	    @JoinColumn(name = "project_id")
	    @JsonIgnoreProperties(value = {"projectAssignments", "projectRequestResources"})
	    private Project project;

	    @ManyToOne
	    @JoinColumn(name = "employee_user_id")
	    @JsonIgnoreProperties(value = {"managerRequestResources", "employeeRequestResources"})
	    private User employee;

	 @Enumerated(EnumType.STRING)
	 private RequestStatus status;
	 
	    public RequestResource() {
		super();
		// TODO Auto-generated constructor stub
	}



		public RequestResource(Long requestId, User manager, Project project, User employee, RequestStatus status) {
			super();
			this.requestId = requestId;
			this.manager = manager;
			this.project = project;
			this.employee = employee;
			this.status = status;
		}



		public Long getRequestId() {
			return requestId;
		}



		public void setRequestId(Long requestId) {
			this.requestId = requestId;
		}



		public User getManager() {
			return manager;
		}



		public void setManager(User manager) {
			this.manager = manager;
		}



		public Project getProject() {
			return project;
		}



		public void setProject(Project project) {
			this.project = project;
		}



		public User getEmployee() {
			return employee;
		}



		public void setEmployee(User employee) {
			this.employee = employee;
		}



		public RequestStatus getStatus() {
			return status;
		}



		public void setStatus(RequestStatus status) {
			this.status = status;
		}



		public enum RequestStatus {
	        PENDING,
	        APPROVED,
	        REJECTED
	    }
}