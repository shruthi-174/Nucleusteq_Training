package com.emp.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeSkillTests {

    private EmployeeSkill employeeSkill;
    private EmployeeSkill.EmployeeSkillId employeeSkillId;

    @BeforeEach
    public void setUp() {
        employeeSkillId = new EmployeeSkill.EmployeeSkillId();
        employeeSkill = new EmployeeSkill();
    }

    @AfterEach
    public void tearDown() {
        employeeSkill = null;
        employeeSkillId = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test employeeSkillId
        employeeSkillId.setEmployeeUserId(1L);
        employeeSkillId.setSkillId(2L);
        employeeSkill.setId(employeeSkillId);

        assertEquals(Long.valueOf(1L), employeeSkill.getId().getEmployeeUserId());
        assertEquals(Long.valueOf(2L), employeeSkill.getId().getSkillId());
    }
}
