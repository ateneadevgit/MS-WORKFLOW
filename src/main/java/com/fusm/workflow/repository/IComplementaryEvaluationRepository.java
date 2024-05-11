package com.fusm.workflow.repository;

import com.fusm.workflow.entity.ComplementaryEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComplementaryEvaluationRepository extends JpaRepository<ComplementaryEvaluation, Integer> {
}
