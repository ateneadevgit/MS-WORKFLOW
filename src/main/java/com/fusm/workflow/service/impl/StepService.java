package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.*;
import com.fusm.workflow.model.StepByIdModel;
import com.fusm.workflow.model.StepRequest;
import com.fusm.workflow.model.StepRoleActionRequest;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.IStepService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StepService implements IStepService {

    @Autowired
    private IStepRepository stepRepository;

    @Autowired
    private IWorkflowBaseRepository workflowBaseRepository;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private IWorkflowBaseStepFeedbackRepository workflowBaseStepFeedbackRepository;

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private IActionRepository actionRepository;

    @Autowired
    private IWorkFlowStepRepository workFlowStepRepository;

    @Autowired
    private IWorkflowRepository workflowRepository;

    @Autowired
    private ITemplateRepository templateRepository;


    @Override
    public Boolean isStepMandatory(Integer stepId) {
        Boolean isMandatory = false;
        Optional<Step> stepOptional = stepRepository.findById(stepId);
        if (stepOptional.isPresent()) isMandatory = stepOptional.get().getIsMandatory();
        return isMandatory;
    }

    @Override
    public void updateEditionOfSteps(Integer objectId) {
        List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectIdAndStatus(objectId, Constant.STATUS_CREATED);

        for (WorkflowBase base : workflowBases) {
            List<WorkflowBaseStep> workflowBaseSteps = workflowBaseStepRepository.findAllByWorkflowBaseId_WorkflowBaseId(base.getWorkflowBaseId());

            for (WorkflowBaseStep step : workflowBaseSteps) {
                if (!step.getIsEditable() && !step.getStepId().getControlId().equals(2)) {
                    step.setIsEditable(true);
                    step.setUpdatedAt(new Date());
                    workflowBaseStepRepository.save(step);
                }
            }

        }
    }

    @Override
    public void updateEditionOfStepsWithoutControl(Integer objectId) {
        List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectIdAndStatus(objectId, Constant.STATUS_CREATED);

        for (WorkflowBase base : workflowBases) {
            List<WorkflowBaseStep> workflowBaseSteps = workflowBaseStepRepository
                    .findAllByWorkflowBaseId_WorkflowBaseIdAndIsEditable(base.getWorkflowBaseId(), false);

            for (WorkflowBaseStep step : workflowBaseSteps) {
                if (!step.getIsEditable()) {
                    step.setIsEditable(true);
                    step.setUpdatedAt(new Date());
                    workflowBaseStepRepository.save(step);
                }
            }

        }
    }

    @Override
    public Boolean allMandatoryDone(Integer objectId, WorkflowBaseStep workflowBaseStep) {
        List<WorkflowStep> stepsMandatory = workFlowStepRepository.finAllMandatorySteps(workflowBaseStep.getWorkflowBaseId().getWorkflowId().getWorkflowId());
        List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectIdAndStatus(objectId, Constant.STATUS_CREATED);

        int stepsDone = 0;

        for (WorkflowStep step : stepsMandatory) {
            for (WorkflowBase base : workflowBases) {
                Optional<WorkflowBaseStep> baseStep = workflowBaseStepRepository.findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId
                        (base.getWorkflowBaseId(), step.getWorkflowStepPKId().getStepId().getStepId());
                if (baseStep.isPresent()) {
                    boolean allEvaluation = stepHasAllEvaluation(step.getWorkflowStepPKId().getStepId().getStepId(), baseStep.get().getWorkflowBaseStepId());
                    if (allEvaluation) stepsDone++;
                }
            }
        }

        return stepsDone >= stepsMandatory.size();
    }

    @Override
    public void updateStepToDone(WorkflowBaseStep workflowBaseStep) {
        workflowBaseStep.setIsDone(true);
        workflowBaseStep.setUpdatedAt(new Date());
        workflowBaseStepRepository.save(workflowBaseStep);
    }

    @Override
    public void updateStepIsSent(WorkflowBaseStep workflowBaseStep) {
        workflowBaseStep.setIsSent(false);
        workflowBaseStep.setUpdatedAt(new Date());
        workflowBaseStepRepository.save(workflowBaseStep);
    }

    @Override
    public Boolean stepHasAllEvaluation(Integer stepId, Integer workflowBaseStepId) {
        List<StepRoleAction> stepRoleActionList = stepRoleActionRepository.findAllByActionId_ActionIdAndStepId_StepId(
                sharedMethods.getSettingValue(Constant.EVALUATE_STEP_ACTION), stepId);
        return stepRoleActionList.size() == currentEvaluationOfStep(stepRoleActionList, workflowBaseStepId);
    }

    @Override
    public Boolean hasAllSummaryEvaluation(Integer stepId, Integer workflowBaseStepId) {
        List<Integer> getSummaryEvaluators = stepRoleActionRepository.getSummaryEvaluators(stepId);
        Integer approvedStatus = sharedMethods.getSettingValue(Constant.STEP_APPROVED);
        int evaluation = 0;

        for (Integer roleId : getSummaryEvaluators) {
            List<WorkflowBaseStepFeedback> hasSummaryEvaluation = workflowBaseStepFeedbackRepository
                    .lastSummaryReview(workflowBaseStepId, stepId, roleId);
            if (!hasSummaryEvaluation.isEmpty()) {
                if (hasSummaryEvaluation.get(0).getStatus().equals(approvedStatus)) evaluation += 1;
            }
        }

        return getSummaryEvaluators.size() == evaluation;
    }

    @Override
    public Boolean allStepsFinished(Integer objectId) {
        List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectIdAndStatus(objectId, Constant.STATUS_CREATED);
        int totalStepsDone = 0;
        int totalStepsSpected = 0;

        for (WorkflowBase workflowBase : workflowBases) {
            List<WorkflowBaseStep> stepsDone = workflowBaseStepRepository
                    .findAllByWorkflowBaseId_WorkflowBaseIdAndIsDoneAndIsEditable(workflowBase.getWorkflowBaseId(), true, true);
            totalStepsDone += stepsDone.size();
            List<WorkflowBaseStep> stepsSpected = workflowBaseStepRepository
                    .findAllByWorkflowBaseId_WorkflowBaseIdAndIsEditable(workflowBase.getWorkflowBaseId(), true);
            totalStepsSpected += stepsSpected.size();
        }
        return totalStepsDone == totalStepsSpected;
    }

    @Override
    public void createStep(StepRequest stepRequest, Integer workflowId) {
        Optional<Workflow> workflowOptional = workflowRepository.findById(workflowId);
        if (workflowOptional.isPresent()) {
            Step newStep = stepRepository.save(
                    Step.builder()
                            .stepName(stepRequest.getName())
                            .controlId(Constant.STEP_DEFAULT_CONTROL)
                            .createdAt(new Date())
                            .createdBy(stepRequest.getCreatedBy())
                            .isMandatory(stepRequest.getIsPrerrequeriment())
                            .hasSummary(stepRequest.getHasSummary())
                            .enabled(true)
                            .build()
            );
            saveStepRoleAction(stepRequest.getRoleActions(), newStep, stepRequest.getStepOrder());
            workFlowStepRepository.save(
                    WorkflowStep.builder()
                            .workflowStepPKId(
                                    WorkflowStepPKId.builder()
                                            .stepId(newStep)
                                            .workflowId(workflowOptional.get())
                                            .build()
                            )
                            .stepOrder(stepRequest.getStepOrder())
                            .enabled(true)
                            .build()
            );
        }
    }

    @Override
    public void enableDisableStep(Integer stepId, Integer workflowId, Boolean enabled) {
        Optional<Workflow> workflowOptional = workflowRepository.findById(workflowId);
        Optional<Step> stepOptional = stepRepository.findById(stepId);
        if (workflowOptional.isPresent() && stepOptional.isPresent()) {
            Optional<WorkflowStep> workflowStepOptional = workFlowStepRepository
                    .findById(WorkflowStepPKId.builder()
                            .stepId(stepOptional.get())
                            .workflowId(workflowOptional.get())
                            .build());
            if (workflowStepOptional.isPresent()) {
                WorkflowStep workflowStep = workflowStepOptional.get();
                workflowStep.setEnabled(enabled);
                workFlowStepRepository.save(workflowStep);
            }
        }
    }

    @Override
    public StepByIdModel getStepById(Integer stepId, Integer workflowId) {
        StepByIdModel stepByIdModel = new StepByIdModel();
        Optional<Workflow> workflowOptional = workflowRepository.findById(workflowId);
        Optional<Step> stepOptional = stepRepository.findById(stepId);
        if (workflowOptional.isPresent() && stepOptional.isPresent()) {
            Optional<WorkflowStep> workflowStepOptional = workFlowStepRepository
                    .findById(WorkflowStepPKId.builder()
                            .stepId(stepOptional.get())
                            .workflowId(workflowOptional.get())
                            .build());
            if (workflowStepOptional.isPresent()) {
                stepByIdModel = StepByIdModel.builder()
                        .name(workflowStepOptional.get().getWorkflowStepPKId().getStepId().getStepName())
                        .stepOrder(workflowStepOptional.get().getStepOrder())
                        .isPrerrequeriment(workflowStepOptional.get().getWorkflowStepPKId().getStepId().getIsMandatory())
                        .hasSummary(workflowStepOptional.get().getWorkflowStepPKId().getStepId().getHasSummary())
                        .minimumRequired(getAttachRequirement(stepId))
                        .roleActions(getActionRelatedWithStep(stepId))
                        .build();
            }
        }
        return stepByIdModel;
    }

    @Override
    public void updateStep(StepRequest stepRequest, Integer workflowId, Integer stepId) {
        Optional<Workflow> workflowOptional = workflowRepository.findById(workflowId);
        Optional<Step> stepOptional = stepRepository.findById(stepId);
        if (workflowOptional.isPresent() && stepOptional.isPresent()) {
            Optional<WorkflowStep> workflowStepOptional = workFlowStepRepository
                    .findById(WorkflowStepPKId.builder()
                            .stepId(stepOptional.get())
                            .workflowId(workflowOptional.get())
                            .build());
            Step step = stepOptional.get();
            step.setStepName(stepRequest.getName());
            step.setIsMandatory(stepRequest.getIsPrerrequeriment());
            step.setHasSummary(stepRequest.getHasSummary());
            stepRepository.save(step);
            if (workflowStepOptional.isPresent()) {
                WorkflowStep workflowStep = workflowStepOptional.get();
                workflowStep.setStepOrder(stepRequest.getStepOrder());
                workFlowStepRepository.save(workflowStep);
            }
        }
    }

    private Integer currentEvaluationOfStep(List<StepRoleAction> stepRoleActionList, Integer workflowBaseStepId) {
        int numberEvaluations = 0;
        Integer approvedStatus = sharedMethods.getSettingValue(Constant.STEP_APPROVED);

        for (StepRoleAction stepRoleAction : stepRoleActionList) {
            Integer evaluation = workflowBaseStepFeedbackRepository.findAttachStatusByRole(
                    stepRoleAction.getRoleId(),
                    workflowBaseStepId
            );
            if (evaluation.equals(approvedStatus)) numberEvaluations++;
        }
        return numberEvaluations;
    }

    private void saveStepRoleAction(List<StepRoleActionRequest> roleActions, Step step, Integer order) {
        for (StepRoleActionRequest stepRoleActionRequest : roleActions) {
            for (Action action: stepRoleActionRequest.getActionIds()) {
                Optional<Action> actionOptional = actionRepository.findById(action.getActionId());
                actionOptional.ifPresent(value -> stepRoleActionRepository.save(
                        StepRoleAction.builder()
                                .roleId(stepRoleActionRequest.getRoleId())
                                .stepId(step)
                                .actionId(value)
                                .build()
                ));
            }
        }
    }

    private String getAttachRequirement(Integer stepId) {
        List<Template> template = templateRepository.findByStepId(stepId);
        return (!template.isEmpty()) ? template.get(0).getDescription() : null;
    }

    private List<StepRoleActionRequest> getActionRelatedWithStep(Integer stepId) {
        List<StepRoleAction> stepRoleActionList = stepRoleActionRepository
                .findAllByStepId_StepId(stepId);
        Map<Integer, List<Action>> roleActionMap = new HashMap<>();
        List<StepRoleActionRequest> actionResponse = new ArrayList<>();

        for (StepRoleAction stepRoleAction : stepRoleActionList) {
            if (!roleActionMap.containsKey(stepRoleAction.getRoleId())) {
                List<Action> actionList = new ArrayList<>();
                actionList.add(stepRoleAction.getActionId());
                roleActionMap.put(stepRoleAction.getRoleId(), actionList);
            } else {
                List<Action> actionList = roleActionMap.get(stepRoleAction.getRoleId());
                actionList.add(stepRoleAction.getActionId());
                roleActionMap.put(stepRoleAction.getRoleId(), actionList);
            }
        }

        for (Integer key : roleActionMap.keySet()) {
            actionResponse.add(
                    StepRoleActionRequest.builder()
                            .roleId(key)
                            .actionIds(roleActionMap.get(key))
                            .build()
            );
        }

        return actionResponse;
    }

}
