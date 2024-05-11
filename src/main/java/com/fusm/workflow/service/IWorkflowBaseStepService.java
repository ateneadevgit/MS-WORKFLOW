package com.fusm.workflow.service;

import com.fusm.workflow.model.SendEvaluationRequest;
import com.fusm.workflow.model.StepAttachRequest;
import com.fusm.workflow.model.WorkflowStepModel;
import com.fusm.workflow.model.WorkflowStepRequest;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface IWorkflowBaseStepService {

    WorkflowStepModel getStepsOfWorkflowByRole(Integer objectId, WorkflowStepRequest workflowStepRequest);
    void loadAttachment(StepAttachRequest stepAttachRequest) throws DocumentException, IOException;
    void disableAttachment(Integer attachmentId);
    void sendStepToEvaluation(SendEvaluationRequest sendEvaluationRequest) throws DocumentException, IOException;

}
