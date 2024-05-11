package com.fusm.workflow.service;

import com.fusm.workflow.entity.Program;
import com.fusm.workflow.entity.WorkflowBaseStep;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;

@Service
public interface IHistoryService {

    void createHistoric(String value, Integer moduleId, Integer programId, String createdBy, Integer type);
    void createSpecificHistory(WorkflowBaseStep workflowBaseStep, Integer summaryType);
    void createCurriculumHistory(WorkflowBaseStep workflowBaseStep);
    void createSyllabusHistory(WorkflowBaseStep workflowBaseStep) throws DocumentException;
    void createCurricularComponentHistory(WorkflowBaseStep workflowBaseStep, Integer type);
    void createCreditAcademicHistory(Program program);
    void createSubCoreHistory(Program program);
    void createCoreAndSubCoreHistory(Program program);

}
