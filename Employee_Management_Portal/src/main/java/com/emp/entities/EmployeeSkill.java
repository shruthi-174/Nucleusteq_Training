package com.emp.entities;
import javax.persistence.*;


@Entity
@Table(name= "employee_skills")
public class EmployeeSkill {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeSkillId;

    @ManyToOne
    @JoinColumn(name = "employee_user_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
	
}
