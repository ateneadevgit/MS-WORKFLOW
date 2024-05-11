package com.fusm.workflow.repository;

import com.fusm.workflow.entity.ComplementarySubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComplementarySubjectRepository extends JpaRepository<ComplementarySubject, Integer> {
}
