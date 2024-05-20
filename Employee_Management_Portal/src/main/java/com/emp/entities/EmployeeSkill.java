package com.emp.entities;
import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "employee_skills")
public class EmployeeSkill {
	 @EmbeddedId
	    private EmployeeSkillId id;

	    @Embeddable
	    public static class EmployeeSkillId implements Serializable {
	        @Column(name = "employee_user_id") 
	        private Long employeeUserId;

	        @Column(name = "skill_id") 
	        private Long skillId;

	       
	    }
}