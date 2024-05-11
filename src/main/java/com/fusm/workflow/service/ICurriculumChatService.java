package com.fusm.workflow.service;

import com.fusm.workflow.model.CurriculumChatRequest;
import com.fusm.workflow.model.GetReviewModel;
import com.fusm.workflow.model.ReviewModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICurriculumChatService {

    void createReview(CurriculumChatRequest curriculumChatRequest);
    List<ReviewModel> getReviews(GetReviewModel getReviewModel);
    void readCurriculumChat(List<Integer> curriculumChats);
}
