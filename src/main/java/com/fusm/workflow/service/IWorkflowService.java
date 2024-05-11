package com.fusm.workflow.service;

import com.fusm.workflow.entity.Workflow;
import com.fusm.workflow.model.WorkflowRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWorkflowService {

    List<Workflow> getWorkflows();
    void updateWorkflow(WorkflowRequest workflowRequest, Integer workflowId);

}
