package com.fusm.workflow.repository;

import com.fusm.workflow.entity.ProblemCompetence;
import com.fusm.workflow.entity.ProblemCompetencePKId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProblemCompetenceRepository extends JpaRepository<ProblemCompetence, ProblemCompetencePKId> {

    Optional<ProblemCompetence> findByProblemCompetencePKId_CompetenceId_CompetenceIdAndProblemCompetencePKId_ProblemBankId_ProblemBankId(
            Integer competenceId, Integer problemBankId
    );

    List<ProblemCompetence> findAllByProblemCompetencePKId_ProblemBankId_ProblemBankId(Integer problemBankId);

    List<ProblemCompetence> findAllByProblemCompetencePKId_ProblemBankId_ProblemBankIdAndEnabled(Integer problemBankId, Boolean enabled);

}
