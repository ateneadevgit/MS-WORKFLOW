package com.fusm.workflow.service;

import com.fusm.workflow.model.StepByWorkflowModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWorkflowStepService {

    List<StepByWorkflowModel> getStepsByWorkflow(Integer workflowId);

}
