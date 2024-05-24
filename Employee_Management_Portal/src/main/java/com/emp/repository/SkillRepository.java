package com.emp.repository;

import com.emp.entities.Skill;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
	Optional<Skill> findByNameIgnoreCase(String name);

	
}

