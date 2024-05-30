package com.emp.repository;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.emp.service.EmployeeService;
import com.emp.service.ManagerService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.emp.entities.EmployeeSkill;
import com.emp.entities.EmployeeSkill.EmployeeSkillId;
import com.emp.entities.Skill;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmployeeSkillRepositoryTests {
	@Mock
    private EmployeeSkillRepository employeeSkillRepository;
	
	@Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @InjectMocks
    private ManagerService managerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        EmployeeSkill employeeSkill1 = new EmployeeSkill(new EmployeeSkillId(1L, 1L));
        EmployeeSkill employeeSkill2 = new EmployeeSkill(new EmployeeSkillId(2L, 2L));
        
        List<EmployeeSkill> expectedEmployeeSkills = new ArrayList<>();
        expectedEmployeeSkills.add(employeeSkill1);
        expectedEmployeeSkills.add(employeeSkill2);

        when(employeeSkillRepository.findAll()).thenReturn(expectedEmployeeSkills);

        List<EmployeeSkill> actualEmployeeSkills = managerService.getAllEmployeeSkills();

        assertEquals(expectedEmployeeSkills, actualEmployeeSkills);
    }


    @Test
    void testFindByIdEmployeeUserId() {
        EmployeeSkill employeeSkill1 = new EmployeeSkill(new EmployeeSkillId(1L, 1L));
        EmployeeSkill employeeSkill2 = new EmployeeSkill(new EmployeeSkillId(1L, 2L));
        List<EmployeeSkill> employeeSkills = Arrays.asList(employeeSkill1, employeeSkill2);

        Skill skill1 = new Skill(1L, "Skill 1");
        Skill skill2 = new Skill(2L, "Skill 2");

        when(employeeSkillRepository.findByIdEmployeeUserId(1L)).thenReturn(employeeSkills);
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        when(skillRepository.findById(2L)).thenReturn(Optional.of(skill2));

        List<String> skills = employeeService.getSkillsForEmployee(1L);

        assertEquals(2, skills.size());
        assertEquals("Skill 1", skills.get(0));
        assertEquals("Skill 2", skills.get(1));
    }    
}
