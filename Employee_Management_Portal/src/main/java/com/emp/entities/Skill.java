package com.emp.entities;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;
    private String name;

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("skills")
    private List<User> employees;

    public Skill() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Skill(Long skillId, String name, List<RequestResource> skillRequestResources, List<User> employees) {
        super();
        this.skillId = skillId;
        this.name = name;
        this.employees = employees;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public String getCleanedName() {
        return name.replaceAll("\\s+", " ").trim();
    }
}
