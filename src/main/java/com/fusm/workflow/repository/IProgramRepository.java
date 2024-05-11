package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProgramRepository extends JpaRepository<Program, Integer> {
}
