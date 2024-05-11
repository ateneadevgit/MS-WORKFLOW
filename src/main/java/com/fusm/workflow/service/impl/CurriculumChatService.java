package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.CurriculumChat;
import com.fusm.workflow.model.CurriculumChatRequest;
import com.fusm.workflow.model.GetReviewModel;
import com.fusm.workflow.model.ReviewModel;
import com.fusm.workflow.repository.ICurriculumChatRepository;
import com.fusm.workflow.service.ICurriculumChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurriculumChatService implements ICurriculumChatService {

    @Autowired
    private ICurriculumChatRepository curriculumChatRepository;


    @Override
    public void createReview(CurriculumChatRequest curriculumChatRequest) {
        Optional<CurriculumChat> curriculumChatOptional = (curriculumChatRequest.getReplyTo() != null) ?
                curriculumChatRepository.findById(curriculumChatRequest.getReplyTo()) : Optional.empty();
        curriculumChatRepository.save(
                CurriculumChat.builder()
                        .sender(curriculumChatRequest.getCreatedBy())
                        .receptor(curriculumChatRequest.getSendTo())
                        .objectId(curriculumChatRequest.getObjectId())
                        .objectType(curriculumChatRequest.getObjectType())
                        .roleId(curriculumChatRequest.getRoleId())
                        .createdAt(new Date())
                        .replyTo(curriculumChatOptional.orElse(null))
                        .createdBy(curriculumChatRequest.getCreatedBy())
                        .review(curriculumChatRequest.getReview())
                        .isRead(false)
                        .build()
        );
    }

    @Override
    public List<ReviewModel> getReviews(GetReviewModel getReviewModel) {
        List<CurriculumChat> curriculumChatList = curriculumChatRepository
                .findAllChatsByUser(getReviewModel.getObjectId(), getReviewModel.getType(), getReviewModel.getUserId());
        return mapToReviewModel(curriculumChatList);
    }

    @Override
    public void readCurriculumChat(List<Integer> curriculumChats) {
        List<CurriculumChat> curriculumChatList = curriculumChatRepository.findAllById(curriculumChats);
        for (CurriculumChat review : curriculumChatList) {
            review.setIsRead(true);
            curriculumChatRepository.save(review);
        }

    }

    private List<ReviewModel> mapToReviewModel(List<CurriculumChat> reviewList) {
        Map<Integer, ReviewModel> nodeMap = new HashMap<>();

        for (CurriculumChat review : reviewList) {
            ReviewModel node = ReviewModel.builder()
                    .reviewId(review.getCurriculumChatId())
                    .review(review.getReview())
                    .roleId(review.getRoleId())
                    .createdBy(review.getCreatedBy())
                    .createdAt(review.getCreatedAt())
                    .replies(new ArrayList<>())
                    .sender(review.getSender())
                    .isRead(review.getIsRead())
                    .build();
            nodeMap.put(review.getCurriculumChatId(), node);
        }

        for (CurriculumChat review : reviewList) {
            ReviewModel currentNode = nodeMap.get(review.getCurriculumChatId());
            CurriculumChat parent = review.getReplyTo();
            if (parent != null) {
                ReviewModel parentNode = nodeMap.get(parent.getCurriculumChatId());
                if (parentNode != null) {
                    parentNode.addRepliedTo(currentNode);
                }
            }
        }

        List<ReviewModel> roots = new ArrayList<>();
        for (CurriculumChat review : reviewList) {
            ReviewModel currentNode = nodeMap.get(review.getCurriculumChatId());
            CurriculumChat parent = review.getReplyTo();
            if (parent == null) {
                roots.add(currentNode);
            }
        }

        return roots;
    }

}
