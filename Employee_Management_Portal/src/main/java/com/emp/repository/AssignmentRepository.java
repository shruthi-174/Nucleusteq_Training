package com.emp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.emp.entities.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByEmployeeUserId(Long employeeId);
    Optional<Assignment> findByProjectProjectIdAndEmployeeUserId(Long projectId, Long employeeId);
}
