package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISummaryRepository extends JpaRepository<Summary, Integer> {
}
