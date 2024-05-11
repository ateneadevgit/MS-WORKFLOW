package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Action;
import com.fusm.workflow.entity.Modality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IModalityRepository extends JpaRepository<Modality, Integer> {

    @Query(
            value = "SELECT value " +
                    "FROM modality " +
                    "WHERE program_id = :programId AND enabled = true",
            nativeQuery = true)
    List<Integer> findByProgramId_ProgramId(Integer programId);

}
