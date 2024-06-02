package com.emp.entities;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "manager")
    @JsonManagedReference(value = "project-manager")
    private List<Project> managerProjects;

    @OneToMany(mappedBy = "manager")
    @JsonIgnoreProperties(value = {"manager", "employee"})
    private List<RequestResource> managerRequestResources;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = {"manager", "employee"})
    private List<RequestResource> employeeRequestResource;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("employees") 
    @JoinTable(
        name = "employee_skills",
        joinColumns = @JoinColumn(name = "employee_user_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;


    public User() {
		super();
		// TODO Auto-generated constructor stub
	}

    public User(String firstname, String lastname, String email, String password, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	public List<Project> getManagerProjects() {
		return managerProjects;
	}


	public void setManagerProjects(List<Project> managerProjects) {
		this.managerProjects = managerProjects;
	}


	public List<RequestResource> getManagerRequestResources() {
		return managerRequestResources;
	}


	public void setManagerRequestResources(List<RequestResource> managerRequestResources) {
		this.managerRequestResources = managerRequestResources;
	}


	public List<RequestResource> getEmployeeRequestResource() {
		return employeeRequestResource;
	}

	public void setEmployeeRequestResource(List<RequestResource> employeeRequestResource) {
		this.employeeRequestResource = employeeRequestResource;
	}

	public List<Skill> getSkills() {
		return skills;
	}


	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	public enum Role {
        ADMIN,
        EMPLOYEE,
        MANAGER
    }
}
