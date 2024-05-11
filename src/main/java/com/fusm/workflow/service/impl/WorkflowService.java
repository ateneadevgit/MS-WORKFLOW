package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Workflow;
import com.fusm.workflow.model.WorkflowRequest;
import com.fusm.workflow.repository.IWorkflowRepository;
import com.fusm.workflow.service.IWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkflowService implements IWorkflowService {

    @Autowired
    private IWorkflowRepository workflowRepository;


    @Override
    public List<Workflow> getWorkflows() {
        return workflowRepository.finAllOrdered();
    }

    @Override
    public void updateWorkflow(WorkflowRequest workflowRequest, Integer workflowId) {
        Optional<Workflow> workflowOptional = workflowRepository.findById(workflowId);
        if (workflowOptional.isPresent()) {
            Workflow workflow = workflowOptional.get();
            workflow.setName(workflowRequest.getName());
            workflow.setDescription(workflowRequest.getDescription());
            workflowRepository.save(workflow);
        }
    }

}
