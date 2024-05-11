package com.fusm.workflow.repository;

import com.fusm.workflow.entity.WorkflowStep;
import com.fusm.workflow.entity.WorkflowStepPKId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkFlowStepRepository extends JpaRepository<WorkflowStep, WorkflowStepPKId> {

    List<WorkflowStep> findAllByWorkflowStepPKId_WorkflowId_WorkflowIdAndEnabled(Integer workflowId, Boolean enabled);

    @Query(
            value = "select * from Workflow_step " +
                    "where workflow_id = :id " +
                    "order by step_id asc " ,
            nativeQuery = true
    )
    List<WorkflowStep> findAllByWorkflowIdOrdered(
            @Param("id") Integer workflowId
    );

    @Query(
            value = "select * from Workflow_step " +
                    "join step on step.id_step = Workflow_step.step_id " +
                    "where step.is_mandatory = true " +
                    "and Workflow_step.workflow_id = :workflowId ",
            nativeQuery = true
    )
    List<WorkflowStep> finAllMandatorySteps(Integer workflowId);

}
