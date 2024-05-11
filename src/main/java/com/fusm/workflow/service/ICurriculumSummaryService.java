package com.fusm.workflow.service;

import com.fusm.workflow.model.*;
import org.springframework.stereotype.Service;

@Service
public interface ICurriculumSummaryService {

    void createCurruculumSummary(CurriculumSummaryRequest curriculumSummaryRequest);
    void createCurruculumSummaryWithHistoric(CurriculumSummaryRequest curriculumSummaryRequest);
    CurriculumSummaryModel getCurriculumSummary(ReviewListModel reviewListModel, Integer typeCurriculum);
    void updateCurruculumSummary(UpdateCurriculumSummary updateCurriculumSummary, Integer summaryId);
    CurriculumSummaryModel getCurriculumSummaryByProgram(Integer programId, Integer type);

}
