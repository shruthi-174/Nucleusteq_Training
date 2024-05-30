package com.emp.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.emp.entities.Skill;

public class SkillRepositoryTests {

    @Mock
    private SkillRepository skillRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Skill skill = new Skill();
        skill.setSkillId(1L);
        skill.setName("Java");

        when(skillRepository.save(skill)).thenReturn(skill);

        Skill savedSkill = skillRepository.save(skill);

        assertNotNull(savedSkill);
        assertEquals(1L, savedSkill.getSkillId());
        assertEquals("Java", savedSkill.getName());
    }

    @Test
    public void testFindByNameIgnoreCase() {
        Skill skill = new Skill();
        skill.setSkillId(1L);
        skill.setName("Java");

        when(skillRepository.findByNameIgnoreCase("java")).thenReturn(Optional.of(skill));

        Optional<Skill> foundSkill = skillRepository.findByNameIgnoreCase("java");

        assertTrue(foundSkill.isPresent());
        assertEquals(1L, foundSkill.get().getSkillId());
        assertEquals("Java", foundSkill.get().getName());
    }

    @Test
    public void testFindById() {
        Skill skill = new Skill();
        skill.setSkillId(1L);
        skill.setName("Java");

        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        Optional<Skill> foundSkill = skillRepository.findById(1L);

        assertTrue(foundSkill.isPresent());
        assertEquals(1L, foundSkill.get().getSkillId());
        assertEquals("Java", foundSkill.get().getName());
    }
}
