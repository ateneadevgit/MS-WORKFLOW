package com.fusm.workflow.repository;

import com.fusm.workflow.entity.ProgramModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProgramModuleRepository extends JpaRepository<ProgramModule, Integer> {
}
