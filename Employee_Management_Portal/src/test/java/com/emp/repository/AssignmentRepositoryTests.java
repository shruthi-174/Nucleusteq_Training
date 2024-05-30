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

import com.emp.entities.Assignment;
import com.emp.entities.Project;
import com.emp.entities.User;
import com.emp.entities.User.Role;

public class AssignmentRepositoryTests {

    @Mock
    private AssignmentRepository assignmentRepository;

    private User employee;
    private Project project;
    private Assignment assignment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new User("Employee1", "Emp", "emp1@nucleusteq.com", "Password123", Role.EMPLOYEE);
        employee.setUserId(1L);

        project = new Project();
        project.setProjectId(1L);

        assignment = new Assignment();
        assignment.setAssignmentId(1L);
        assignment.setEmployee(employee);
        assignment.setProject(project);
    }

    @Test
    public void testFindAll() {
        when(assignmentRepository.findAll()).thenReturn(Arrays.asList(assignment));

        List<Assignment> assignments = assignmentRepository.findAll();

        assertNotNull(assignments);
        assertEquals(1, assignments.size());
        assertEquals(1L, assignments.get(0).getAssignmentId());
        assertEquals(1L, assignments.get(0).getEmployee().getUserId());
        assertEquals(1L, assignments.get(0).getProject().getProjectId());
    }

    @Test
    public void testDelete() {
        doNothing().when(assignmentRepository).delete(assignment);
        assignmentRepository.delete(assignment);
    }

    @Test
    public void testSave() {
        when(assignmentRepository.save(assignment)).thenReturn(assignment);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        assertNotNull(savedAssignment);
        assertEquals(1L, savedAssignment.getAssignmentId());
        assertEquals(1L, savedAssignment.getEmployee().getUserId());
        assertEquals(1L, savedAssignment.getProject().getProjectId());
    }

    @Test
    public void testFindByEmployeeUserId() {
        when(assignmentRepository.findByEmployeeUserId(1L)).thenReturn(Arrays.asList(assignment));

        List<Assignment> assignments = assignmentRepository.findByEmployeeUserId(1L);

        assertNotNull(assignments);
        assertEquals(1, assignments.size());
        assertEquals(1L, assignments.get(0).getAssignmentId());
        assertEquals(1L, assignments.get(0).getEmployee().getUserId());
        assertEquals(1L, assignments.get(0).getProject().getProjectId());
    }

    @Test
    public void testFindByProjectProjectIdAndEmployeeUserId() {
        when(assignmentRepository.findByProjectProjectIdAndEmployeeUserId(1L, 1L)).thenReturn(Optional.of(assignment));

        Optional<Assignment> foundAssignment = assignmentRepository.findByProjectProjectIdAndEmployeeUserId(1L, 1L);

        assertTrue(foundAssignment.isPresent());
        assertEquals(1L, foundAssignment.get().getAssignmentId());
        assertEquals(1L, foundAssignment.get().getEmployee().getUserId());
        assertEquals(1L, foundAssignment.get().getProject().getProjectId());
    }
}
