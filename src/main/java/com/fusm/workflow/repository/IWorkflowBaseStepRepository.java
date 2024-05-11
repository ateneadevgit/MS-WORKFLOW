package com.fusm.workflow.repository;

import com.fusm.workflow.dto.WorkflowStepDto;
import com.fusm.workflow.entity.WorkflowBaseStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWorkflowBaseStepRepository extends JpaRepository<WorkflowBaseStep, Integer> {

    @Query(
            value = "SELECT * FROM get_steps_by_workflow_and_role(:objectId, :workflowId, :roleId)",
            nativeQuery = true
    )
    List<WorkflowStepDto> findStepsByWorkflow(
            @Param("objectId") Integer objectId,
            @Param("workflowId") Integer workflowId,
            @Param("roleId") Integer roleId);

    Optional<WorkflowBaseStep> findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(Integer workflowId,Integer stepId);

    List<WorkflowBaseStep> findAllByWorkflowBaseId_WorkflowBaseId(Integer workflowBaseId);

    List<WorkflowBaseStep> findAllByWorkflowBaseId_WorkflowBaseIdAndIsDoneAndIsEditable
            (Integer workflowBaseId, Boolean isDone, Boolean isEditable);

    List<WorkflowBaseStep> findAllByWorkflowBaseId_WorkflowBaseIdAndIsEditable
            (Integer workflowBaseId, Boolean isEditable);

    @Query(
            value = "SELECT step_id FROM workflow_base_step " +
                    "where workflow_base_id = :workflowId " +
                    "order by updated_at " +
                    "limit 1",
            nativeQuery = true
    )
    Integer findLastTouched(@Param("workflowId") Integer workflowBaseId);

    List<WorkflowBaseStep> findAllBySummaryId_SummaryId(Integer summaryId);

    @Query(
            value = "SELECT * " +
                    "FROM workflow_base_step " +
                    "join summary " +
                    "on summary.id_summary = workflow_base_step.summary_id " +
                    "join workflow_base " +
                    "on workflow_base.id_workflow_base = workflow_base_step.workflow_base_id " +
                    "where workflow_base.workflow_object_id = :programId " +
                    "and workflow_base_step.step_id = :type ",
            nativeQuery = true
    )
    List<WorkflowBaseStep> findCurricularComponentByProgram(
            @Param("programId") Integer programId,
            @Param("type") Integer typeId);

    List<WorkflowBaseStep> findWorkflowBaseStepBySummaryId_SummaryId(Integer summaryId);

}
