package com.emp.entities;

import java.util.List;

import javax.persistence.*;


@Entity
@Table(name = "request_resources")
public class RequestResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "manager_user_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;



	    @Enumerated(EnumType.STRING)
	    private RequestStatus status;

	   
    
	    public enum RequestStatus {
	        PENDING,
	        APPROVED,
	        REJECTED
	    }
}