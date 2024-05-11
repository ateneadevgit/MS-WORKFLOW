package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.*;
import com.fusm.workflow.model.CoreAndSubcoreModel;
import com.fusm.workflow.model.CurriculumSemester;
import com.fusm.workflow.model.CurriculumSummaryModel;
import com.fusm.workflow.model.SubjectListModel;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.ICurriculumService;
import com.fusm.workflow.service.ICurriculumSummaryService;
import com.fusm.workflow.service.IHistoryService;
import com.fusm.workflow.service.ISyllabusService;
import com.fusm.workflow.util.Constant;
import com.google.gson.Gson;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService implements IHistoryService {

    @Autowired
    private ICurriculumSummaryService curriculumSummaryService;

    @Autowired
    private ICurriculumService curriculumService;

    @Autowired
    private ISyllabusService syllabusService;

    @Autowired
    private ICurriculumSummaryRepository curriculumSummaryRepository;

    @Autowired
    private ISyllabusRepository syllabusRepository;

    @Autowired
    private HistoryExtendedService historyExtendedService;

    Gson gson = new Gson();


    @Override
    public void createHistoric(String value, Integer moduleId, Integer programId, String createdBy, Integer type) {
        historyExtendedService.createHistoric(value, moduleId, programId, createdBy, type);
    }

    @Override
    public void createSpecificHistory(WorkflowBaseStep workflowBaseStep, Integer summaryType) {
        CurriculumSummaryModel value = curriculumSummaryService
                .getCurriculumSummaryByProgram(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId(), summaryType);
        Optional<CurriculumSummary> summaryOptional = curriculumSummaryRepository.findById(value.getCurriculumSummaryId());
        summaryOptional.ifPresent(curriculumSummary -> createHistoric(
                gson.toJson(value),
                getModuleId(summaryType),
                workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId(),
                curriculumSummary.getCreatedBy(),
                null));
    }

    @Override
    public void createCurriculumHistory(WorkflowBaseStep workflowBaseStep) {
        CurriculumSemester curriculumSemester = CurriculumSemester.builder()
                .curriculumList(curriculumService.getCurriculum(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId()))
                .semesterList(curriculumService.getSubjectBySemester(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId()))
                .build();
        createHistoric(
                gson.toJson(curriculumSemester),
                Constant.MODULE_PLAN_OF_STUDY_ORGANIZATION,
                workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId(),
                workflowBaseStep.getCreatedBy(),
                null);
    }

    @Override
    public void createSyllabusHistory(WorkflowBaseStep workflowBaseStep) throws DocumentException {
        List<Syllabus> syllabusList = syllabusRepository.findAllSyllabysByProgram(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId());
        for (Syllabus syllabus : syllabusList) {
            createHistoric(
                    syllabusService.syllabusPdf(syllabus.getCurriculumId().getCurriculumId()),
                    Constant.MODULE_SYLLABUS,
                    workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId(),
                    workflowBaseStep.getCreatedBy(),
                    syllabus.getCurriculumId().getCurriculumId());
        }
    }

    @Override
    public void createCurricularComponentHistory(WorkflowBaseStep workflowBaseStep, Integer type) {
        createHistoric(
                gson.toJson(workflowBaseStep.getSummaryId()),
                Constant.MODULE_CURRICULAR_COMPONENTS,
                workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId(),
                workflowBaseStep.getSummaryId().getCreatedBy(),
                type);
    }

    @Override
    public void createCreditAcademicHistory(Program program) {
        List<SubjectListModel> subjectListModels = curriculumService.getSubjects(program.getProgramId());
        for (SubjectListModel subject : subjectListModels) {
            createHistoric(
                    gson.toJson(subject),
                    Constant.MODULE_ACADEMIC_CREDITS,
                    program.getProgramId(),
                    program.getCreatedBy(),
                    subject.getSubjectId());
        }
    }

    @Override
    public void createSubCoreHistory(Program program) {
        List<CoreAndSubcoreModel> coreAndSubcoreModelList = curriculumService.getCoreAndSubcore(program.getProgramId());
        for (CoreAndSubcoreModel coreAndSubcoreModel : coreAndSubcoreModelList) {
            if (coreAndSubcoreModel.getType().equals(55)) {
                createHistoric(
                        gson.toJson(coreAndSubcoreModel),
                        Constant.MODULE_CURRICULAR_OUTCOMES,
                        program.getProgramId(),
                        program.getCreatedBy(),
                        coreAndSubcoreModel.getCurriculumId());
            }
        }
    }

    @Override
    public void createCoreAndSubCoreHistory(Program program) {
        List<CoreAndSubcoreModel> coreAndSubcoreModelList = curriculumService.getCoreAndSubcore(program.getProgramId());
        for (CoreAndSubcoreModel coreAndSubcoreModel : coreAndSubcoreModelList) {
            createHistoric(
                    gson.toJson(coreAndSubcoreModel),
                    Constant.MODULE_CORES_AND_SUBCORES,
                    program.getProgramId(),
                    program.getCreatedBy(),
                    coreAndSubcoreModel.getCurriculumId());
        }
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
