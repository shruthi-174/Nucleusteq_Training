package com.emp.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	    private User manager;

		public Project() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Project(Long projectId, String name, String description, User manager) {
			super();
			this.projectId = projectId;
			this.name = name;
			this.description = description;
			this.manager = manager;
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

	    
	}
    