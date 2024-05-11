package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    private Integer workflowId;
    private Integer stepId;
    private String review;
    private String createdBy;
    private Integer roleId;
    private Integer replyTo;

}
