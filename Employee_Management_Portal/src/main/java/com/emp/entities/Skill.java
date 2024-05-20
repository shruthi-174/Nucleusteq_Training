package com.emp.entities;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    private String name;

    @OneToMany(mappedBy = "skill")
    private List<RequestResource> skillRequestResources;
    
    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL)
    private List<User> employees;
    
	public Skill() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
