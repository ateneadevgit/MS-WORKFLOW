package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Template;
import com.fusm.workflow.model.StepByWorkflowModel;
import com.fusm.workflow.repository.ITemplateRepository;
import com.fusm.workflow.repository.IWorkFlowStepRepository;
import com.fusm.workflow.service.IWorkflowStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowStepService implements IWorkflowStepService {

    @Autowired
    private IWorkFlowStepRepository workFlowStepRepository;

    @Autowired
    private ITemplateRepository templateRepository;


    @Override
    public List<StepByWorkflowModel> getStepsByWorkflow(Integer workflowId) {
        return workFlowStepRepository.findAllByWorkflowIdOrdered(workflowId).stream().map(
                workflowStep -> {
                    Template template = getAttachRequirement(workflowStep.getWorkflowStepPKId().getStepId().getStepId());
                    return StepByWorkflowModel.builder()
                            .stepId(workflowStep.getWorkflowStepPKId().getStepId().getStepId())
                            .name(workflowStep.getWorkflowStepPKId().getStepId().getStepName())
                            .isRequired(workflowStep.getWorkflowStepPKId().getStepId().getIsMandatory())
                            .hasSummary(workflowStep.getWorkflowStepPKId().getStepId().getHasSummary())
                            .orderStep(workflowStep.getStepOrder())
                            .enabled(workflowStep.getEnabled())
                            .minimumRequired((template != null) ? template.getDescription() : null)
                            .minimumRequiredId((template != null) ? template.getTemplateId() : 0)
                            .build();
                }
        ).toList();
    }

    private Template getAttachRequirement(Integer stepId) {
        List<Template> template = templateRepository.findByStepId(stepId);
        return (!template.isEmpty()) ? template.get(0) : null;
    }

}
