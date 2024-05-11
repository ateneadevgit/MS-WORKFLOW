package com.fusm.workflow.service;

import com.fusm.workflow.entity.WorkflowBaseStepReview;
import com.fusm.workflow.model.ReviewListModel;
import com.fusm.workflow.model.ReviewModel;
import com.fusm.workflow.model.ReviewRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWorkflowBaseStepReviewService {

    void createReview(ReviewRequest reviewRequest);
    List<ReviewModel> getReviewByStep(ReviewListModel reviewListModel);

}
