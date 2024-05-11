package com.fusm.workflow.repository;

import com.fusm.workflow.entity.WorkflowBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkflowBaseRepository extends JpaRepository<WorkflowBase, Integer> {

    List<WorkflowBase> findAllByWorkflowId_WorkflowIdAndWorkflowObjectId(
            Integer workflowId, Integer objectId);

    List<WorkflowBase> findAllByWorkflowObjectIdAndStatus(Integer programId, String status);

    List<WorkflowBase> findAllByWorkflowObjectId(Integer programId);

}
