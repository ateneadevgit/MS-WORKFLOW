package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.WorkflowBaseLastAction;
import com.fusm.workflow.entity.WorkflowBaseStep;
import com.fusm.workflow.repository.IWorkflowBaseLastActionRepository;
import com.fusm.workflow.repository.IWorkflowBaseStepRepository;
import com.fusm.workflow.service.IWorkflowBaseLastActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WorkflowBaseLastActionService implements IWorkflowBaseLastActionService {

    @Autowired
    private IWorkflowBaseLastActionRepository workflowBaseLastActionRepository;


    @Override
    public void createLastAction(Integer roleId, Integer actionId, WorkflowBaseStep workflowBaseStep) {
        workflowBaseLastActionRepository.save(
                WorkflowBaseLastAction.builder()
                        .roleId(roleId)
                        .actionId(actionId)
                        .createdAt(new Date())
                        .enabled(true)
                        .workflowBaseStepId(workflowBaseStep)
                        .build()
        );
    }

}
