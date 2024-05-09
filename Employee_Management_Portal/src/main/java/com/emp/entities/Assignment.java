package com.emp.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name= "assignments")
public class Assignment {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long assignmentId;

	    @ManyToOne
	    @JoinColumn(name = "employee_user_id")
	    private User employee;

	    @ManyToOne
	    @JoinColumn(name = "project_id")
	    private Project project;

    
    
}