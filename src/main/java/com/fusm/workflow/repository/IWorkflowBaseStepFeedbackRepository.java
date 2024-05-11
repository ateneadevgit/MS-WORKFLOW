package com.fusm.workflow.repository;

import com.fusm.workflow.entity.StepRoleAction;
import com.fusm.workflow.entity.WorkflowBaseStepFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkflowBaseStepFeedbackRepository extends JpaRepository<WorkflowBaseStepFeedback, Integer> {

    @Query(
            value = "select status " +
                    "from workflow_base_step_feedback " +
                    "where role_id = :roleId " +
                    "and workflow_base_step_id = :workflowId " +
                    "order by created_at desc  " +
                    "limit 1",
            nativeQuery = true
    )
    Integer findAttachStatusByRole(
            @Param("roleId") Integer roleId,
            @Param("workflowId") Integer workflowBaseId
    );

    @Query(
            value = "select * " +
                    "from workflow_base_step_feedback " +
                    "where role_id = :roleId " +
                    "and workflow_base_step_id = :workflowId " +
                    "order by created_at desc  " +
                    "limit 1",
            nativeQuery = true
    )
    List<WorkflowBaseStepFeedback> findlastStatus(
            @Param("roleId") Integer roleId,
            @Param("workflowId") Integer workflowBaseId
    );

    @Query(
            value = "select status " +
                    "from workflow_base_step_feedback " +
                    "where role_id = :roleId " +
                    "and workflow_base_step_id = :workflowId " +
                    "order by created_at desc  " +
                    "limit 1",
            nativeQuery = true
    )
    Integer hasEvaluationByRole(
            @Param("roleId") Integer roleId,
            @Param("workflowId") Integer workflowBaseId
    );

    @Query(
            value = "SELECT * FROM workflow_base_step_feedback " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_step_id = workflow_base_step_feedback.workflow_base_step_id " +
                    "where workflow_base_step.workflow_base_id = :workflowId " +
                    "and workflow_base_step.step_id = :stepId " +
                    "and workflow_base_step_feedback.is_summary = true " +
                    "and workflow_base_step_feedback.role_id = :roleId " +
                    "order by workflow_base_step_feedback.created_at desc ",
            nativeQuery = true
    )
    List<WorkflowBaseStepFeedback> lastSummaryReview(
            @Param("workflowId") Integer workflowId,
            @Param("stepId") Integer stepId,
            @Param("roleId") Integer roleId
    );

}
