package com.fusm.workflow.repository;

import com.fusm.workflow.entity.StepRoleAction;
import com.fusm.workflow.entity.SyllabusComplementaryInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISyllabusComplementaryInformationRepository extends JpaRepository<SyllabusComplementaryInformation, Integer> {
}
