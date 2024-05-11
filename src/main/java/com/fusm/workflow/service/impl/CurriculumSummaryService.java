package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.*;
import com.fusm.workflow.model.CurriculumSummaryModel;
import com.fusm.workflow.model.CurriculumSummaryRequest;
import com.fusm.workflow.model.ReviewListModel;
import com.fusm.workflow.model.UpdateCurriculumSummary;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.ICurriculumSummaryService;
import com.fusm.workflow.util.Constant;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CurriculumSummaryService implements ICurriculumSummaryService {

    @Autowired
    private ICurriculumSummaryRepository curriculumSummaryRepository;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private HistoryExtendedService historyExtendedService;

    Gson gson = new Gson();


    @Override
    public void createCurruculumSummary(CurriculumSummaryRequest curriculumSummaryRequest) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(curriculumSummaryRequest.getWorkflowId(), curriculumSummaryRequest.getStepId());

        workflowBaseStepOptional.ifPresent(
                workflowBaseStep ->
                    curriculumSummaryRepository.save(
                            CurriculumSummary.builder()
                                    .summary(curriculumSummaryRequest.getSummary())
                                    .curriculumType(curriculumSummaryRequest.getCurriculumType())
                                    .createdAt(new Date())
                                    .createdBy(curriculumSummaryRequest.getCreatedBy())
                                    .workflowBaseStepId(workflowBaseStep)
                                    .build())
        );
    }

    @Override
    public void createCurruculumSummaryWithHistoric(CurriculumSummaryRequest curriculumSummaryRequest) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(curriculumSummaryRequest.getWorkflowId(), curriculumSummaryRequest.getStepId());
        workflowBaseStepOptional.ifPresent(
                workflowBaseStep -> {
                    curriculumSummaryRepository.save(
                            CurriculumSummary.builder()
                                    .summary(curriculumSummaryRequest.getSummary())
                                    .curriculumType(curriculumSummaryRequest.getCurriculumType())
                                    .createdAt(new Date())
                                    .createdBy(curriculumSummaryRequest.getCreatedBy())
                                    .workflowBaseStepId(workflowBaseStep)
                                    .build());
                    historyExtendedService.createHistoric(
                            gson.toJson(getCurriculumSummaryByProgram(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId(), curriculumSummaryRequest.getCurriculumType())),
                            getModuleId(curriculumSummaryRequest.getCurriculumType()),
                            workflowBaseStepOptional.get().getWorkflowBaseId().getWorkflowObjectId(),
                            curriculumSummaryRequest.getCreatedBy(),
                            null);
                }
        );
    }

    @Override
    public CurriculumSummaryModel getCurriculumSummary(ReviewListModel reviewListModel, Integer typeCurriculum) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(reviewListModel.getWorkflowId(), reviewListModel.getStepId());

        CurriculumSummaryModel summary = new CurriculumSummaryModel();

        if (workflowBaseStepOptional.isPresent()) {
            List<CurriculumSummary> summaryList = curriculumSummaryRepository
                    .findCurriculumSummaryByStep(workflowBaseStepOptional.get().getWorkflowBaseStepId(), typeCurriculum);
            if (!summaryList.isEmpty()) {
                summary = CurriculumSummaryModel.builder()
                        .curriculumSummaryId(summaryList.get(0).getCurriculumSummaryId())
                        .curriculumSummary(summaryList.get(0).getSummary())
                        .build();
            }
        }

        return summary;
    }

    @Override
    public void updateCurruculumSummary(UpdateCurriculumSummary updateCurriculumSummary, Integer summaryId) {
        Optional<CurriculumSummary> summaryOptional = curriculumSummaryRepository.findById(summaryId);
        if (summaryOptional.isPresent()) {
            CurriculumSummary summary = summaryOptional.get();
            summary.setSummary(updateCurriculumSummary.getCurriculum());
            summary.setUpdatedAt(new Date());
            curriculumSummaryRepository.save(summary);
        }
    }

    @Override
    public CurriculumSummaryModel getCurriculumSummaryByProgram(Integer programId, Integer type) {
        CurriculumSummaryModel curriculumSummaryModel = new CurriculumSummaryModel();

        List<CurriculumSummary> summaryList = curriculumSummaryRepository
                .findCurriculumSummaryByProgram(programId, type);
        if (!summaryList.isEmpty()) {
            curriculumSummaryModel = CurriculumSummaryModel.builder()
                    .curriculumSummaryId(summaryList.get(0).getCurriculumSummaryId())
                    .curriculumSummary(summaryList.get(0).getSummary())
                    .build();
        }

        return curriculumSummaryModel;
    }

    private int getModuleId(Integer curriculumSummaryType) {
        int moduleId = 0;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_GRADUATE_PROFILE)) moduleId = Constant.MODULE_CURRICULAR_PROCESS_PROFILES;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_RAE)) moduleId = Constant.MODULE_RAE;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_COMPETENCIES)) moduleId = Constant.MODULE_COMPETENCIES;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_PROGRAM_OBJECTIVES)) moduleId = Constant.MODULE_TRAINING_OBJECTIVES;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_ENGLISH_TRAINING)) moduleId = Constant.MODULE_ENGLISH_TRAINING;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_ACADEMIC_FIELD_PROGRAMS)) moduleId = Constant.MODULE_ACADEMIC_FIELD_PROGRAMS;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_FORMATIVE_RESEARCH)) moduleId = Constant.MODULE_FORMATIVE_RESEARCH;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_EXTENSION_OR_SOCIAL_PROJECT)) moduleId = Constant.MODULE_EXTENSION_OR_SOCIAL_PROJECTION;
        if (curriculumSummaryType.equals(Constant.CURRICULUM_INTERNATIONALIZATION)) moduleId = Constant.MODULE_INTERNATIONALIZATION;
        return moduleId;
    }

}
