package com.fusm.workflow.repository;

import com.fusm.workflow.entity.WorkflowBaseLastAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkflowBaseLastActionRepository extends JpaRepository<WorkflowBaseLastAction, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM workflow_base_last_action " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_step_id = workflow_base_last_action.workflow_base_step_id " +
                    "where workflow_base_step.workflow_base_id = :workflowId " +
                    "and workflow_base_step.step_id = :stepId " +
                    "and workflow_base_last_action.role_id = :roleId " +
                    "and workflow_base_last_action.enabled = true " +
                    "order by workflow_base_last_action.created_at desc ",
            nativeQuery = true
    )
    List<WorkflowBaseLastAction> findLastAction(
        @Param("workflowId") Integer workflowBaseId,
        @Param("stepId") Integer stepId,
        @Param("roleId") Integer roleId
    );

    @Query(
            value = "SELECT * " +
                    "FROM workflow_base_last_action " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_step_id = workflow_base_last_action.workflow_base_step_id " +
                    "where workflow_base_step.workflow_base_id = :workflowId " +
                    "and workflow_base_step.step_id = :stepId " +
                    "and workflow_base_last_action.enabled = true",
            nativeQuery = true
    )
    List<WorkflowBaseLastAction> findLastActionActive(
            @Param("workflowId") Integer workflowBaseId,
            @Param("stepId") Integer stepId
    );

}
