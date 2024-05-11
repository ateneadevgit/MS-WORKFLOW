package com.fusm.workflow.service;

import com.fusm.workflow.entity.Program;
import com.fusm.workflow.model.UserData;
import com.fusm.workflow.model.WorkflowBaseRequest;
import org.springframework.stereotype.Service;

@Service
public interface IWorkflowBaseService {

    void createWorkflowBase(WorkflowBaseRequest workflowBaseRequest);
    Boolean hasFlowStarted(Integer programId);
    void relateUserToWorkflow(UserData userData, Integer programId);
    void relateUserToWorkflowFather(UserData userData, Integer programId);

}
