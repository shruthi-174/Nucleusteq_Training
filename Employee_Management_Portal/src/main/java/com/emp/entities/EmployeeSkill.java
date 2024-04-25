package com.emp.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "skills")
public class EmployeeSkill {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long skill_id;
	
	private String name;

	public EmployeeSkill() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EmployeeSkill(Long skill_id, String name) {
		super();
		this.skill_id = skill_id;
		this.name = name;
	}
	public Long getSkill_id() {
		return skill_id;
	}
	public void setSkill_id(Long skill_id) {
		this.skill_id = skill_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	
}
