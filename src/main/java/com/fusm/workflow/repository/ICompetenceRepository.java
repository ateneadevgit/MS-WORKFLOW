package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompetenceRepository extends JpaRepository<Competence, Integer> {

    @Query(
            value = "select * from competences " +
                    "where enabled = true " +
                    "and is_nif = :isNif "+
                    "order by category_id asc",
            nativeQuery = true
    )
    List<Competence> findAllCompetencesOrdered(@Param("isNif") Boolean isNif);

}
