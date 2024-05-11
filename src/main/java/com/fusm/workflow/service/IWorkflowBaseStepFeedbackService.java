package com.fusm.workflow.service;

import com.fusm.workflow.model.EvaluateStepRequest;
import com.fusm.workflow.model.EvaluateTraceability;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface IWorkflowBaseStepFeedbackService {

    void evaluateStep(EvaluateStepRequest evaluateStepRequest) throws DocumentException, IOException;
    Integer getStatusOfStep(Integer stepId, Integer workflowBaseId, Integer roleId);
    void evaluateTraceability(EvaluateTraceability evaluateTraceability, Integer programId);
    boolean hasSummaryStatus(Integer stepId, Integer workflowBaseId, Integer roleId);

}
