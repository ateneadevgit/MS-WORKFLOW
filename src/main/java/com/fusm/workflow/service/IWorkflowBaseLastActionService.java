package com.fusm.workflow.service;

import com.fusm.workflow.entity.WorkflowBaseStep;
import org.springframework.stereotype.Service;

@Service
public interface IWorkflowBaseLastActionService {

    void createLastAction(Integer roleId, Integer actionId, WorkflowBaseStep workflowBaseStep);

}
