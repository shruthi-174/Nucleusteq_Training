package com.emp.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SkillTests {

    private Skill skill;
    private User userMock;

    @BeforeEach
    public void setUp() {
        skill = new Skill();
        userMock = mock(User.class);
    }

    @AfterEach
    public void tearDown() {
        skill = null;
        userMock = null;
    }

    @Test
    public void testGettersAndSetters() {
        // Test skillId
        skill.setSkillId(1L);
        assertEquals(Long.valueOf(1L), skill.getSkillId());

        // Test name
        skill.setName("Java");
        assertEquals("Java", skill.getName());

        // Test employees
        List<User> employees = new ArrayList<>();
        employees.add(userMock);
        skill.setEmployees(employees);
        assertEquals(employees, skill.getEmployees());
    }

    @Test
    public void testGetCleanedName() {
        // Test cleaned name
        skill.setName("  Java   Programming  ");
        assertEquals("Java Programming", skill.getCleanedName());
    }
}
