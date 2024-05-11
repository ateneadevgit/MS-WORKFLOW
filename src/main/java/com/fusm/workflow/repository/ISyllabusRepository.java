package com.fusm.workflow.repository;

import com.fusm.workflow.entity.StepRoleAction;
import com.fusm.workflow.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISyllabusRepository extends JpaRepository<Syllabus, Integer> {

    @Query(
            value = "select * from Syllabus " +
                    "where curriculum_id = :currilumId " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<Syllabus> findAllByCurriculumId(@Param("currilumId") Integer currilumId);

    @Query(
            value = "SELECT * " +
                    "FROM syllabus " +
                    "WHERE id_syllabus = :syllabusId AND enabled = true " +
                    "order by 1 asc",
            nativeQuery = true)
    Optional<Syllabus> findBySyllabusId(@Param("syllabusId") Integer syllabusId);

    @Query(
            value = "SELECT * " +
                    "FROM ( " +
                    "    SELECT *, " +
                    "        ROW_NUMBER() OVER (PARTITION BY syllabus.curriculum_id ORDER BY syllabus.created_at DESC) AS rn " +
                    "    FROM syllabus " +
                    ") AS syllabus_lastest " +
                    "JOIN workflow_base_step ON workflow_base_step.workflow_base_step_id = syllabus_lastest.workflow_base_step_id " +
                    "JOIN workflow_base ON workflow_base.id_workflow_base = workflow_base_step.workflow_base_id " +
                    "WHERE syllabus_lastest.rn = 1 " +
                    "and workflow_base.workflow_object_id = :programId ",
            nativeQuery = true
    )
    List<Syllabus> findAllSyllabysByProgram(@Param("programId") Integer programId);

}
