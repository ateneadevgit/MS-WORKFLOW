package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.*;
import com.fusm.workflow.model.EvaluateStepRequest;
import com.fusm.workflow.model.ReviewListModel;
import com.fusm.workflow.model.SummaryModel;
import com.fusm.workflow.model.SummaryRequest;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.ISummaryService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SummaryService implements ISummaryService {

    @Autowired
    private ISummaryRepository summaryRepository;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private IWorkflowBaseStepFeedbackRepository workflowBaseStepFeedbackRepository;

    @Autowired
    private HistoryExtendedService historyExtendedService;

    @Autowired


    Gson gson = new Gson();


    @Override
    public void createSummary(SummaryRequest summaryRequest) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(summaryRequest.getWorkflowId(), summaryRequest.getStepId());

        if (workflowBaseStepOptional.isPresent()) {
            WorkflowBaseStep workflowBaseStep = workflowBaseStepOptional.get();
            Summary summary = summaryRepository.save(
                    Summary.builder()
                            .summary(summaryRequest.getSummary())
                            .isSent(false)
                            .createdAt(new Date())
                            .createdBy(summaryRequest.getCreatedBy())
                            .build()
            );
            workflowBaseStep.setSummaryId(summary);
            workflowBaseStep.setUpdatedAt(new Date());
            workflowBaseStepRepository.save(workflowBaseStep);
        }

    }

    @Override
    public SummaryModel getSummary(ReviewListModel reviewListModel) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(reviewListModel.getWorkflowId(), reviewListModel.getStepId());
        SummaryModel summaryModel = new SummaryModel();

        if (workflowBaseStepOptional.isPresent()) {
            Summary summary = workflowBaseStepOptional.get().getSummaryId();
            if (summary != null) {
                if (hasCreateSummary(reviewListModel.getRoleId(), workflowBaseStepOptional.get().getStepId().getStepId()) ||
                        (hasEvaluateSummary(reviewListModel.getRoleId(), workflowBaseStepOptional.get().getStepId().getStepId()) && summary.getIsSent())) {
                    summaryModel = SummaryModel.builder()
                            .summaryId(summary.getSummaryId())
                            .summary(summary.getSummary())
                            .build();
                }
            }
        }
        return summaryModel;
    }

    @Override
    public SummaryModel getSummaryByProgramAndType(Integer programId, Integer type) {
        SummaryModel summaryModel = new SummaryModel();
        List<WorkflowBaseStep> workflowBaseSteps = workflowBaseStepRepository.findCurricularComponentByProgram(programId, type);
        if (!workflowBaseSteps.isEmpty()) {
            summaryModel = SummaryModel.builder()
                    .summary(workflowBaseSteps.get(0).getSummaryId().getSummary())
                    .summaryId(workflowBaseSteps.get(0).getSummaryId().getSummaryId())
                    .build();
        }
        return summaryModel;
    }

    @Override
    public void updateSummary(SummaryRequest summaryRequest) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(summaryRequest.getWorkflowId(), summaryRequest.getStepId());
        if (workflowBaseStepOptional.isPresent()) {
            Summary summary = workflowBaseStepOptional.get().getSummaryId();
            if (summary != null) {
                summary.setSummary(summaryRequest.getSummary());
                summary.setIsSent(false);
                summary.setUpdatedAt(new Date());
                summary = summaryRepository.save(summary);
            } else {
                summary = summaryRepository.save(
                        Summary.builder()
                                .summary(summaryRequest.getSummary())
                                .isSent(false)
                                .createdAt(new Date())
                                .createdBy(summaryRequest.getCreatedBy())
                                .build()
                );
            }
            historyExtendedService.createHistoric(
                    gson.toJson(summary),
                    Constant.MODULE_CURRICULAR_COMPONENTS,
                    workflowBaseStepOptional.get().getWorkflowBaseId().getWorkflowObjectId(),
                    summaryRequest.getCreatedBy(),
                    workflowBaseStepOptional.get().getStepId().getStepId());
        }
    }

    @Override
    public void updateSummarWithHistory(SummaryRequest summaryRequest) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(summaryRequest.getWorkflowId(), summaryRequest.getStepId());
        if (workflowBaseStepOptional.isPresent()) {
            Summary summary = workflowBaseStepOptional.get().getSummaryId();
            if (summary != null) {
                summary.setSummary(summaryRequest.getSummary());
                summary.setIsSent(false);
                summary.setUpdatedAt(new Date());
                summaryRepository.save(summary);
            } else {
                summaryRepository.save(
                        Summary.builder()
                                .summary(summaryRequest.getSummary())
                                .isSent(false)
                                .createdAt(new Date())
                                .createdBy(summaryRequest.getCreatedBy())
                                .build()
                );
            }

        }
    }

    @Override
    public void sendSummaryToEvaluation(Integer summaryId) {
        Optional<Summary> summaryOptional = summaryRepository.findById(summaryId);
        if (summaryOptional.isPresent()) {
            Summary summary = summaryOptional.get();
            summary.setIsSent(true);
            summary.setUpdatedAt(new Date());
            summaryRepository.save(summary);
            List<WorkflowBaseStep> workflowBaseStep = workflowBaseStepRepository.findWorkflowBaseStepBySummaryId_SummaryId(summaryId);
            if (!workflowBaseStep.isEmpty()) {
                changeStepToSummary(summary.getCreatedBy(), workflowBaseStep.get(0));
            }

        }
    }

    @Override
    public Boolean hasAlreadyEvaluated(Integer summaryId, Integer roleId) {
        boolean isEvaluated = false;
        Optional<Summary> summaryOptional = summaryRepository.findById(summaryId);

        if (summaryOptional.isPresent()) {
            List<WorkflowBaseStep> workflowBaseSteps = workflowBaseStepRepository.findAllBySummaryId_SummaryId(summaryId);
            if (!workflowBaseSteps.isEmpty()) {
                List<WorkflowBaseStepFeedback> workflowBaseStepFeedbacks = workflowBaseStepFeedbackRepository
                        .findlastStatus(roleId, workflowBaseSteps.get(0).getWorkflowBaseStepId());
                Integer onSummaryCreation = sharedMethods.getSettingValue(Constant.STEP_ON_SUMMARY);
                if (workflowBaseStepFeedbacks.get(0).getIsSummary() && !workflowBaseStepFeedbacks.get(0).getStatus().equals(onSummaryCreation)){
                    isEvaluated = true;
                }
            }
        }

        return isEvaluated;
    }

    @Override
    public Boolean hasAlreadySendToEvaluation(Integer summaryId) {
        boolean summaryIsSent = false;
        Optional<Summary> summaryOptional = summaryRepository.findById(summaryId);
        if (summaryOptional.isPresent()) {
            summaryIsSent = summaryOptional.get().getIsSent();
        }
        return summaryIsSent;
    }

    @Override
    public void editIsSentOfSummary(Summary summary) {
        summary.setIsSent(false);
        summaryRepository.save(summary);
    }

    private boolean hasCreateSummary(Integer roleId, Integer stepId) {
        List<StepRoleAction> hasCreateSummary = stepRoleActionRepository
                .findAllByRoleIdAndActionId_ActionIdAndStepId_StepId(roleId, sharedMethods.getSettingValue(Constant.CREATE_SUMMARY_ACTION), stepId);
        return !hasCreateSummary.isEmpty();
    }

    private boolean hasEvaluateSummary(Integer roleId, Integer stepId) {
        List<StepRoleAction> hasEvaluateSummary = stepRoleActionRepository
                .findAllByRoleIdAndActionId_ActionIdAndStepId_StepId(roleId, sharedMethods.getSettingValue(Constant.EVALUATE_SUMMARY_ACTION), stepId);
        return !hasEvaluateSummary.isEmpty();
    }

    private void changeStepToSummary(String createdBy, WorkflowBaseStep workflowBaseStep) {
        List<Integer> rolesRelatedWithSummary = stepRoleActionRepository.getRolesRelatedWithSummary(workflowBaseStep.getStepId().getStepId());

        for (Integer roleId : rolesRelatedWithSummary) {
            EvaluateStepRequest initSummary = EvaluateStepRequest.builder()
                    .feedbackStatus("summary")
                    .stepId(workflowBaseStep.getStepId().getStepId())
                    .isSummary(true)
                    .createdBy(createdBy)
                    .build();
            workflowBaseStepFeedbackRepository.save(
                    createBaseStepFeedback(initSummary, workflowBaseStep, roleId)
            );
        }
    }

    private WorkflowBaseStepFeedback createBaseStepFeedback(EvaluateStepRequest evaluateStepRequest, WorkflowBaseStep workflowBaseStep, Integer roleId) {
        return WorkflowBaseStepFeedback.builder()
                .status(sharedMethods.getSettingValue(Constant.STEP_ON_SUMMARY))
                .feedback(evaluateStepRequest.getFeedback())
                .roleId(roleId)
                .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                .createdBy(evaluateStepRequest.getCreatedBy())
                .isSummary(evaluateStepRequest.getIsSummary())
                .workflowBaseStepId(workflowBaseStep)
                .build();
    }

}
