package com.emp.repository;


import com.emp.entities.EmployeeSkill;
import com.emp.entities.EmployeeSkill.EmployeeSkillId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, EmployeeSkillId> {

    List<EmployeeSkill> findByIdEmployeeUserId(Long employeeId);
}