package com.fusm.workflow.repository;

import com.fusm.workflow.entity.CurriculumGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICurriculumGroupRepository extends JpaRepository<CurriculumGroup, Integer> {
}
