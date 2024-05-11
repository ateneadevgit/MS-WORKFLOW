package com.fusm.workflow.repository;

import com.fusm.workflow.entity.ComplementaryCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComplementaryCoreRepository extends JpaRepository<ComplementaryCore, Integer> {
}
