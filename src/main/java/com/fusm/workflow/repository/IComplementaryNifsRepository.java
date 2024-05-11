package com.fusm.workflow.repository;

import com.fusm.workflow.entity.ComplementaryNifs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComplementaryNifsRepository extends JpaRepository<ComplementaryNifs, Integer> {
}
