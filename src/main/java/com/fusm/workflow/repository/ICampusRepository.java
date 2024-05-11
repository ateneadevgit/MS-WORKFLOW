package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICampusRepository extends JpaRepository<Campus, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM campus " +
                    "WHERE campus.program_id = :programId " +
                    "AND campus.value NOT IN (:campusId)",
            nativeQuery = true
    )
    List<Campus> findCampusNotIncluded(
            @Param("campusId") Integer[] campusId,
            @Param("programId") Integer programId);


    @Query(
            value = "SELECT value " +
                    "FROM campus " +
                    "WHERE program_id = :programId AND enabled = true",
            nativeQuery = true)
    List<Integer> findByProgramId_ProgramId(Integer programId);

}
