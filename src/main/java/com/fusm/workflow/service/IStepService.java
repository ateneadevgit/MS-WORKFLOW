package com.fusm.workflow.service;

import com.fusm.workflow.entity.Step;
import com.fusm.workflow.entity.WorkflowBaseStep;
import com.fusm.workflow.model.StepByIdModel;
import com.fusm.workflow.model.StepRequest;
import org.springframework.stereotype.Service;

@Service
public interface IStepService {

    Boolean isStepMandatory(Integer stepId);
    void updateEditionOfSteps(Integer objectId);
    void updateEditionOfStepsWithoutControl(Integer objectId);
    Boolean allMandatoryDone(Integer objectId, WorkflowBaseStep workflowBaseStep);
    void updateStepToDone(WorkflowBaseStep workflowBaseStep);
    void updateStepIsSent(WorkflowBaseStep workflowBaseStep);
    Boolean stepHasAllEvaluation(Integer stepId, Integer workflowBaseStepId);
    Boolean hasAllSummaryEvaluation(Integer stepId, Integer workflowBaseStepId);
    Boolean allStepsFinished(Integer objectId);
    void createStep(StepRequest stepRequest, Integer workflowId);
    void enableDisableStep(Integer stepId, Integer workflowId, Boolean enabled);
    StepByIdModel getStepById(Integer stepId, Integer workflowId);
    void updateStep(StepRequest stepRequest, Integer workflowId, Integer stepId);

}
