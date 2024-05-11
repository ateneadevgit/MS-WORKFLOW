package com.fusm.workflow.repository;

import com.fusm.workflow.dto.ProblemBankDto;
import com.fusm.workflow.entity.ProblemBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProblemBankRepository extends JpaRepository<ProblemBank, Integer> {

    @Query(
            value = "select * from get_problem_nif(:tittle)",
            nativeQuery = true
    )
    List<ProblemBankDto> findAllNifsOrdered(@Param("tittle") String tittle);

    @Query(
            value = "select * from get_problem_bank(:roleId, :semester, :programId, :tittle)",
            nativeQuery = true
    )
    List<ProblemBankDto> findAllProblemBank(
            @Param("roleId") Integer roleId,
            @Param("semester") Integer semester,
            @Param("programId") String programId,
            @Param("tittle") String tittle);

}
