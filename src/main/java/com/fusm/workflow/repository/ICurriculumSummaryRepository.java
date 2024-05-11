package com.fusm.workflow.repository;

import com.fusm.workflow.entity.CurriculumSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICurriculumSummaryRepository extends JpaRepository<CurriculumSummary, Integer> {

    @Query(
            value = "SELECT * FROM public.curriculum_summary " +
                    "where workflow_base_step_id = :workflowId " +
                    "and curriculum_type = :type " +
                    "order by created_at desc " +
                    "limit 1",
            nativeQuery = true
    )
    List<CurriculumSummary> findCurriculumSummaryByStep(
            @Param("workflowId") Integer workflowStepId,
            @Param("type") Integer curriculumType
    );

    @Query(
            value = "SELECT * " +
                    "FROM curriculum_summary " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_step_id = curriculum_summary.workflow_base_step_id " +
                    "join workflow_base " +
                    "on workflow_base.id_workflow_base = workflow_base_step.workflow_base_id " +
                    "where workflow_base.workflow_object_id = :objectId " +
                    "and curriculum_summary.curriculum_type = :typeId " +
                    "order by curriculum_summary.created_at desc ",
            nativeQuery = true
    )
    List<CurriculumSummary> findCurriculumSummaryByProgram(
            @Param("objectId") Integer objectId,
            @Param("typeId") Integer typeId
    );

}
