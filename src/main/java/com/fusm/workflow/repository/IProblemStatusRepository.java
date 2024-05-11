package com.fusm.workflow.repository;

import com.fusm.workflow.entity.ProblemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProblemStatusRepository extends JpaRepository<ProblemStatus, Integer> {

    @Query(
            value = "select * from problem_status " +
                    "where problem_bank_id = :problemId " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<ProblemStatus> findLastStatus(@Param("problemId") Integer problemId);

}
