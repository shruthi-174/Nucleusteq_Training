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

	    // Getters and setters
	}
    