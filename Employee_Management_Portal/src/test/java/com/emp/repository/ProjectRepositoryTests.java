package com.emp.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.emp.entities.Project;
import com.emp.entities.User;
import com.emp.entities.User.Role;

public class ProjectRepositoryTests {

    @Mock
    private ProjectRepository projectRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        User manager = new User("Manager1", "Mgr", "mgr1@nucleusteq.com", "Password123", Role.MANAGER);
        manager.setUserId(1L);

        Project project = new Project();
        project.setProjectId(1L);
        project.setManager(manager);

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));

        List<Project> projects = projectRepository.findAll();

        assertNotNull(projects);
        assertEquals(1, projects.size());
        assertEquals(1L, projects.get(0).getProjectId());
        assertEquals(1L, projects.get(0).getManager().getUserId());
    }

    @Test
    public void testFindById() {
        User manager = new User("Manager1", "Mgr", "mgr1@nucleusteq.com", "Password123", Role.MANAGER);
        manager.setUserId(1L);

        Project project = new Project();
        project.setProjectId(1L);
        project.setManager(manager);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> foundProject = projectRepository.findById(1L);

        assertTrue(foundProject.isPresent());
        assertEquals(1L, foundProject.get().getProjectId());
        assertEquals(1L, foundProject.get().getManager().getUserId());
    }

    @Test
    public void testSave() {
        User manager = new User("Manager1", "Mgr", "mgr1@nucleusteq.com", "Password123", Role.MANAGER);
        manager.setUserId(1L);

        Project project = new Project();
        project.setProjectId(1L);
        project.setManager(manager);

        when(projectRepository.save(project)).thenReturn(project);

        Project savedProject = projectRepository.save(project);

        assertNotNull(savedProject);
        assertEquals(1L, savedProject.getProjectId());
        assertEquals(1L, savedProject.getManager().getUserId());
    }
}
