package com.fusm.workflow.repository;

import com.fusm.workflow.entity.ProposalRenovation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProposalRenovationRepository extends JpaRepository<ProposalRenovation, Integer> {

    @Query(
            value = "select * from proposal_renovation " +
                    "where proposal_renovation.program_id = :programId " +
                    "order by proposal_renovation.created_at desc ",
            nativeQuery = true
    )
    List<ProposalRenovation> findLastUpgradeRequest(Integer programId);

}
