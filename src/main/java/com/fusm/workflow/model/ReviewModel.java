package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewModel {

    private Integer reviewId;
    private String review;
    private Integer roleId;
    private String createdBy;
    private Date createdAt;
    private String sender;
    private Boolean isRead;
    private List<ReviewModel> replies;

    public void addRepliedTo(ReviewModel reviewModel) {
        replies.add(reviewModel);
    }

}
