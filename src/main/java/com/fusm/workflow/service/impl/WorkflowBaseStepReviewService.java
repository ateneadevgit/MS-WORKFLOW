package com.fusm.workflow.service.impl;

import com.fusm.workflow.dto.AttachmentDto;
import com.fusm.workflow.entity.StepRoleAction;
import com.fusm.workflow.entity.WorkflowBaseStep;
import com.fusm.workflow.entity.WorkflowBaseStepReview;
import com.fusm.workflow.model.AttachmentModel;
import com.fusm.workflow.model.ReviewListModel;
import com.fusm.workflow.model.ReviewModel;
import com.fusm.workflow.model.ReviewRequest;
import com.fusm.workflow.repository.IStepRoleActionRepository;
import com.fusm.workflow.repository.IWorkflowBaseStepRepository;
import com.fusm.workflow.repository.IWorkflowBaseStepReviewRepository;
import com.fusm.workflow.service.IWorkflowBaseStepReviewService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkflowBaseStepReviewService  implements IWorkflowBaseStepReviewService {

    @Autowired
    private IWorkflowBaseStepReviewRepository workflowBaseStepReviewRepository;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;

    @Autowired
    private SharedMethods sharedMethods;


    @Override
    public void createReview(ReviewRequest reviewRequest) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(reviewRequest.getWorkflowId(), reviewRequest.getStepId());

        workflowBaseStepOptional.ifPresent(
                workflowBaseStep -> {
                    WorkflowBaseStepReview reply = null;

                    if (reviewRequest.getReplyTo() != null) {
                        Optional<WorkflowBaseStepReview> reviewOptional = workflowBaseStepReviewRepository.findById(reviewRequest.getReplyTo());
                        if (reviewOptional.isPresent()) reply = reviewOptional.get();
                    }

                    workflowBaseStepReviewRepository.save(
                            WorkflowBaseStepReview.builder()
                                    .review(reviewRequest.getReview())
                                    .roleId(reviewRequest.getRoleId())
                                    .createdBy(reviewRequest.getCreatedBy())
                                    .createdAt(new Date())
                                    .repliedTo(reply)
                                    .workflowBaseStepId(workflowBaseStep)
                                    .build());
                }
        );
    }

    @Override
    public List<ReviewModel> getReviewByStep(ReviewListModel reviewListModel) {

        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(reviewListModel.getWorkflowId(), reviewListModel.getStepId());

        List<ReviewModel> reviewsResponse = new ArrayList<>();

        if (workflowBaseStepOptional.isPresent()) {
            Integer evaluationAction = sharedMethods.getSettingValue(Constant.EVALUATE_STEP_ACTION);
            Integer loadFileAction = sharedMethods.getSettingValue(Constant.LOAD_FILE_ACTION);

            boolean hasEvaluation = hasAction(evaluationAction, reviewListModel);
            boolean hasLoadFile = hasAction(loadFileAction, reviewListModel);

            List<WorkflowBaseStepReview> reviews = new ArrayList<>();
            if (hasLoadFile) {
                reviews = workflowBaseStepReviewRepository.getReviewByStep(workflowBaseStepOptional.get().getWorkflowBaseStepId());
            } else if (hasEvaluation) {
                List<Integer> notEvaluators = stepRoleActionRepository.getRolesNotEvaluators(reviewListModel.getStepId());
                reviews = workflowBaseStepReviewRepository
                        .getEvaluatorsReviewByStep(workflowBaseStepOptional.get().getWorkflowBaseStepId(), notEvaluators.toArray(new Integer[0]));
            }
            reviewsResponse = getReviewsWithReplies(reviews);
        }

        return reviewsResponse;
    }

    private boolean hasAction(Integer actionId, ReviewListModel reviewListModel) {
        return !stepRoleActionRepository
                .findAllByRoleIdAndActionId_ActionIdAndStepId_StepId(reviewListModel.getRoleId(), actionId, reviewListModel.getStepId())
                .isEmpty();
    }

    private List<ReviewModel> getReviewsWithReplies(List<WorkflowBaseStepReview> reviews) {
        Map<Integer, ReviewModel> reviewFather = new HashMap<>();

        for (WorkflowBaseStepReview reviewBase : reviews) {
            if (reviewBase.getRepliedTo() == null) {
                reviewFather.put(reviewBase.getStepReviewId(), buildReview(reviewBase));
            }
        }

        for (WorkflowBaseStepReview reviewBase : reviews) {
            if (reviewBase.getRepliedTo() != null) {
                ReviewModel father = reviewFather.get(reviewBase.getRepliedTo().getStepReviewId());
                if (father!= null) father.getReplies().add(buildReview(reviewBase));
            }
        }

        return new ArrayList<>(reviewFather.values());
    }

    private ReviewModel buildReview(WorkflowBaseStepReview reviewBaseStep) {
        return ReviewModel.builder()
                .reviewId(reviewBaseStep.getStepReviewId())
                .review(reviewBaseStep.getReview())
                .roleId(reviewBaseStep.getRoleId())
                .createdAt(reviewBaseStep.getCreatedAt())
                .createdBy(reviewBaseStep.getCreatedBy())
                .replies(new ArrayList<>())
                .build();
    }

}
