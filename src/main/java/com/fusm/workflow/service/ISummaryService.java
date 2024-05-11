package com.fusm.workflow.service;

import com.fusm.workflow.entity.Summary;
import com.fusm.workflow.model.ReviewListModel;
import com.fusm.workflow.model.SummaryModel;
import com.fusm.workflow.model.SummaryRequest;
import org.springframework.stereotype.Service;

@Service
public interface ISummaryService {

    void createSummary(SummaryRequest summaryRequest);
    SummaryModel getSummary(ReviewListModel reviewListModel);
    SummaryModel getSummaryByProgramAndType(Integer programId, Integer type);
    void updateSummary(SummaryRequest summaryRequest);
    void updateSummarWithHistory(SummaryRequest summaryRequest);
    void sendSummaryToEvaluation(Integer summaryId);
    Boolean hasAlreadyEvaluated(Integer summaryId, Integer roleId);
    Boolean hasAlreadySendToEvaluation(Integer summaryId);
    void editIsSentOfSummary(Summary summary);

}
